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
@Table(name = "INSURANCE_PACKAGE")
@IdClass(InsurancePackagePK.class)
public class InsurancePackage {
    @Id
    @Column(name = "NAME")
    @View.SingleVale(name = "csomag neve", control = TextField.class)
    private String name;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INSURANCE_COMPANY_NAME", foreignKey = @ForeignKey(name = "INSURANCE_PACKAGE_INSURANCE_COMPANY_fk"))
    @View.SingleVale(control = ComboBox.class, name = "biztositó", listValueClass = InsuranceCompany.class, attributeExtractor = "name")
    private InsuranceCompany insuranceCompany;

    @Column(name = "PRICE")
    @View.SingleVale(control = TextField.class, name = "ár", stringConverter = IntegerStringConverter.class)
    private int price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsurancePackage that = (InsurancePackage) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getInsuranceCompany(), that.getInsuranceCompany());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getInsuranceCompany());
    }


    public String getShortName() {
        return String.format("%s: %s", insuranceCompany.getName(), name);
    }
}
