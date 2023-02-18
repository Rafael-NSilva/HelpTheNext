package com.example.helpthenext;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.helpthenext.dataclasses.DAOHomeRequest;
import com.example.helpthenext.dataclasses.DAOMissingPerson;
import com.example.helpthenext.dataclasses.DAOSupplyRequest;
import com.example.helpthenext.dataclasses.HomeRequest;
import com.example.helpthenext.dataclasses.MissingPerson;
import com.example.helpthenext.dataclasses.SupplyRequest;
import com.example.helpthenext.utils.PermissionUtils;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback{
    private Toolbar appBar;
    private FloatingActionButton fabMain, fabResource, fabShelter, fabPerson;
    private TextView textViewResource, textViewShelter, textViewPerson;
    private Animation fabOpen, fabClose, fabClock, fabAntiClock;
    private Boolean isOpen = false;

    private GoogleMap map;
    private Marker searchMarker = null;
    private Location lastLocation = null;
    private LocationRequest mLocationRequest;

    private DAOMissingPerson missingDAO;
    private DAOHomeRequest homeRequestDAO;
    private DAOSupplyRequest supplyRequestDAO;

    private HashMap<Marker, MissingPerson> personMarkers;
    private HashMap<Marker, HomeRequest> homeRequestMarkers;
    private HashMap<Marker, SupplyRequest> supplyRequestMarkers;

    private boolean locationPermission;


    private final long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private final long FASTEST_INTERVAL = 2000; /* 2 sec */

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;
        locationPermission = PermissionUtils.checkPermissions(this);
        if (locationPermission){
            startLocationUpdates();
            map.setMyLocationEnabled(true);
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.location_permission_denied), Toast.LENGTH_LONG);
        }
        loadMapMarkers();

        //Quando utilizador carrega num marker, passa a informação do objeto obtido no inicio por intent
        map.setOnMarkerClickListener(marker -> {
            if(personMarkers.containsKey(marker)){
                Intent missingPersonView = new Intent(this, ViewMissingPersonRequestActivity.class);
                missingPersonView.putExtra("missingPersonID", personMarkers.get(marker).getId());
                startActivity(missingPersonView);
            }
            else if(homeRequestMarkers.containsKey(marker)){
                Intent homeRequestView = new Intent(this, ViewAccommodationRequest.class);
                homeRequestView.putExtra("homeRequestDetails", homeRequestMarkers.get(marker));
                startActivity(homeRequestView);
            }
            else if(supplyRequestMarkers.containsKey(marker)){
                Intent supplyRequestView = new Intent(this, ViewSupplyRequest.class);
                //supplyRequestView.putExtra("supplyRequestDetails", supplyRequestMarkers.get(marker));
                startActivity(supplyRequestView);
            }
            return true;
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(appBar);
        setContentView(R.layout.map);

        missingDAO = new DAOMissingPerson();
        homeRequestDAO = new DAOHomeRequest();
        supplyRequestDAO = new DAOSupplyRequest();
        personMarkers = new HashMap<>();
        homeRequestMarkers = new HashMap<>();
        supplyRequestMarkers = new HashMap<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        appBar = findViewById(R.id.toolbar);
        fabMain = findViewById(R.id.addRequestFab);
        fabResource = findViewById(R.id.resourceRequest);
        fabShelter = findViewById(R.id.shelterRequest);
        fabPerson = findViewById(R.id.missingPersonRequest);
        textViewResource = findViewById(R.id.textview_resourceRequest);
        textViewShelter = findViewById(R.id.textview_shelterRequest);
        textViewPerson = findViewById(R.id.textview_missingPersonRequest);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fabAntiClock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        PopupMenu popupMenu = new PopupMenu(this, appBar);
        popupMenu.getMenuInflater().inflate(R.menu.map_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item ->  {
            if(item.getItemId() == R.id.profile_option){
                startActivity(new Intent(MapActivity.this, UserProfileActivity.class));
            }
            else if(item.getItemId() == R.id.notifications_option){
                startActivity(new Intent(this, NotificationsActivity.class));
            }
            else if(item.getItemId() == R.id.donatingResources_option){
                Intent donatingResources = new Intent(this, ResourcesDonationActivity.class);
                donatingResources.putExtra("userLocation", new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                startActivity(donatingResources);
            }
            else if(item.getItemId() == R.id.requests_option){
                if(locationPermission){
                    Intent resourceRequest = new Intent(this, ListOfResourcesActivity.class);
                    resourceRequest.putExtra("userLocation", new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                    startActivity(resourceRequest);
                } else {
                    locationPermission = PermissionUtils.checkPermissions(this);
                }
            }
            else if(item.getItemId() == R.id.settings_option){
                startActivity(new Intent(this, SettingsActivity.class));
            }
            else if(item.getItemId() == R.id.logOut_option){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),MenuAutenticacao.class));
                    finish();
            }
            return true;
        });
        popupMenu.setForceShowIcon(true);

        appBar.setNavigationOnClickListener(view->{
            popupMenu.show();
        });

        findViewById(R.id.documents).setOnClickListener(view -> {
            startActivity(new Intent(MapActivity.this, DocumentsActivity.class));
        });

        findViewById(R.id.filter).setOnClickListener(view -> {
            startActivity(new Intent(MapActivity.this, SearchForResourcesActivity.class));
        });

        fabMain.setOnClickListener(view -> {
                if (isOpen) {
                    textViewResource.setVisibility(View.INVISIBLE);
                    textViewShelter.setVisibility(View.INVISIBLE);
                    textViewPerson.setVisibility(View.INVISIBLE);
                    fabPerson.startAnimation(fabClose);
                    fabShelter.startAnimation(fabClose);
                    fabResource.startAnimation(fabClose);
                    fabMain.startAnimation(fabAntiClock);
                    isOpen = false;
                } else {
                    textViewResource.setVisibility(View.VISIBLE);
                    textViewShelter.setVisibility(View.VISIBLE);
                    textViewPerson.setVisibility(View.VISIBLE);
                    fabResource.startAnimation(fabOpen);
                    fabShelter.startAnimation(fabOpen);
                    fabPerson.startAnimation(fabOpen);
                    fabMain.startAnimation(fabClock);
                    isOpen = true;
                }
        });

        fabResource.setOnClickListener(view -> {
            if(locationPermission){
                Intent resourceRequest = new Intent(this, ListOfResourcesActivity.class);
                resourceRequest.putExtra("userLocation", new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                startActivity(resourceRequest);
            } else {
                locationPermission = PermissionUtils.checkPermissions(this);
            }
        });
        fabShelter.setOnClickListener(view -> {
            if(locationPermission){
                Intent shelterRequest = new Intent(this, AccommodationRequestActivity.class);
                shelterRequest.putExtra("userLocation", new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                startActivity(shelterRequest);
            } else {
                locationPermission = PermissionUtils.checkPermissions(this);
            }
        });
        fabPerson.setOnClickListener(view -> {
            if(locationPermission){
                Intent missingRequest = new Intent(this, MissingPersonRequestActivity.class);
                missingRequest.putExtra("userLocation", new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                startActivity(missingRequest);
            } else {
                locationPermission = PermissionUtils.checkPermissions(this);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMapMarkers();
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("search")){
            if(searchMarker != null){
                searchMarker.remove();
            }
            geoLocate(extras.getString("search"));
            getIntent().removeExtra("search");
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }

    private Marker addMarkerToMap(String type, double longitude, double latitude){
        BitmapDescriptor iconBitmap = null;
        final LatLng markerLocation = new LatLng(latitude, longitude);
        if(type != null){
            if(type.equals("resource")){
                iconBitmap = getMarkerIconFromDrawableResource(this, R.drawable.ic_resourceicon);
            } else if(type.equals("shelter")){
                iconBitmap = getMarkerIconFromDrawableResource(this, R.drawable.ic_houseicon);
            } else if(type.equals("missing")) {
                iconBitmap = getMarkerIconFromDrawableResource(this, R.drawable.ic_missingpersonicon);
            }
        }

        if(iconBitmap == null){
            return map.addMarker(
                    new MarkerOptions()
                            .position(markerLocation));
        }
        else{
            return map.addMarker(
                    new MarkerOptions()
                            .position(markerLocation)
                            .icon(iconBitmap));
        }
    }

    private BitmapDescriptor getMarkerIconFromDrawableResource(Context context, int resourceId) {
        Drawable drawable = AppCompatResources.getDrawable(context, resourceId);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @SuppressLint("MissingPermission")
    private void geoLocate(String searchString){
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 5,
                    lastLocation.getLatitude()-2,
                    lastLocation.getLongitude()-2,
                    lastLocation.getLatitude()+2,
                    lastLocation.getLongitude()+2);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            for(Address i : list){
                searchMarker = addMarkerToMap(null, i.getLongitude(), i.getLatitude());
            }
        }
    }

    public void onLocationChanged(Location location) {

        if(lastLocation == null && location != null){
            lastLocation = location;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 16f));
        } else{
            lastLocation = location;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            startLocationUpdates();
            map.setMyLocationEnabled(true);
        }
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

    public void loadMapMarkers(){
        //Buscar os dados a base de dados
        if(missingDAO.getReference() != null){
            missingDAO.getReference().get().addOnSuccessListener(dataSnapshot -> {
                for(DataSnapshot dataChild : dataSnapshot.getChildren()){
                    MissingPerson person = dataChild.getValue(MissingPerson.class);
                    Date missingTime = dataChild.child("missingDate").getValue(Date.class);
                    person.setMissingDate(missingTime);

                    Marker marker = addMarkerToMap("missing",
                            person.getLongitude(),
                            person.getLatitude());
                    personMarkers.put(marker,person);
                }
            });
        }
        if(homeRequestDAO.getReference() != null){
            homeRequestDAO.getReference().get().addOnSuccessListener(dataSnapshot -> {
                for(DataSnapshot dataChild : dataSnapshot.getChildren()){
                    HomeRequest homeRequest = dataChild.getValue(HomeRequest.class);
                    Date sentTime = dataChild.child("sentDate").getValue(Date.class);
                    homeRequest.setSentDate(sentTime);

                    Marker marker = addMarkerToMap("shelter",
                            homeRequest.getLongitude(),
                            homeRequest.getLatitude());
                    homeRequestMarkers.put(marker,homeRequest);
                }
            });
        }
        if(supplyRequestDAO.getReference() != null){
            supplyRequestDAO.getReference().get().addOnSuccessListener(dataSnapshot -> {
                for(DataSnapshot dataChild : dataSnapshot.getChildren()){
                    SupplyRequest supplyRequest = dataChild.getValue(SupplyRequest.class);
                    Date sentTime = dataChild.child("sentDate").getValue(Date.class);
                    supplyRequest.setSentDate(sentTime);

                    Marker marker = addMarkerToMap("resource",
                            supplyRequest.getLongitude(),
                            supplyRequest.getLatitude());
                    supplyRequestMarkers.put(marker,supplyRequest);
                }
            });
        }
    }
}