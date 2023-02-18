package com.example.helpthenext.Model;

public class SearchResourcesModel {
    int userProfilePicture;
    String userName;
    String resourceName;
    public SearchResourcesModel(int userProfilePicture, String userName, String resourceName) {
        this.userProfilePicture = userProfilePicture;
        this.userName = userName;
        this.resourceName = resourceName;
    }

    public int getUserProfilePicture() {
        return userProfilePicture;
    }

    public String getUserName() {
        return userName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
