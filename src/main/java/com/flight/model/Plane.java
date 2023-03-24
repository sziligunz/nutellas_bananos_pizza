package com.flight.model;

import com.flight.view.View;
import jakarta.persistence.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.converter.DoubleStringConverter;
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
@Table(name = "PLANE")
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "MAKE")
    @View.SingleVale(name = "gyártó", control = TextField.class)
    private String make;

    @Column(name = "FIRST_CLASS_CAPACITY")
    @View.SingleVale(name = "első osztály helyek", control = TextField.class, stringConverter = IntegerStringConverter.class)
    private int firstClassCapacity;

    @Column(name = "COMMERCIAL_CAPACITY")
    @View.SingleVale(name = "commercial osztály helyek", control = TextField.class, stringConverter = IntegerStringConverter.class)
    private int commercialCapacity;

    @Column(name = "BUSINESS_CAPACITY")
    @View.SingleVale(name = "business osztály helyek", control = TextField.class, stringConverter = IntegerStringConverter.class)
    private int businessCapacity;

    @Column(name = "MODEL_NAME")
    @View.SingleVale(name = "model", control = TextField.class)
    private String modelName;

    @Column(name = "FUEL_CONSUMPTION")
    @View.SingleVale(name = "fogyasztás", control = TextField.class, stringConverter = DoubleStringConverter.class)
    private double fuelConsumption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNING_AIRLINE", foreignKey = @ForeignKey(name = "PLANE_AIRLINE_fk"))
    @ToString.Exclude
    @View.SingleVale(control = ComboBox.class, name = "Tulaj", listValueClass = Airline.class, attributeExtractor = "name")
    private Airline owningAirline;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plane plane = (Plane) o;
        return getId() == plane.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}