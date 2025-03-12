package com.bpifrance.beneficiariesmanagment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminator;
import org.hibernate.annotations.AnyKeyJavaClass;


import java.util.UUID;

@Entity
@Table(name = "ownership")
public class Ownership {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "Company cannot be null")
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @NotNull(message = "Owner cannot be null")
    @Any
    @AnyKeyJavaClass(UUID.class)
    @Column(name = "owner_type")
    @AnyDiscriminator(DiscriminatorType.STRING)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @NotNull(message = "Ownership percentage cannot be null")
    @Min(value = 0, message = "Ownership percentage must be at least 0%")
    @Max(value = 100, message = "Ownership percentage cannot exceed 100%")
    private Float percentage;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }
}