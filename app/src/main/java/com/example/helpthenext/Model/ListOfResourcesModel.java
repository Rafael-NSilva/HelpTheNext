package com.example.helpthenext.Model;

public class ListOfResourcesModel {
    String resourceName;
    String quantity;
    String unit;

    public ListOfResourcesModel() {
    }

    public ListOfResourcesModel(String resourceName, String quantity, String unit) {
        this.resourceName = resourceName;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
