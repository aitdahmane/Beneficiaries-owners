package com.bpifrance.beneficiariesmanagment.dto;

import java.util.UUID;

public class OwnerDTO {
    private UUID id;
    private String name;
    private String type;
    private Float ownershipPercentage;

    public OwnerDTO(UUID id, String name, String type, Float ownershipPercentage) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ownershipPercentage = ownershipPercentage;
    }

    public OwnerDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getOwnershipPercentage() {
        return ownershipPercentage;
    }

    public void setOwnershipPercentage(Float ownershipPercentage) {
        this.ownershipPercentage = ownershipPercentage;
    }
}