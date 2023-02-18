package com.example.helpthenext;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.helpthenext.dataclasses.DAOHomeOffer;
import com.example.helpthenext.dataclasses.DAOSupplyOffer;
import com.example.helpthenext.dataclasses.HomeOffer;
import com.example.helpthenext.dataclasses.SupplyOffer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class ResourcesDonationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CheckBox checkBoxAccommodation, checkBoxResources;
    private TextView houseSpecifications, numberOfPeople,resourcesToDonate, sliderTxtQuantity;
    private TextInputLayout textInputLayoutHouseDetails, textInputLayoutResources;
    private TextInputEditText textInputHouseDetails, textInputResources;
    private EditText numberOfPeopleCounter;
    private Button btnSubmitHelp;
    private Slider slider;
    private Spinner spinner;
    private DAOSupplyOffer daoSupplyOffer;
    private DAOHomeOffer daoHomeOffer;

    private LatLng markerLocation;

    private String errorCheckboxNotChecked, errorInputFieldsEmpty, errorSliderValue, errorSpinnerValueNotSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_donation);

        daoSupplyOffer = new DAOSupplyOffer();
        daoHomeOffer = new DAOHomeOffer();

        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener((v) -> super.onBackPressed());

        errorCheckboxNotChecked = getResources().getString(R.string.throw_error_no_checkboxes_checked);
        errorInputFieldsEmpty = getResources().getString(R.string.throw_error_empty_field);
        errorSliderValue = getResources().getString(R.string.throw_error_slider_value_null);
        errorSpinnerValueNotSelected = getResources().getString(R.string.throw_error_spinner_not_selected_value);

        checkBoxAccommodation = findViewById(R.id.checkBoxAccommodation);
        checkBoxResources = findViewById(R.id.checkBoxResources);
        houseSpecifications = findViewById(R.id.houseSpecifications);
        numberOfPeople = findViewById(R.id.numberOfPeople);
        textInputLayoutHouseDetails = findViewById(R.id.textInputLayoutHouseDetails);
        textInputHouseDetails = findViewById(R.id.textInputHouseDetails);
        numberOfPeopleCounter = findViewById(R.id.numberOfPeopleCounter);
        resourcesToDonate = findViewById(R.id.resourcesToDonate);
        textInputLayoutResources = findViewById(R.id.textInputLayoutResources);
        textInputResources = findViewById(R.id.textInputResources);
        slider = findViewById(R.id.slider);
        sliderTxtQuantity = findViewById(R.id.sliderTxtQuantity);
        spinner = findViewById(R.id.txtMeasurements);
        btnSubmitHelp = findViewById(R.id.btnSubmitHelp);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("userLocation")){
            LatLng marker = extras.getParcelable("userLocation");
            if(marker != null){
                markerLocation = marker;
            }
        }

        //Code for checkbox of houseSpecs
        houseSpecifications.setVisibility(View.INVISIBLE);
        textInputHouseDetails.setVisibility(View.INVISIBLE);
        textInputLayoutHouseDetails.setVisibility(View.INVISIBLE);
        numberOfPeople.setVisibility(View.INVISIBLE);
        numberOfPeopleCounter.setVisibility(View.INVISIBLE);
        checkBoxAccommodation.setOnCheckedChangeListener((compoundButton, b) -> {
            if(checkBoxAccommodation.isChecked()){
                houseSpecifications.setVisibility(View.VISIBLE);
                textInputHouseDetails.setVisibility(View.VISIBLE);
                textInputLayoutHouseDetails.setVisibility(View.VISIBLE);
                numberOfPeople.setVisibility(View.VISIBLE);
                numberOfPeopleCounter.setVisibility(View.VISIBLE);
            }
            else{
                houseSpecifications.setVisibility(View.INVISIBLE);
                textInputHouseDetails.setVisibility(View.INVISIBLE);
                textInputLayoutHouseDetails.setVisibility(View.INVISIBLE);
                numberOfPeople.setVisibility(View.INVISIBLE);
                numberOfPeopleCounter.setVisibility(View.INVISIBLE);
            }
        });

        //Code for checkbox of recourses
        resourcesToDonate.setVisibility(View.INVISIBLE);
        textInputLayoutResources.setVisibility(View.INVISIBLE);
        textInputResources.setVisibility(View.INVISIBLE);
        slider.setVisibility(View.INVISIBLE);
        sliderTxtQuantity.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        checkBoxResources.setOnCheckedChangeListener((compoundButton, b) -> {
            if(checkBoxResources.isChecked()){
                resourcesToDonate.setVisibility(View.VISIBLE);
                textInputLayoutResources.setVisibility(View.VISIBLE);
                textInputResources.setVisibility(View.VISIBLE);
                slider.setVisibility(View.VISIBLE);
                sliderTxtQuantity.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);

                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.unit_of_measurements, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
            else {
                resourcesToDonate.setVisibility(View.INVISIBLE);
                textInputLayoutResources.setVisibility(View.INVISIBLE);
                textInputResources.setVisibility(View.INVISIBLE);
                slider.setVisibility(View.INVISIBLE);
                sliderTxtQuantity.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
            }
        });

        slider.addOnChangeListener((slider, value, fromUser) -> sliderTxtQuantity.setText(String.valueOf(value)));

        btnSubmitHelp.setOnClickListener(view -> {
            if(checkBoxAccommodation.isChecked() || checkBoxResources.isChecked()){
                if(checkBoxAccommodation.isChecked()){
                    if(!TextUtils.isEmpty(textInputHouseDetails.getText().toString()) &&
                        !TextUtils.isEmpty(numberOfPeopleCounter.getText().toString())){

                        daoHomeOffer.add(new HomeOffer(FirebaseAuth.getInstance().getUid() ,textInputHouseDetails.getText().toString(), Double.parseDouble(numberOfPeopleCounter.getText().toString()), new Date(), markerLocation.latitude, markerLocation.longitude));
                        startActivity(new Intent(this, SearchForResourcesActivity.class));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), errorInputFieldsEmpty, Toast.LENGTH_SHORT).show();
                    }
                }
                if(checkBoxResources.isChecked()){
                    if(!TextUtils.isEmpty(textInputResources.getText().toString()) &&
                            slider.getValue()>0 && !spinner.getSelectedItem().toString().isEmpty()){

                        daoSupplyOffer.add(new SupplyOffer(FirebaseAuth.getInstance().getUid(), textInputResources.getText().toString(), Double.parseDouble(sliderTxtQuantity.getText().toString()), spinner.getSelectedItem().toString(),new Date(), markerLocation.latitude, markerLocation.longitude));
                        startActivity(new Intent(this, SearchForResourcesActivity.class));
                    }
                    else if(TextUtils.isEmpty(textInputResources.getText().toString())){
                        Toast.makeText(getApplicationContext(), errorInputFieldsEmpty, Toast.LENGTH_LONG).show();
                    }
                    else if(sliderTxtQuantity.getText().toString().equals("") || sliderTxtQuantity.getText().toString().equals("0")){
                        Toast.makeText(getApplicationContext(), errorSliderValue, Toast.LENGTH_LONG).show();
                    }
                    else if(spinner.getSelectedItem().toString().equals("Unit") || spinner.getSelectedItem().toString().equals("Unid")){
                        Toast.makeText(getApplicationContext(), errorSpinnerValueNotSelected, Toast.LENGTH_LONG).show();
                    }
                }
            }
            else if(!checkBoxAccommodation.isChecked() || !checkBoxResources.isChecked()){
                Toast.makeText(getApplicationContext(), errorCheckboxNotChecked, Toast.LENGTH_SHORT).show();
            }
        });
    }
}