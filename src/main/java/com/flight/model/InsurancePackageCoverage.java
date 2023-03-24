package com.flight.model;

import com.flight.view.View;
import jakarta.persistence.*;
import javafx.scene.control.ComboBox;
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
@Table(name = "INSURANCE_PACKAGE_COVERAGE")
@IdClass(InsurancePackageCoveragePK.class)
public class InsurancePackageCoverage {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INSURANCE_COMPANY_NAME", referencedColumnName = "INSURANCE_COMPANY_NAME",
            foreignKey = @ForeignKey(name = "INSURANCE_PACKAGE_COVERAGE_PACKAGE_fk"))
    @JoinColumn(name = "INSURANCE_PACKAGE_NAME", referencedColumnName = "NAME",
            foreignKey = @ForeignKey(name = "INSURANCE_PACKAGE_COVERAGE_PACKAGE_fk"))
    @View.SingleVale(control = ComboBox.class, name = "csomag", listValueClass = InsurancePackage.class, attributeExtractor = "shortName")
    private InsurancePackage insurancePackage;


    @Id
    @Column(name = "DAMAGE_TYPE")
    @View.SingleVale(control = ComboBox.class, name = "kár", list = {"közlekedési", "egészségügyi", "annyagi"})
    private String damageType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsurancePackageCoverage that = (InsurancePackageCoverage) o;
        return Objects.equals(getInsurancePackage(), that.getInsurancePackage()) && Objects.equals(getDamageType(), that.getDamageType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInsurancePackage(), getDamageType());
    }
}
