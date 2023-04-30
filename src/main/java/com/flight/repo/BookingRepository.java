package com.flight.repo;

import com.flight.model.Booking;
import com.flight.model.ClassClassifier;
import com.flight.model.Schedule;
import com.flight.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, User> {
    @Query("SELECT booking FROM Booking booking WHERE booking.schedule = :#{#schedule} AND booking.user = :#{#user}")
    List<Booking> getBookingsByScheduleAndUser(@Param("schedule") Schedule schedule, @Param("user") User user);


    @Query("SELECT booking FROM Booking booking WHERE booking.user = :#{#user}")
    List<Booking> getBookingsByUser(@Param("user") User user);

}