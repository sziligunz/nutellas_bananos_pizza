package com.flight.repo;

import com.flight.model.ClassClassifier;
import com.flight.model.Schedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassClassifierRepository extends CrudRepository<ClassClassifier, Schedule> {
}