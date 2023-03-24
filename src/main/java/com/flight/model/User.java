package com.flight.model;

import com.flight.view.View;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
@Table(name = "\"USER\"")
public class User {
    @Column(name = "NAME")
    @View.SingleVale(name = "név", control = TextField.class)
    private String name;

    @Id
    @Column(name = "EMAIL")
    @View.SingleVale(name = "email", control = TextField.class)
    private String email;

    @Column(name = "HASHED_PASSWORD")
    @View.SingleVale(name = "hashelt jelszó", control = TextField.class)
    private String hashedPassword;

    @Column(name = "PRIVILEGE")
    @View.SingleVale(control = ComboBox.class, name = "szint", list = {"olvasó", "mendezser", "admin"})
    private String privilege;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}
