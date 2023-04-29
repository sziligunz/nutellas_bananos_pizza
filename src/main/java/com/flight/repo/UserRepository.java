package com.flight.repo;

import com.flight.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    @Query("SELECT user FROM User user WHERE user.email = :#{#email}")
    Optional<User> getUserByEmail(@Param("email") String email);


    @Query("SELECT user FROM User user WHERE user.email = :#{#email} and user.hashedPassword = :#{#pw}")
    Optional<User> getUserByEmailAndHashedPassword(@Param("email") String email, @Param("pw") String pw);


    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.privilege = :#{#newRole} WHERE user.email = :#{#email}")
    void updateUserRole(@Param("email") int email, @Param("newRole") String newRole);


    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.hashedPassword = :#{#newPassword} WHERE user.email = :#{#email}")
    void updateUserPassword(@Param("email") int email, @Param("newPassword") String newPassword);

    @Query(value = "Select departure_time, insurance_package, sum(price) as DB from booking inner join insurance_package on insurance_package.name = booking.insurance_package group by departure_time, insurance_package", nativeQuery = true)
    List<Object[]> insurance_packages_bought();

    @Query(value = """
            Select
                departure_time,
                insurance_package,
                count(price) as DB
            from booking
            inner join insurance_package on insurance_package.name = booking.insurance_package
            group by departure_time, insurance_package""", nativeQuery = true)
    List<Object[]> insurance_packages_money_summed();

    @Query(value = """
            Select
                insurance_package,
                count(price) as DB
            from booking
            inner join insurance_package on insurance_package.name = booking.insurance_package
            group by insurance_package""", nativeQuery = true)
    List<Object[]> insurance_packages_bought_all_time();

    @Query(value = """
            Select
                model_name,
                owning_airline
            from plane
            inner join schedule on plane.id = schedule.plane_id
            inner join flight on schedule.flight_id = flight.id
            inner join airport on flight.departure = airport.id
            where airport.vaccine_required = 1
            group by model_name, owning_airline""", nativeQuery = true)
    List<Object[]> airplanes_fly_from_vaccine();

    @Query(value = """
            Select
                model_name,
                schedule.departure_time,
                count(user_email) as DB
            from plane
            inner join schedule on plane.id = schedule.plane_id
            inner join booking on schedule.flight_id = booking.flight_id and schedule.plane_id = booking.plane_id
            group by model_name, schedule.departure_time""", nativeQuery = true)
    List<Object[]> seats_reserved_on_flight();

    @Query(value = """
            Select
                departure_time,
                sum(flight_duration) ALL_DURATION
            from schedule
            inner join flight on schedule.flight_id = flight.id
            group by departure_time""", nativeQuery = true)
    List<Object[]> minutes_of_flight_per_day();
}