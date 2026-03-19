package com.mdm.deduplication.client;

import com.mdm.common.dto.GoldenCustomerDto;
import com.mdm.common.dto.RawCustomerRecordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "golden-record-service", url = "${golden-record.url}")
public interface GoldenRecordClient {

    @GetMapping("/search")
    List<GoldenCustomerDto> searchCandidates(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName);

    @PostMapping("/survive")
    GoldenCustomerDto applySurvivorship(@RequestParam("rawRecordId") Long rawRecordId,
            @RequestBody RawCustomerRecordDto rawDto);
}
