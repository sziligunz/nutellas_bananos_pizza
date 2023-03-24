package com.flight.repo;

import com.flight.model.Schedule;
import com.flight.model.SchedulePK;
import org.springframework.data.repository.CrudRepository;


public interface ScheduleRepository extends CrudRepository<Schedule, SchedulePK> {
}