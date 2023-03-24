package com.flight.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

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
}
