package com.flight.repo;

import com.flight.model.Schedule;
import com.flight.model.SchedulePK;
import com.flight.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface ScheduleRepository extends CrudRepository<Schedule, SchedulePK> {


}