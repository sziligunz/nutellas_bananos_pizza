package com.flight.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
public class ClassClassifierPK implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FLIGHT_ID", referencedColumnName = "FLIGHT_ID",
            foreignKey = @ForeignKey(name = "CLASS_CLASSIFIER_SCHEDULE_fk"))
    @JoinColumn(name = "PLANE_ID", referencedColumnName = "PLANE_ID",
            foreignKey = @ForeignKey(name = "CLASS_CLASSIFIER_SCHEDULE_fk"))
    @JoinColumn(name = "DEPARTURE_TIME", referencedColumnName = "DEPARTURE_TIME",
            foreignKey = @ForeignKey(name = "CLASS_CLASSIFIER_SCHEDULE_fk"))

    private Schedule schedule;

    @Id
    @Column(name = "SEAT_NUMBER")
    private int seatNumber;
}
