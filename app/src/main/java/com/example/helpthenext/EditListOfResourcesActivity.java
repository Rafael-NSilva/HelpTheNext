package com.example.helpthenext;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.helpthenext.Adapter.EditListofResourcesAdapter;
import com.example.helpthenext.dataclasses.DAOSupplyRequest;
import com.example.helpthenext.dataclasses.DAOUserInfo;
import com.example.helpthenext.dataclasses.Supply;
import com.example.helpthenext.dataclasses.SupplyRequest;
import com.example.helpthenext.dataclasses.SupplyRequestCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;


public class EditListOfResourcesActivity extends AppCompatActivity implements SupplyRequestCallback {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EditListofResourcesAdapter listOfResourcesAdapter;
    private Button submitListOfRequestedResources;
    private Button add;
    private DAOSupplyRequest daoSupplyRequest;
    private LatLng markerLocation;
    private DAOUserInfo daoUserInfo;

    private final ArrayList<Supply> resourcesHolder = new ArrayList<Supply>();
    private final ArrayList<Supply> newAddedResources = new ArrayList<Supply>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list_of_resources);

        //Toolbar
        toolbar = findViewById(R.id.toolbarEditListOfResources);
        toolbar.setNavigationOnClickListener(view->{
            startActivity(new Intent(this, ListOfResourcesActivity.class));
            //super.onBackPressed();
        });

        daoUserInfo = new DAOUserInfo();
        daoSupplyRequest = new DAOSupplyRequest();
        daoSupplyRequest.read(FirebaseAuth.getInstance().getUid(),this);

        //RecyclerView
        recyclerView = findViewById(R.id.editListOfResourcesRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listOfResourcesAdapter = new EditListofResourcesAdapter(this, resourcesHolder, daoSupplyRequest);
        recyclerView.setAdapter(listOfResourcesAdapter);

        // Buttons
        submitListOfRequestedResources = findViewById(R.id.btnSubmitEditList);
        add = findViewById(R.id.btnAddCard);


        submitListOfRequestedResources.setOnClickListener(view -> {
            Bundle extras = getIntent().getExtras();
            if(extras != null && extras.containsKey("userLocation")){
                LatLng marker = extras.getParcelable("userLocation");
                if(marker != null){
                    markerLocation = marker;
                }
            }
            daoSupplyRequest.add(new SupplyRequest(newAddedResources, new Date(), markerLocation.latitude, markerLocation.longitude, "", FirebaseAuth.getInstance().getUid()));
            startActivity(new Intent(this, ListOfResourcesActivity.class));
        });


        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Dialog dialog = new Dialog(EditListOfResourcesActivity.this);
                dialog.setContentView(R.layout.edit_resources_cardview);
                EditText editResourceName = dialog.findViewById(R.id.editResourceName);
                TextView txtSliderQuantity = dialog.findViewById(R.id.txtSliderQuantity);
                Slider slider = dialog.findViewById(R.id.editQuantitySlider);
                Spinner spinner = dialog.findViewById(R.id.spinnerEditUnit);
                Button submitNewResource = dialog.findViewById(R.id.submitNewResource);

                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.unit_of_measurements, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                slider.addOnChangeListener(new Slider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                        txtSliderQuantity.setText(String.valueOf(value));
                    }
                });

                submitNewResource.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String spinnerA = "";
                        String nameOfResource = "";
                        String sliderValue = "";

                        if(editResourceName.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Ã‰ necessÃ¡rio indicar o nome do recurso", Toast.LENGTH_SHORT).show();
                        } else if(spinner.getSelectedItem().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Ã‰ necessÃ¡rio indicar a unidade", Toast.LENGTH_SHORT).show();
                        } else if(txtSliderQuantity.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Ã‰ necessÃ¡rio indicar a quantidade", Toast.LENGTH_SHORT).show();
                        } else {
                            sliderValue = txtSliderQuantity.getText().toString();
                            nameOfResource = editResourceName.getText().toString();
                            spinnerA = spinner.getSelectedItem().toString();
                            newAddedResources.add(new Supply(nameOfResource, Double.parseDouble(sliderValue), spinnerA));
                            resourcesHolder.add(new Supply(nameOfResource, Double.parseDouble(sliderValue), spinnerA));
                            Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
                            listOfResourcesAdapter.notifyItemInserted(resourcesHolder.size()-1);
                            recyclerView.scrollToPosition(resourcesHolder.size()-1);
                            dialog.dismiss();
                        }

                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void loadInfo(ArrayList<Supply> supplies) {
        resourcesHolder.addAll(supplies);
        listOfResourcesAdapter.notifyDataSetChanged();
    }
}
