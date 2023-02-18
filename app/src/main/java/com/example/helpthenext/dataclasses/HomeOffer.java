package com.example.helpthenext.dataclasses;

import java.io.Serializable;
import java.util.Date;

public class HomeOffer implements Serializable {
    private String userId;
    private String homeInfo;
    private double homeSize;
    private Date sentDate;
    private double latitude;
    private double longitude;


    public HomeOffer() {}

    public HomeOffer(String userId,String homeInfo, double homeSize, Date sentDate, double latitude, double longitude) {
        this.userId = userId;
        this.homeInfo = homeInfo;
        this.homeSize = homeSize;
        this.sentDate = sentDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserId(){return userId;}

    public String getHomeInfo() {
        return homeInfo;
    }

    public double getHomeSize() {
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

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
}
