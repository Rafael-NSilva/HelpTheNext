package com.example.helpthenext.dataclasses;


import android.graphics.Bitmap;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MissingPerson{
    private String id;
    private String firstName;
    private String lastName;
    private Date missingDate;
    private double latitude;
    private double longitude;
    private String otherDetails;
    private String userImageReference;

    private String creatorName;

    public MissingPerson() {}

    public MissingPerson(String id, String firstName, String lastName, Date missingDate, double latitude, double longitude, String otherDetails, String userImageReference, String creatorName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.missingDate = missingDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.otherDetails = otherDetails;
        this.userImageReference = userImageReference;
        this.creatorName = creatorName;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getMissingDate() {
        return missingDate;
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

    public String getUserImageReference() {
        return userImageReference;
    }

    public String getCreatorName() {
        return creatorName;
    }
  
    public void setMissingDate(Date missingDate) {
        this.missingDate = missingDate;
    }
}
