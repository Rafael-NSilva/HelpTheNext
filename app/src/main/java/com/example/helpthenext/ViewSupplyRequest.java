package com.example.helpthenext;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpthenext.Adapter.ListOfResourcesAdapter;
import com.example.helpthenext.dataclasses.DAOSupplyRequest;
import com.example.helpthenext.dataclasses.Supply;
import com.example.helpthenext.dataclasses.SupplyRequestCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ViewSupplyRequest  extends AppCompatActivity implements SupplyRequestCallback {
    private Toolbar toolbar;
    private RecyclerView listOfResources;
    private ListOfResourcesAdapter listOfResourcesAdapter;
    private ArrayList<Supply> resourcesHolder;
    private DAOSupplyRequest daoSupplyRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_of_resources);

        //Toolbar
        toolbar = findViewById(R.id.toolbarViewListOfResources);

        toolbar.setNavigationOnClickListener(view->{
            startActivity(new Intent(this, MapActivity.class));
            //super.onBackPressed();
        });


        listOfResources = findViewById(R.id.viewListOfItemsRView);
        resourcesHolder = new ArrayList<>();
        daoSupplyRequest = new DAOSupplyRequest();
        daoSupplyRequest.read(FirebaseAuth.getInstance().getUid(),this);

        listOfResources.setLayoutManager(new LinearLayoutManager(this));
        listOfResourcesAdapter = new ListOfResourcesAdapter(resourcesHolder);
        listOfResources.setAdapter(listOfResourcesAdapter);
    }

    @Override
    public void loadInfo(ArrayList<Supply> supplies) {
        resourcesHolder.addAll(supplies);
        listOfResourcesAdapter.notifyDataSetChanged();
    }
}
