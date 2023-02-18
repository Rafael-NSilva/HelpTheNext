package com.example.helpthenext.dataclasses;

import java.io.Serializable;
import java.util.Date;

public class HomeRequest implements Serializable {
    private String userID;
    private String homeInfo;
    private int homeSize;
    private Date sentDate;
    private double latitude;
    private double longitude;
    private String otherDetails;

    public HomeRequest() {}

    public HomeRequest(String userID, String homeInfo, int homeSize, Date sentDate, double latitude, double longitude, String otherDetails) {
        this.userID = userID;
        this.homeInfo = homeInfo;
        this.homeSize = homeSize;
        this.sentDate = sentDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.otherDetails = otherDetails;
    }

    public String getUserID() {
        return userID;
    }

    public String getHomeInfo() {
        return homeInfo;
    }

    public int getHomeSize() {
        return homeSize;
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