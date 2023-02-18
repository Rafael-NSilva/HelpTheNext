package com.example.helpthenext.dataclasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class SupplyOffer implements Serializable {
    private String userId;
    private String supplies;
    private Date sentDate;
    private double latitude;
    private double longitude;
    private double quantity;
    private String units;

    public SupplyOffer() {}

    public SupplyOffer(String userId,String supplies, double quantity, String units, Date sentDate, double latitude, double longitude) {
        this.userId = userId;
        this.supplies = supplies;
        this.quantity = quantity;
        this.units = units;
        this.sentDate = sentDate;
        this.latitude = latitude;
        this.longitude = longitude;

    }
    public String getUserId(){return userId;}

    public String getSupplies() {
        return supplies;
    }

    public Double getQuantity(){return quantity;}

    public String getUnits(){return units;}

    public Date getSentDate() {
        return sentDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    @Override
    public String toString() {
        return "SupplyOffer{" +
                "userId='" + userId + '\'' +
                ", supplies='" + supplies + '\'' +
                ", sentDate=" + sentDate +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", quantity=" + quantity +
                ", units='" + units + '\'' +
                '}';
    }
}
