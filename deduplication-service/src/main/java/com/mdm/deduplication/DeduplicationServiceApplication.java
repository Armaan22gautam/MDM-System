package com.mdm.deduplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = { "com.mdm.deduplication", "com.mdm.common" })
@EnableFeignClients
public class DeduplicationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeduplicationServiceApplication.class, args);
    }
}
