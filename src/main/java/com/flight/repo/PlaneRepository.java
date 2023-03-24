package com.flight.repo;

import com.flight.model.Plane;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaneRepository extends CrudRepository<Plane, Integer> {
}
