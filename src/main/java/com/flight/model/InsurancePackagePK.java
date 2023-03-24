package com.flight.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
public class InsurancePackagePK implements Serializable {
    @Id
    @Column(name = "NAME")
    private String name;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INSURANCE_COMPANY_NAME", foreignKey = @ForeignKey(name = "INSURANCE_PACKAGE_INSURANCE_COMPANY_fk"))
    private InsuranceCompany insuranceCompany;
}
