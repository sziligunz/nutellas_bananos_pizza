package com.flight.model;

import com.flight.view.View;
import jakarta.persistence.*;
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
@Table(name = "HOTEL")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
//    @View.SingleVale(name = "id", control = TextField.class, stringConverter = IntegerStringConverter.class)
    private int id;

    @Column(name = "COUNTRY")
    @View.SingleVale(name = "ország", control = TextField.class)
    private String country;

    @Column(name = "CITY")
    @View.SingleVale(name = "város", control = TextField.class)
    private String city;

    @Column(name = "STAR_RATING")
    @View.SingleVale(name = "csillagok", control = TextField.class, stringConverter = IntegerStringConverter.class)
    private int stars;

    @Column(name = "URL")
    @View.SingleVale(name = "url", control = TextField.class)
    private String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return id == hotel.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
