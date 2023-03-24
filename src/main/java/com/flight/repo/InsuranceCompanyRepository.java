package com.flight.repo;

import com.flight.model.InsuranceCompany;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceCompanyRepository extends CrudRepository<InsuranceCompany, String> {
}