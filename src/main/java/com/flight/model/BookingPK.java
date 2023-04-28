package com.flight.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class BookingPK implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_EMAIL", foreignKey = @ForeignKey(name = "BOOKING_USER_fk"))
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FLIGHT_ID", referencedColumnName = "FLIGHT_ID",
            foreignKey = @ForeignKey(name = "BOOKING_SCHEDULE_fk"))
    @JoinColumn(name = "PLANE_ID", referencedColumnName = "PLANE_ID",
            foreignKey = @ForeignKey(name = "BOOKING_SCHEDULE_fk"))
    @JoinColumn(name = "DEPARTURE_TIME", referencedColumnName = "DEPARTURE_TIME",
            foreignKey = @ForeignKey(name = "BOOKING_SCHEDULE_fk"))
    private Schedule schedule;

    @Id
    @Column(name = "SEAT_NUMBER")
    private int seatNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingPK bookingPK = (BookingPK) o;
        return seatNumber == bookingPK.seatNumber && Objects.equals(user, bookingPK.user) && Objects.equals(schedule, bookingPK.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, schedule, seatNumber);
    }
}



