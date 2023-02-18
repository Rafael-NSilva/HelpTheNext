package com.example.helpthenext;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.helpthenext.dataclasses.DAOMissingPerson;
import com.example.helpthenext.dataclasses.MissingPerson;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class MissingPersonRequestActivity extends AppCompatActivity implements OnMapReadyCallback {

    // XML
    private Toolbar toolbar;
    private EditText firstName, lastName, otherDetails;
    private TextView missingDateDisplay;
    private ImageView personImage;
    private Button btnSubmit;

    // Imagem e Data
    private Uri personImageUri;
    private boolean hasChoosenImage = false;
    private ActivityResultLauncher<Intent> imagePickerResultLauncher;
    private DatePickerDialog.OnDateSetListener missingDateSetListener;
    private Date missingDate;

    // Mapa
    private LatLng markerLocation;
    private GoogleMap map;


    private DAOMissingPerson dao;




    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;

        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);

        Bundle extras = getIntent().getExtras();
        LatLng location = extras.getParcelable("userLocation");
        if(location != null){
            markerLocation = location;
        }

        if(markerLocation != null) {
            map.addMarker(new MarkerOptions().position(markerLocation));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 14f));
        }

        map.setOnMapClickListener(event -> {
            Intent intent = new Intent(MissingPersonRequestActivity.this, SelectMissingPersonLocationActivity.class);
            intent.putExtra("firstName", firstName.getText().toString());
            intent.putExtra("lastName", lastName.getText().toString());
            intent.putExtra("otherDetails", otherDetails.getText().toString());
            intent.putExtra("date", missingDate);
            intent.putExtra("missingImage", personImageUri);
            intent.putExtra("markerLocation", markerLocation);
            startActivity(intent);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_person_request);

        // Mapa Estático
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.missingPersonMapLocation);
        mapFragment.getMapAsync(this);

        // Toolbar Ações
        toolbar = findViewById(R.id.toolbarMissingPersonRequest);
        toolbar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(MissingPersonRequestActivity.this, MapActivity.class));
        });

       imagePickerResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if(data != null) {
                                personImageUri = data.getData();
                                hasChoosenImage = true;
                                personImage.setImageURI(personImageUri);
                            }
                        }
                    }
                });


        dao = new DAOMissingPerson();

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        missingDateDisplay = (TextView) findViewById(R.id.txtMissingPersonDateRequest);
        missingDateDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MissingPersonRequestActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        missingDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        missingDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String pattern = "dd-MM-yyyy";
                missingDate = new GregorianCalendar(year, month, day).getTime();
                Date currentDate = new Date();
                if(missingDate.before(currentDate)) {
                    String missingDateText = day + "-" + (month + 1) + "-" + year;
                    missingDateDisplay.setText(missingDateText);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(MissingPersonRequestActivity.this).create();
                    alertDialog.setTitle(getString(R.string.alert_title_wrong_date));
                    alertDialog.setMessage(getString(R.string.alert_desc_wrong_date_after));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.alert_accept),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    alertDialog.show();
                }


            }
        };

        getButtonsAndTexts();
        addEventsToButtons();


        // Ter informação de ecrãs anteriores
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("firstName") && extras.containsKey("lastName")
            && extras.containsKey("otherDetails") && extras.containsKey("date") && extras.containsKey("missingImage")
            && extras.containsKey("markerLocation")){
            getIntent().removeExtra("search");
            String firstNameString = extras.get("firstName").toString();
            if(!firstNameString.isEmpty()) {
                firstName.setText(firstNameString);
            }
            String lastNameString = extras.get("lastName").toString();
            if(!lastNameString.isEmpty()) {
                lastName.setText(lastNameString);
            }
            String otherDetailsString = extras.get("otherDetails").toString();
            if(!otherDetailsString.isEmpty()) {
                otherDetails.setText(otherDetailsString);
            }
            Date date = (Date) extras.getSerializable("date");
            if(date != null) {
                missingDate = date;
                String missingDateText = day + "-" + (month + 1) + "-" + year;
                missingDateDisplay.setText(missingDateText);
            }
            Uri imageUri = extras.getParcelable("missingImage");
            if(imageUri != null) {
                personImageUri = imageUri;
                hasChoosenImage = true;
                personImage.setImageURI(personImageUri);
            }
            LatLng marker = extras.getParcelable("markerLocation");
            if(marker != null) {
                markerLocation = marker;
            }
        }

    }

    private void getButtonsAndTexts() {
        firstName = findViewById(R.id.missingPersonFirstNameRequest);
        lastName = findViewById(R.id.missingPersonLastNameRequest);
        otherDetails = findViewById(R.id.missingPersonOtherDetailsRequest);
        btnSubmit = findViewById(R.id.btnSubmitMissingPersonRequest);
        personImage = findViewById(R.id.imgMissingPersonRequest);
    }

    private void addEventsToButtons() {

        btnSubmit.setOnClickListener(event -> {
            if(!hasChoosenImage) {
                Toast.makeText(getApplicationContext(), R.string.toast_no_image, Toast.LENGTH_SHORT).show();
            } else if (firstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.toast_no_first_name, Toast.LENGTH_SHORT).show();
            } else if (lastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.toast_no_last_name, Toast.LENGTH_SHORT).show();
            } else if (missingDate == null) {
                Toast.makeText(getApplicationContext(), R.string.toast_no_last_date, Toast.LENGTH_SHORT).show();
            } else if (markerLocation == null) {
                Toast.makeText(getApplicationContext(), R.string.toast_no_map_location, Toast.LENGTH_SHORT).show();
            } else {
                // TODO Falta criador do Post - Adicionar UserEmail (Necessário Auth)
                MissingPerson missingPerson = new MissingPerson(UUID.randomUUID().toString(),
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        missingDate,
                        markerLocation.latitude, markerLocation.longitude,
                        otherDetails.getText().toString(),
                        dao.uploadImage(personImageUri, "Creator", (firstName.getText().toString().trim() + lastName.getText().toString().trim())),
                        FirebaseAuth.getInstance().getUid());
                dao.add(missingPerson).addOnSuccessListener(sucess -> {
                    Toast.makeText(this, R.string.toast_request_success, Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er -> {
                    Toast.makeText(this, R.string.toast_request_fail, Toast.LENGTH_SHORT).show();
                });
                startActivity(new Intent(MissingPersonRequestActivity.this, MapActivity.class));
            }
        });

        // TODO - Adicionar opção de câmara para além de galeria
        personImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                imagePickerResultLauncher.launch(intent);
            }
        });
    }

}