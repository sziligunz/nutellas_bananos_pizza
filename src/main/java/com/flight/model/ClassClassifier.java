package com.flight.model;

import com.flight.view.View;
import jakarta.persistence.*;
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
@Table(name = "CLASS_CLASSIFIER")
@IdClass(ClassClassifierPK.class)
public class ClassClassifier {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FLIGHT_ID", referencedColumnName = "FLIGHT_ID", foreignKey = @ForeignKey(name = "CLASS_CLASSIFIER_SCHEDULE_fk"))
    @JoinColumn(name = "PLANE_ID", referencedColumnName = "PLANE_ID", foreignKey = @ForeignKey(name = "CLASS_CLASSIFIER_SCHEDULE_fk"))
    @JoinColumn(name = "DEPARTURE_TIME", referencedColumnName = "DEPARTURE_TIME", foreignKey = @ForeignKey(name = "CLASS_CLASSIFIER_SCHEDULE_fk"))

    @View.SingleVale(name = "járat", attributeExtractor = "shortText", listValueClass = Schedule.class, control = ComboBox.class)
    private Schedule schedule;

    @Id
    @Column(name = "SEAT_NUMBER")
    @View.SingleVale(name = "szék szám", control = TextField.class, stringConverter = IntegerStringConverter.class)
    private int seatNumber;

    @Column(name = "CLASS")
    @View.SingleVale(name = "osztály", list = {"első osztály", "business", "commercial"}, control = ComboBox.class)
    private String clazz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassClassifier that = (ClassClassifier) o;
        return Objects.equals(getSchedule(), that.getSchedule()) && Objects.equals(getSeatNumber(), that.getSeatNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSchedule(), getSeatNumber());
    }
}
