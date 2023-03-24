package com.flight.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

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

}
