package com.example.helpthenext.dataclasses;

public class Supply {
    private String resourceName;
    private double resourceQuantity;
    private String quantityMeasurement;

    public Supply(String resourceName, double resourceQuantity, String quantityMeasurement) {
        this.resourceName = resourceName;
        this.resourceQuantity = resourceQuantity;
        this.quantityMeasurement = quantityMeasurement;
    }

    public Supply() {
    }

    public String getResourceName() {
        return resourceName;
    }

    public double getResourceQuantity() {
        return resourceQuantity;
    }

    public String getQuantityMeasurement() {
        return quantityMeasurement;
    }
}
