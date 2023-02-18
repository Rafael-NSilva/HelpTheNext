package com.example.helpthenext;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpthenext.Adapter.ListOfResourcesAdapter;
import com.example.helpthenext.Model.ListOfResourcesModel;
import com.example.helpthenext.dataclasses.DAOSupplyRequest;
import com.example.helpthenext.dataclasses.Supply;
import com.example.helpthenext.dataclasses.SupplyRequestCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ListOfResourcesActivity extends AppCompatActivity implements SupplyRequestCallback {
    private RecyclerView listOfResources;
    private ListOfResourcesAdapter listOfResourcesAdapter;
    private Button editList;
    private Toolbar toolbar;
    private ArrayList<Supply> resourcesHolder;
    private DAOSupplyRequest daoSupplyRequest;
    private LatLng markerLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_resources);

        //Toolbar
        toolbar = findViewById(R.id.toolbarListOfResources);

        toolbar.setNavigationOnClickListener(view->{
            startActivity(new Intent(this, MapActivity.class));
            //super.onBackPressed();
        });


        listOfResources = findViewById(R.id.listOfItemsRView);
        editList = findViewById(R.id.btnEditListOfResources);
        resourcesHolder = new ArrayList<>();
        daoSupplyRequest = new DAOSupplyRequest();
        daoSupplyRequest.read(FirebaseAuth.getInstance().getUid(),this);

        listOfResources.setLayoutManager(new LinearLayoutManager(this));
        listOfResourcesAdapter = new ListOfResourcesAdapter(resourcesHolder);
        listOfResources.setAdapter(listOfResourcesAdapter);


        editList.setOnClickListener(view -> {
            Bundle extras = getIntent().getExtras();
            if(extras != null && extras.containsKey("userLocation")){
                LatLng marker = extras.getParcelable("userLocation");
                if(marker != null){
                    markerLocation = marker;
                }
            }
            Intent intent = new Intent(this, EditListOfResourcesActivity.class);
            intent.putExtra("userLocation", markerLocation);
            startActivity(intent);
        });
    }

    @Override
    public void loadInfo(ArrayList<Supply> supplies) {
        resourcesHolder.addAll(supplies);
        listOfResourcesAdapter.notifyDataSetChanged();
    }
}
