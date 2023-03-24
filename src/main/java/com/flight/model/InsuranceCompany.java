package com.flight.model;

import com.flight.view.View;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "INSURANCE_COMPANY")
public class InsuranceCompany {
    @Id
    @Column(name = "NAME")
    @View.SingleVale(name = "name", control = TextField.class)
    private String name;

    @Column(name = "URL")
    @View.SingleVale(name = "url", control = TextField.class)
    private String url;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsuranceCompany that = (InsuranceCompany) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
