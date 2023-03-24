package com.flight.repo;

import com.flight.model.InsurancePackage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsurancePackageRepository extends CrudRepository<InsurancePackage, String> {
}