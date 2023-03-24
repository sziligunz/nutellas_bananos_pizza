package com.flight.model;

import com.flight.view.View;
import jakarta.persistence.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.converter.IntegerStringConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "FLIGHT")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTURE", foreignKey = @ForeignKey(name = "FLIGHT_DEPARTURE_fk"))
    @ToString.Exclude
    @View.SingleVale(control = ComboBox.class, name = "Indul", listValueClass = Airport.class, attributeExtractor = "id")
    private Airport departure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARRIVAL", foreignKey = @ForeignKey(name = "FLIGHT_ARRIVAL_fk"))
    @ToString.Exclude
    @View.SingleVale(control = ComboBox.class, name = "Érkezik", listValueClass = Airport.class, attributeExtractor = "id")
    private Airport arrival;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AIRLINE", foreignKey = @ForeignKey(name = "FLIGHT_AIRLINE_fk"))
    @ToString.Exclude
    @View.SingleVale(control = ComboBox.class, name = "Légitársaság", listValueClass = Airline.class, attributeExtractor = "name")
    private Airline airline;

    @Column(name = "MEAL_AVAILABLE")
    @View.SingleVale(name = "étel elérhető?", control = CheckBox.class)
    private boolean mealAvailable;

    @Column(name = "FLIGHT_DURATION")
    @View.SingleVale(name = "út hossza?", control = TextField.class, stringConverter = IntegerStringConverter.class)
    private int flightDuration;

    @Column(name = "CHILD_FRIENDLY")
    @View.SingleVale(name = "gyerek barát?", control = CheckBox.class)
    private boolean childFriendly;

    public String getShortName() {
        return String.format("%s: %s-%s", airline.getName(), departure.getId(), arrival.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return getId() == flight.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
