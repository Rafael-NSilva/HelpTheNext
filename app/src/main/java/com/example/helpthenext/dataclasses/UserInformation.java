package com.example.helpthenext.dataclasses;



public class UserInformation {
    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
    private String gender;
    private String profileImageRef;

    public UserInformation() {}

    public UserInformation(String firstName, String lastName, String email, String birthDate, String gender, String profileImageRef) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.profileImageRef = profileImageRef;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getProfileImageRef() {
        return profileImageRef;
    }
}
