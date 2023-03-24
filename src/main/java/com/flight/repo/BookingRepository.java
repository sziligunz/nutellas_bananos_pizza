package com.flight.repo;

import com.flight.model.Booking;
import com.flight.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends CrudRepository<Booking, User> {
}