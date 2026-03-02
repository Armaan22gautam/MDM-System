package com.mdm.ingestion.model;

import com.mdm.common.dto.RawCustomerRecordDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "raw_customer_records", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "source_system", "source_record_id" })
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawCustomerRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_system", nullable = false, length = 50)
    private String sourceSystem;

    @Column(name = "source_record_id", nullable = false, length = 100)
    private String sourceRecordId;

    @Column(name = "golden_customer_id")
    private Long goldenCustomerId;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(length = 255)
    private String email;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(length = 255)
    private String street;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Column(length = 100)
    private String country;

    @Column(length = 50)
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null)
            this.status = "PENDING";
    }

    public RawCustomerRecordDto toDto() {
        return RawCustomerRecordDto.builder()
                .sourceSystem(this.sourceSystem)
                .sourceRecordId(this.sourceRecordId)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .street(this.street)
                .city(this.city)
                .state(this.state)
                .zipCode(this.zipCode)
                .country(this.country)
                .build();
    }
}
