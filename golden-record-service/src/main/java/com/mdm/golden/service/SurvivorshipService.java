package com.mdm.golden.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdm.common.dto.RawCustomerRecordDto;
import com.mdm.golden.model.AuditLog;
import com.mdm.golden.model.GoldenCustomer;
import com.mdm.golden.repository.AuditLogRepository;
import com.mdm.golden.repository.GoldenCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SurvivorshipService {

    @Autowired
    private GoldenCustomerRepository goldenRepo;

    @Autowired
    private AuditLogRepository auditRepo;

    @Autowired
    private ObjectMapper mapper;

    @Transactional
    public GoldenCustomer applySurvivorship(Long matchCandidateId, RawCustomerRecordDto raw) {
        // Find existing candidate or create new
        // For simplicity, we are either finding by an arbitrary ID if match > 85%, or
        // creating new
        GoldenCustomer golden = null;
        if (matchCandidateId != null) {
            golden = goldenRepo.findById(matchCandidateId).orElse(new GoldenCustomer());
        } else {
            golden = new GoldenCustomer();
        }

        String oldStateStr = toJson(golden);

        // Apply Logic: Prefer Non-Null or maintain existing based on data freshness
        // (For simplicity here, we assume new payload takes precedence if not null)
        if (raw.getFirstName() != null)
            golden.setFirstName(raw.getFirstName());
        if (raw.getLastName() != null)
            golden.setLastName(raw.getLastName());
        if (raw.getEmail() != null)
            golden.setEmail(raw.getEmail());
        if (raw.getPhoneNumber() != null)
            golden.setPhoneNumber(raw.getPhoneNumber());

        // Longest string rule for Address fields
        if (raw.getStreet() != null) {
            if (golden.getStreet() == null || raw.getStreet().length() > golden.getStreet().length()) {
                golden.setStreet(raw.getStreet());
            }
        }

        if (raw.getCity() != null)
            golden.setCity(raw.getCity());
        if (raw.getState() != null)
            golden.setState(raw.getState());
        if (raw.getZipCode() != null)
            golden.setZipCode(raw.getZipCode());
        if (raw.getCountry() != null)
            golden.setCountry(raw.getCountry());

        String action = golden.getId() == null ? "PROMOTION" : "MERGE_UPDATE";

        GoldenCustomer saved = goldenRepo.save(golden);

        AuditLog log = AuditLog.builder()
                .entityName("golden_customers")
                .entityId(saved.getId())
                .action(action)
                .previousState(oldStateStr)
                .newState(toJson(saved))
                .build();
        auditRepo.save(log);

        return saved;
    }

    private String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
