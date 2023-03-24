package com.flight.repo;

import com.flight.model.InsurancePackage;
import com.flight.model.InsurancePackageCoverage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsurancePackageCoverageRepository extends CrudRepository<InsurancePackageCoverage, InsurancePackage> {
}