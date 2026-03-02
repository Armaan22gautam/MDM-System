package com.mdm.ingestion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdm.common.dto.RawCustomerRecordDto;
import com.mdm.ingestion.model.RawCustomerRecord;
import com.mdm.ingestion.repository.RawCustomerRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerIngestionService {

    private static final Logger log = LoggerFactory.getLogger(CustomerIngestionService.class);
    private static final String TOPIC = "customer-ingested";

    @Autowired
    private RawCustomerRecordRepository repository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public RawCustomerRecord ingestRecord(RawCustomerRecordDto dto) {
        // Basic cleansing
        if (dto.getEmail() != null)
            dto.setEmail(dto.getEmail().toLowerCase().trim());
        if (dto.getFirstName() != null)
            dto.setFirstName(dto.getFirstName().trim());
        if (dto.getLastName() != null)
            dto.setLastName(dto.getLastName().trim());
        if (dto.getPhoneNumber() != null) {
            // Remove non-numeric characters except +
            dto.setPhoneNumber(dto.getPhoneNumber().replaceAll("[^\\d+]", ""));
        }

        // Upsert logic based on source system and record ID
        Optional<RawCustomerRecord> existing = repository.findBySourceSystemAndSourceRecordId(
                dto.getSourceSystem(), dto.getSourceRecordId());

        RawCustomerRecord record;
        if (existing.isPresent()) {
            record = existing.get();
            record.setFirstName(dto.getFirstName());
            record.setLastName(dto.getLastName());
            record.setEmail(dto.getEmail());
            record.setPhoneNumber(dto.getPhoneNumber());
            record.setStreet(dto.getStreet());
            record.setCity(dto.getCity());
            record.setState(dto.getState());
            record.setZipCode(dto.getZipCode());
            record.setCountry(dto.getCountry());
            record.setStatus("PENDING"); // Reset status for re-evaluation
        } else {
            record = RawCustomerRecord.builder()
                    .sourceSystem(dto.getSourceSystem())
                    .sourceRecordId(dto.getSourceRecordId())
                    .firstName(dto.getFirstName())
                    .lastName(dto.getLastName())
                    .email(dto.getEmail())
                    .phoneNumber(dto.getPhoneNumber())
                    .street(dto.getStreet())
                    .city(dto.getCity())
                    .state(dto.getState())
                    .zipCode(dto.getZipCode())
                    .country(dto.getCountry())
                    .status("PENDING")
                    .build();
        }

        RawCustomerRecord saved = repository.save(record);

        // Publish to Kafka
        publishEvent(saved.toDto());

        return saved;
    }

    private void publishEvent(RawCustomerRecordDto dto) {
        try {
            String payload = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send(TOPIC, dto.getSourceSystem() + "-" + dto.getSourceRecordId(), payload);
            log.info("Published CustomerIngestedEvent to Kafka topic: {}", TOPIC);
        } catch (Exception e) {
            log.error("Error publishing to Kafka", e);
        }
    }
}
