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
import org.springframework.lang.Nullable;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "BOOKING")
@IdClass(BookingPK.class)
public class Booking {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_EMAIL", foreignKey = @ForeignKey(name = "BOOKING_USER_fk"))
    @View.SingleVale(name = "felhasznalo", attributeExtractor = "email", listValueClass = User.class, control = ComboBox.class)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FLIGHT_ID", referencedColumnName = "FLIGHT_ID",
            foreignKey = @ForeignKey(name = "BOOKING_SCHEDULE_fk"))
    @JoinColumn(name = "PLANE_ID", referencedColumnName = "PLANE_ID",
            foreignKey = @ForeignKey(name = "BOOKING_SCHEDULE_fk"))
    @JoinColumn(name = "DEPARTURE_TIME", referencedColumnName = "DEPARTURE_TIME",
            foreignKey = @ForeignKey(name = "BOOKING_SCHEDULE_fk"))

    @View.SingleVale(name = "járat", attributeExtractor = "shortText", listValueClass = Schedule.class, control = ComboBox.class)
    private Schedule schedule;

    @Id
    @Column(name = "SEAT_NUMBER")
    @View.SingleVale(control = TextField.class, name = "szék szám", stringConverter = IntegerStringConverter.class)
    private int seatNumber;

    @Nullable
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INSURANCE_PACKAGE", foreignKey = @ForeignKey(name = "BOOKING_SCHEDULE_fk"))
    @JoinColumn(name = "INSURANCE_COMPANY", foreignKey = @ForeignKey(name = "BOOKING_SCHEDULE_fk"))
    @View.SingleVale(name = "biztositás", attributeExtractor = "shortName", listValueClass = InsurancePackage.class, control = ComboBox.class)
    private InsurancePackage insurancePackage;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return getSeatNumber() == booking.getSeatNumber() && Objects.equals(getUser(), booking.getUser()) && Objects.equals(getSchedule(), booking.getSchedule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getSchedule(), getSeatNumber());
    }
}
