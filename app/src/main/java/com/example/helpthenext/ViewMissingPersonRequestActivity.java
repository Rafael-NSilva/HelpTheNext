package com.example.helpthenext;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.helpthenext.dataclasses.DAOMissingPerson;
import com.example.helpthenext.dataclasses.DAOUserInfo;
import com.example.helpthenext.dataclasses.MissingPerson;
import com.example.helpthenext.dataclasses.MissingPersonCallback;
import com.example.helpthenext.dataclasses.UserInfoCallback;
import com.example.helpthenext.dataclasses.UserInformation;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewMissingPersonRequestActivity extends AppCompatActivity implements MissingPersonCallback, UserInfoCallback {

    private Toolbar toolbar;
    private TextView name, otherDetails, personSearching, locationName, daysMissing;
    private Button btnKnowLocation;
    private ImageView missingPersonImage;
    private DAOMissingPerson daoMissingPerson;
    private DAOUserInfo daoUserInfo;
    private String missingPersonID;
    private String creatorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_missing_person_request);

        toolbar = findViewById(R.id.toolbarMissingPersonView);
        toolbar.setNavigationOnClickListener(view -> {
            returnToMap();
        });

        daoUserInfo = new DAOUserInfo();
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("missingPersonID")){
            missingPersonID = extras.get("missingPersonID").toString();
            getViews();
            // Variável Teste - missingPersonID = "e417c214-5262-492e-a2c1-da8818804da9";
            daoMissingPerson = new DAOMissingPerson();
            daoMissingPerson.read(missingPersonID, this);
            addListeners();
        } else {
            returnToMap();
        }
    }

    public void getViews() {
        name = findViewById(R.id.txtMissingPersonName);
        otherDetails = findViewById(R.id.txtMissingPersonOtherDetails);
        personSearching = findViewById(R.id.txtUserSearchingForMissingPerson);
        locationName = findViewById(R.id.txtMissingPersonLocation);
        daysMissing = findViewById(R.id.txtLastSeenValue);
        missingPersonImage = findViewById(R.id.imgMissingPerson);
        btnKnowLocation = findViewById(R.id.btnFoundMissingPerson);
    }

    @Override
    public void loadInfo(MissingPerson missingPerson) {
        name.setText(missingPerson.getFirstName() + " " + missingPerson.getLastName());
        otherDetails.setText(missingPerson.getOtherDetails());
        creatorID = missingPerson.getCreatorName();
        daoUserInfo.readUser(missingPerson.getCreatorName(), this);
        getLocationName(missingPerson.getLatitude(), missingPerson.getLongitude());
        daoMissingPerson.getImage(missingPerson.getUserImageReference(), this);
        calculateNumberOfDays(missingPerson.getMissingDate());
    }

    @Override
    public void loadImage(Uri userImage) {
        Glide.with(this).load(userImage).into(missingPersonImage);
    }

    private void getLocationName(double lat, double lon) {
        Geocoder geocoder = new Geocoder(ViewMissingPersonRequestActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            Address missingLocation = addresses.get(0);
            System.out.println(missingLocation.toString());
            locationName.setText(missingLocation.getLocality());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateNumberOfDays(Date missingDate) {
        Date currentDate = new Date();
        long difference = Math.abs(currentDate.getTime() - missingDate.getTime());
        long differenceDates = difference / (24 * 60 * 60 * 1000); // Em Dias
        String dayDifference = Long.toString(differenceDates);
        daysMissing.setText(dayDifference);
    }

    private void addListeners() {
        btnKnowLocation.setOnClickListener(event -> {
            Intent intent = new Intent(ViewMissingPersonRequestActivity.this, LostPersonMessage.class);
            intent.putExtra("notificationType","MISSING_PERSON");
            intent.putExtra("senderUserID", creatorID.toString());
            startActivity(intent);
        });
    }

    private void returnToMap() {
        startActivity(new Intent(ViewMissingPersonRequestActivity.this, MapActivity.class));
    }

    @Override
    public void loadUserInfo(UserInformation userInformation) {
        personSearching.setText(userInformation.getFirstName() + " " + userInformation.getLastName());
    }

    @Override
    public void loadUsersInfo(List<UserInformation> userInformationList) {

    }
}