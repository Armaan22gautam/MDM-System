package com.mdm.ingestion.controller;

import com.mdm.common.dto.RawCustomerRecordDto;
import com.mdm.ingestion.model.RawCustomerRecord;
import com.mdm.ingestion.service.CustomerIngestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerIngestionController {

    @Autowired
    private CustomerIngestionService ingestionService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> ingestCustomer(@RequestBody RawCustomerRecordDto request) {
        RawCustomerRecord record = ingestionService.ingestRecord(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Record accepted for processing.");
        response.put("status", record.getStatus());
        response.put("ingestionId", record.getId());

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
