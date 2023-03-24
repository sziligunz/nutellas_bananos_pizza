package com.flight.model;

import com.flight.view.View;
import jakarta.persistence.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.converter.IntegerStringConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "SCHEDULE")
@IdClass(SchedulePK.class)
public class Schedule {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FLIGHT_ID", foreignKey = @ForeignKey(name = "SCHEDULE_FLIGHT_fk"))
    @ToString.Exclude
    @View.SingleVale(control = ComboBox.class, name = "Járat", listValueClass = Flight.class, attributeExtractor = "shortName")
    private Flight flight;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLANE_ID", foreignKey = @ForeignKey(name = "SCHEDULE_PLANE_fk"))
    @ToString.Exclude
    @View.SingleVale(control = ComboBox.class, name = "Repülő", listValueClass = Plane.class, attributeExtractor = "id")
    private Plane plane;

    @Id
    @Column(name = "DEPARTURE_TIME")
    @View.SingleVale(control = DatePicker.class, name = "Indulás")
    private Date departureTime;

    @Column(name = "FIRST_CLASS_PRICE")
    @View.SingleVale(control = TextField.class, name = "első osztály ár", stringConverter = IntegerStringConverter.class)
    private int firstClassPrice;

    @Column(name = "COMMERCIAL_PRICE")
    @View.SingleVale(control = TextField.class, name = "commercial osztály ár", stringConverter = IntegerStringConverter.class)
    private int commercialPrice;

    @Column(name = "BUSINESS_PRICE")
    @View.SingleVale(control = TextField.class, name = "business osztály ár", stringConverter = IntegerStringConverter.class)
    private int businessPrice;


    public String getShortText() {
        return String.format("%s; %s @ %s", flight.getShortName(), plane.getId(), departureTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(getFlight(), schedule.getFlight()) && Objects.equals(getPlane(), schedule.getPlane()) && Objects.equals(getDepartureTime(), schedule.getDepartureTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFlight(), getPlane(), getDepartureTime());
    }
}
