package com.example.helpthenext.dataclasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class SupplyRequest implements Serializable {
    private ArrayList<Supply> supplies;
    private Date sentDate;
    private double latitude;
    private double longitude;
    private String otherDetails;
    private String userID;

    public SupplyRequest() {}

    public SupplyRequest(ArrayList<Supply> supplies, Date sentDate, double latitude, double longitude, String otherDetails, String userID) {
        this.supplies = supplies;
        this.sentDate = sentDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.otherDetails = otherDetails;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public ArrayList<Supply> getSupplies() {
        return supplies;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
}
