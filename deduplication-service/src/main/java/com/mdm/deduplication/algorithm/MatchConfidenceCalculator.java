package com.mdm.deduplication.algorithm;

import com.mdm.common.dto.GoldenCustomerDto;
import com.mdm.common.dto.MatchResultDto;
import com.mdm.common.dto.RawCustomerRecordDto;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

@Component
public class MatchConfidenceCalculator {

    private static final LevenshteinDistance distanceCalc = new LevenshteinDistance();

    private static final double WEIGHT_EMAIL = 0.40;
    private static final double WEIGHT_PHONE = 0.30;
    private static final double WEIGHT_NAME = 0.30;

    public MatchResultDto calculateScore(RawCustomerRecordDto raw, GoldenCustomerDto golden) {
        double totalScore = 0.0;
        StringBuilder rulesApplied = new StringBuilder();

        if (raw.getEmail() != null && raw.getEmail().equalsIgnoreCase(golden.getEmail())) {
            totalScore += WEIGHT_EMAIL;
            rulesApplied.append("EXACT_EMAIL,");
        }

        if (raw.getPhoneNumber() != null && raw.getPhoneNumber().equals(golden.getPhoneNumber())) {
            totalScore += WEIGHT_PHONE;
            rulesApplied.append("EXACT_PHONE,");
        }

        double nameSimilarity = calculateStringSimilarity(
                raw.getFirstName() + " " + raw.getLastName(),
                golden.getFirstName() + " " + golden.getLastName());

        if (nameSimilarity > 0.8) {
            totalScore += (WEIGHT_NAME * nameSimilarity);
            rulesApplied.append("FUZZY_NAME,");
        }

        double finalPercentage = Math.round((totalScore * 100) * 100.0) / 100.0;
        return new MatchResultDto(finalPercentage, rulesApplied.toString());
    }

    private double calculateStringSimilarity(String str1, String str2) {
        if (str1 == null || str2 == null)
            return 0.0;

        str1 = str1.trim().toLowerCase();
        str2 = str2.trim().toLowerCase();

        int maxLength = Math.max(str1.length(), str2.length());
        if (maxLength == 0)
            return 1.0;

        int distance = distanceCalc.apply(str1, str2);
        return 1.0 - ((double) distance / maxLength);
    }
}
