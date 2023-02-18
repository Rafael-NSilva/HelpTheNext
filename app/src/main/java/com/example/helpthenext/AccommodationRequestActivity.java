package com.example.helpthenext;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.helpthenext.dataclasses.DAOHomeRequest;
import com.example.helpthenext.dataclasses.HomeRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class AccommodationRequestActivity extends AppCompatActivity {


    private AutoCompleteTextView autoCompleteTxt;
    private EditText numberOfPeople;
    private TextInputLayout otherDetails;
    private ArrayAdapter<CharSequence> adapterItems;
    private DAOHomeRequest daoHomeRequest;
    private Toolbar toolbar;
    private Button btnSubmit;
    public String typeOfProperty;
    private LatLng markerLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_accommodation);

        //Toolbar
        toolbar = findViewById(R.id.toolbarSearchForAccommodation);

        toolbar.setNavigationOnClickListener(view->{
            startActivity(new Intent(this, MapActivity.class));
            //super.onBackPressed();
        });
        //Dropdown
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        adapterItems = ArrayAdapter.createFromResource(this,R.array.type_of_properties, android.R.layout.simple_spinner_item);
        autoCompleteTxt.setAdapter(adapterItems);

        //Inputs
        autoCompleteTxt.setOnItemClickListener((adapterView, view, i, l) -> {
            typeOfProperty = adapterView.getItemAtPosition(i).toString();
        });
        numberOfPeople = findViewById(R.id.textNumberOfPeople);
        otherDetails = findViewById(R.id.accommodationOtherDetailsRequest);

        //Button
        btnSubmit = findViewById(R.id.btnSubmitAccommodationRequest);

        //
        daoHomeRequest = new DAOHomeRequest();


        //OnClick
        btnSubmit.setOnClickListener(view -> {
            Bundle extras = getIntent().getExtras();
            int numOfPeople = Integer.parseInt(numberOfPeople.getText().toString());
            if(typeOfProperty.trim().isEmpty()){
                Toast.makeText(this, R.string.toast_no_property_type, Toast.LENGTH_SHORT).show();
            } else if(numOfPeople <= 0 ){
                Toast.makeText(this, R.string.toast_no_number_of_people, Toast.LENGTH_SHORT).show();
            }
            if(extras != null && extras.containsKey("userLocation")){
                LatLng marker = extras.getParcelable("userLocation");
                if(marker != null){
                    markerLocation = marker;
                }
            }
            daoHomeRequest.add(new HomeRequest(FirebaseAuth.getInstance().getUid(),typeOfProperty, numOfPeople, new Date(), markerLocation.latitude, markerLocation.longitude, otherDetails.getEditText().getText().toString()));
            startActivity(new Intent(AccommodationRequestActivity.this, MapActivity.class));
        });


        /*
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });
        */
    }
}