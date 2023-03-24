package com.flight.model;

import com.flight.view.View;
import jakarta.persistence.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.util.converter.IntegerStringConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "AIRPORT")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "COUNTRY")
    @View.SingleVale(name = "ország", control = TextField.class)
    private String country;

    @Column(name = "CITY")
    @View.SingleVale(name = "város", control = TextField.class)
    private String city;

    @Column(name = "CAPACITY")
    @View.SingleVale(name = "kapacitás", control = TextField.class, stringConverter = IntegerStringConverter.class)
    private int capacity;

    @Column(name = "VACCINE_REQUIRED")
    @View.SingleVale(name = "kell-e oltás?", control = CheckBox.class)
    private boolean vaccineRequired;

    @Column(name = "URL")
    @View.SingleVale(name = "url", control = TextField.class)
    private String url;
}
