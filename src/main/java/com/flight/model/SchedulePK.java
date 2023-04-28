package com.flight.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Data
public class SchedulePK implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FLIGHT_ID", foreignKey = @ForeignKey(name = "SCHEDULE_FLIGHT_fk"))
//    @Column(name = "FLIGHT_ID")
    private Flight flight;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLANE_ID", foreignKey = @ForeignKey(name = "SCHEDULE_PLANE_fk"))
//    @Column(name = "PLANE_ID")
    private Plane plane;

    @Id
    @Column(name = "DEPARTURE_TIME")
    private Date departureTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulePK that = (SchedulePK) o;
        return Objects.equals(flight, that.flight) && Objects.equals(plane, that.plane) && Objects.equals(departureTime, that.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flight, plane, departureTime);
    }
}
