package com.flight.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class InsurancePackagePK implements Serializable {
    @Id
    @Column(name = "NAME")
    private String name;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INSURANCE_COMPANY_NAME", foreignKey = @ForeignKey(name = "INSURANCE_PACKAGE_INSURANCE_COMPANY_fk"))
    private InsuranceCompany insuranceCompany;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsurancePackagePK that = (InsurancePackagePK) o;
        return Objects.equals(name, that.name) && Objects.equals(insuranceCompany, that.insuranceCompany);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, insuranceCompany);
    }
}
