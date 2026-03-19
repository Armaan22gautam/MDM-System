package com.mdm.deduplication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdm.common.dto.GoldenCustomerDto;
import com.mdm.common.dto.MatchResultDto;
import com.mdm.common.dto.RawCustomerRecordDto;
import com.mdm.deduplication.algorithm.MatchConfidenceCalculator;
import com.mdm.deduplication.client.GoldenRecordClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchingEngineService {

    private static final Logger log = LoggerFactory.getLogger(MatchingEngineService.class);
    private static final double MATCH_THRESHOLD = 85.0; // 85% confidence required to merge

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GoldenRecordClient goldenRecordClient;

    @Autowired
    private MatchConfidenceCalculator calculator;

    @KafkaListener(topics = "customer-ingested", groupId = "dedup-group")
    public void consumeIngestionEvent(String message) {
        try {
            RawCustomerRecordDto rawDto = objectMapper.readValue(message, RawCustomerRecordDto.class);
            log.info("Processing Raw Record: {} from {}", rawDto.getSourceRecordId(), rawDto.getSourceSystem());

            // 1. Fetch Candidates (blocking call via Feign)
            List<GoldenCustomerDto> candidates = goldenRecordClient.searchCandidates(
                    rawDto.getEmail(),
                    rawDto.getPhoneNumber(),
                    rawDto.getFirstName(),
                    rawDto.getLastName());

            GoldenCustomerDto bestCandidate = null;
            double highestScore = 0.0;

            // 2. Evaluate Scores
            for (GoldenCustomerDto candidate : candidates) {
                MatchResultDto result = calculator.calculateScore(rawDto, candidate);
                log.info("Compared with Golden ID {}. Score: {}", candidate.getId(), result.getConfidenceScore());

                if (result.getConfidenceScore() > highestScore) {
                    highestScore = result.getConfidenceScore();
                    bestCandidate = candidate;
                }
            }

            // 3. Decide Survivorship (Merge into existing vs. Create new)
            if (highestScore >= MATCH_THRESHOLD && bestCandidate != null) {
                log.info("Match found with score {}%. Merging into Golden Record ID: {}",
                        highestScore, bestCandidate.getId());
                goldenRecordClient.applySurvivorship(bestCandidate.getId(), rawDto);
            } else {
                log.info("No match found (best score: {}%). Promoting to new Golden Record.", highestScore);
                goldenRecordClient.applySurvivorship(null, rawDto);
            }
            log.info("Finished evaluation. Triggered Survivorship.");

        } catch (Exception e) {
            log.error("Error processing message", e);
        }
    }
}
