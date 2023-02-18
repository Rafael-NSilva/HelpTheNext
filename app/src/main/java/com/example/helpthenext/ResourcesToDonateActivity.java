package com.example.helpthenext;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpthenext.Adapter.ListOfResourcesAdapter;
import com.example.helpthenext.Model.ListOfResourcesModel;
import com.example.helpthenext.dataclasses.HomeOffer;
import com.example.helpthenext.dataclasses.Supply;
import com.example.helpthenext.dataclasses.SupplyOffer;
import com.example.helpthenext.dataclasses.SupplyOfferCallback;

import java.util.ArrayList;

public class ResourcesToDonateActivity extends AppCompatActivity implements SupplyOfferCallback {
    private RecyclerView listOfResources;
    private ListOfResourcesAdapter listOfResourcesAdapter;
    private Toolbar toolbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_to_donate);

        listOfResources = findViewById(R.id.listOfItemsRView);
        toolbar = findViewById(R.id.toolbarDonationListOfUser);
        
        listOfResources.setLayoutManager(new LinearLayoutManager(this));
        listOfResourcesAdapter = new ListOfResourcesAdapter(resourcesData());
        listOfResources.setAdapter(listOfResourcesAdapter);
        
        String value = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             value = extras.getString("key");
            //The key argument here must match that used in the other activity
        }
        toolbar.setTitle(value + " " + getResources().getString(R.string.donate_resources_label));

        toolbar.setNavigationOnClickListener(view->{
            startActivity(new Intent(this, SearchForResourcesActivity.class));
            //super.onBackPressed();
        });

    }

    public ArrayList<Supply> resourcesData() {
        ArrayList<Supply> resourcesHolder = new ArrayList<Supply>();

        resourcesHolder.add(new Supply("Farinha", 2,"kg"));
        resourcesHolder.add(new Supply("Farinha", 2,"kg"));
        resourcesHolder.add(new Supply("Farinha", 2,"kg"));
        resourcesHolder.add(new Supply("Farinha", 2,"kg"));
        resourcesHolder.add(new Supply("Farinha", 2,"kg"));

        return resourcesHolder;
    }

    @Override
    public void loadSupplyInfo(ArrayList<SupplyOffer> supplyOffers) {

    }

    @Override
    public void loadHomeInfo(ArrayList<HomeOffer> homeOffers) {

    }
}
