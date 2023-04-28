package com.flight.model;

import com.flight.view.View;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "AIRLINE")
public class Airline {
    @Id
    @Column(name = "NAME")
    @View.SingleVale(name = "név", control = TextField.class)
    private String name;

    @Column(name = "PRICE_CATEGORY")
    @View.SingleVale(name = "ár kategória", control = TextField.class, stringConverter = IntegerStringConverter.class)
    private int priceCategory;

    @Column(name = "STAR_RATING")
    @View.SingleVale(name = "csillagok", control = TextField.class, stringConverter = DoubleStringConverter.class)
    private double starRating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airline airline = (Airline) o;
        return Objects.equals(name, airline.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
