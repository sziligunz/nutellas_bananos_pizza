package com.flight.repo;

import com.flight.model.ClassClassifier;
import com.flight.model.Schedule;
import com.flight.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassClassifierRepository extends CrudRepository<ClassClassifier, Schedule> {

    @Query("SELECT classy FROM ClassClassifier classy WHERE classy.schedule = :#{#schedule}")
    List<ClassClassifier> getClassClassifierBySchedule(@Param("schedule") Schedule schedule);

}