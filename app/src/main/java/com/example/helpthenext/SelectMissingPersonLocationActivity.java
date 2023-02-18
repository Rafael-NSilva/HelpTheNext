package com.example.helpthenext;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SelectMissingPersonLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Location lastLocation;
    private LocationRequest mLocationRequest;
    private Marker seenMarker;
    private Button btnSubmit;

    private final long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private final long FASTEST_INTERVAL = 2000; /* 2 sec */

    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;
        startLocationUpdates();

        map.setOnMapClickListener(listener -> {
            LatLng latLng = new LatLng(listener.latitude, listener.longitude);
            if (seenMarker != null) {
                seenMarker.remove();
            }
            seenMarker = map.addMarker(new MarkerOptions().position(latLng));
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("markerLocation")) {
            if(extras.getParcelable("markerLocation") != null) {
                LatLng seenMarkerLocation = extras.getParcelable("markerLocation");
                seenMarker = map.addMarker(new MarkerOptions().position(seenMarkerLocation));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(seenMarkerLocation, 14f));
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_missing_person_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapSelectMissingPerson);
        mapFragment.getMapAsync(this);

        btnSubmit = findViewById(R.id.btnSubmitMissingLocation);
        btnSubmit.setOnClickListener(event -> {
            if(seenMarker != null) {
                Intent intent = new Intent(SelectMissingPersonLocationActivity.this, MissingPersonRequestActivity.class);
                intent.putExtras(getIntent().getExtras());
                intent.putExtra("markerLocation", seenMarker.getPosition());
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.toast_no_map_location, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        lastLocation = location;
    }

}