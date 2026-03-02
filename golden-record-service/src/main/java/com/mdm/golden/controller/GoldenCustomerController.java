package com.mdm.golden.controller;

import com.mdm.common.dto.GoldenCustomerDto;
import com.mdm.common.dto.RawCustomerRecordDto;
import com.mdm.common.exception.ResourceNotFoundException;
import com.mdm.golden.model.GoldenCustomer;
import com.mdm.golden.repository.GoldenCustomerRepository;
import com.mdm.golden.service.SurvivorshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/golden")
public class GoldenCustomerController {

    @Autowired
    private GoldenCustomerRepository repository;

    @Autowired
    private SurvivorshipService survivorshipService;

    @GetMapping("/{id}")
    @Cacheable(value = "goldenRecords", key = "#id")
    public GoldenCustomerDto getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(GoldenCustomer::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Golden Record not found with ID " + id));
    }

    @GetMapping("/search")
    public List<GoldenCustomerDto> searchCandidates(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {

        return repository.findPossibleMatches(email, phone, firstName, lastName)
                .stream()
                .map(GoldenCustomer::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/survive")
    public GoldenCustomerDto applySurvivorship(
            @RequestParam(required = false) Long rawRecordId,
            @RequestBody RawCustomerRecordDto rawDto) {
        return survivorshipService.applySurvivorship(rawRecordId, rawDto).toDto();
    }
}
