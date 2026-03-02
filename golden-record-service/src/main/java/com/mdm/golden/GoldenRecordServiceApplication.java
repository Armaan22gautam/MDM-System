package com.mdm.golden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = { "com.mdm.golden", "com.mdm.common" })
@EnableCaching
public class GoldenRecordServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoldenRecordServiceApplication.class, args);
    }
}
