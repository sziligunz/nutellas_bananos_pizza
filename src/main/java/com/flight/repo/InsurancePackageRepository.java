package com.flight.repo;

import com.flight.model.InsurancePackage;
import com.flight.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsurancePackageRepository extends CrudRepository<InsurancePackage, String> {
    @Query("SELECT insure FROM InsurancePackage insure WHERE insure.name = :#{#name}")
    Optional<InsurancePackage> getInsurancePackageByName(@Param("name") String name);
}