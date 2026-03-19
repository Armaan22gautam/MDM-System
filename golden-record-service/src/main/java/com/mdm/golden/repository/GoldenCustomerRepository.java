package com.mdm.golden.repository;

import com.mdm.golden.model.GoldenCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoldenCustomerRepository extends JpaRepository<GoldenCustomer, Long> {

    @Query("SELECT g FROM GoldenCustomer g WHERE " +
            "(:email IS NOT NULL AND g.email = :email) OR " +
            "(:phone IS NOT NULL AND g.phoneNumber = :phone) OR " +
            "(:firstName IS NOT NULL AND :lastName IS NOT NULL AND g.firstName = :firstName AND g.lastName = :lastName)")
    List<GoldenCustomer> findPossibleMatches(
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName);
}
