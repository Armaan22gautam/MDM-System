package com.mdm.ingestion.repository;

import com.mdm.ingestion.model.RawCustomerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RawCustomerRecordRepository extends JpaRepository<RawCustomerRecord, Long> {
    Optional<RawCustomerRecord> findBySourceSystemAndSourceRecordId(String sourceSystem, String sourceRecordId);
}
