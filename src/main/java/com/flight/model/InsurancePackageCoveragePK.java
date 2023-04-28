package com.flight.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class InsurancePackageCoveragePK implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INSURANCE_COMPANY_NAME", referencedColumnName = "INSURANCE_COMPANY_NAME",
            foreignKey = @ForeignKey(name = "INSURANCE_PACKAGE_COVERAGE_PACKAGE_fk"))
    @JoinColumn(name = "INSURANCE_PACKAGE_NAME", referencedColumnName = "NAME",
            foreignKey = @ForeignKey(name = "INSURANCE_PACKAGE_COVERAGE_PACKAGE_fk"))
    private InsurancePackage insurancePackage;


    @Id
    @Column(name = "DAMAGE_TYPE")
    private String damageType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsurancePackageCoveragePK that = (InsurancePackageCoveragePK) o;
        return Objects.equals(insurancePackage, that.insurancePackage) && Objects.equals(damageType, that.damageType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(insurancePackage, damageType);
    }
}
