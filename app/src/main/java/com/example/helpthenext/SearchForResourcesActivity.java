package com.example.helpthenext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpthenext.Adapter.FilterAdapter;
import com.example.helpthenext.Adapter.SearchResourcesAdapter;
import com.example.helpthenext.Model.FilterModel;
import com.example.helpthenext.Model.SearchResourcesModel;
import com.example.helpthenext.dataclasses.DAOHomeOffer;
import com.example.helpthenext.dataclasses.DAOSupplyOffer;
import com.example.helpthenext.dataclasses.DAOUserInfo;
import com.example.helpthenext.dataclasses.HomeOffer;
import com.example.helpthenext.dataclasses.SupplyOffer;
import com.example.helpthenext.dataclasses.SupplyOfferCallback;
import com.example.helpthenext.dataclasses.UserInfoCallback;
import com.example.helpthenext.dataclasses.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchForResourcesActivity extends AppCompatActivity implements RecyclerViewInterface{
    private Toolbar toolbar;
    private RecyclerView  verticalView;
    private SearchView searchView;
    private ArrayList<SupplyOffer> allSupplies;
    private ArrayList<HomeOffer> allHomeOffers;
    private DatabaseReference databaseSupply;
    private DatabaseReference databaseHome;
    private DatabaseReference databaseUser;


    private UserInformation user;
    private Uri userImage;
    private boolean test = false;

    private SearchResourcesAdapter resourcesAdapter;
    private ArrayList<SearchResourcesModel> resourcesHolder = new ArrayList<SearchResourcesModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_resources);

        toolbar = findViewById(R.id.toolbarEditListOfResources);
        verticalView = findViewById(R.id.vertical_recyclerView);

        toolbar.setNavigationOnClickListener(view->{
            startActivity(new Intent(this, MapActivity.class));
            //super.onBackPressed();
        });




        ArrayList<SupplyOffer> supplyOffers = new ArrayList<>();
        ArrayList<HomeOffer> homeOffers = new ArrayList<>();
        ArrayList<UserInformation> userInformations = new ArrayList<>();
        databaseSupply = FirebaseDatabase.getInstance().getReference("SupplyOffer");
        databaseHome = FirebaseDatabase.getInstance().getReference("HomeOffer");
        databaseUser = FirebaseDatabase.getInstance().getReference("User");

        databaseSupply.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    SupplyOffer supplyOffer = dataSnapshot.getValue(SupplyOffer.class);
                    supplyOffers.add(supplyOffer);
                    resourcesHolder.add(new SearchResourcesModel(R.drawable.ic_user_icon, "",supplyOffer.getSupplies()));
                }
                resourcesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        databaseHome.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    HomeOffer homeOffer = dataSnapshot.getValue(HomeOffer.class);
                    resourcesHolder.add(new SearchResourcesModel(R.drawable.ic_user_icon, "",homeOffer.getHomeInfo()));
                }
                resourcesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        verticalView.setLayoutManager(new LinearLayoutManager(this));
        System.out.println("AAAAAAA" + resourcesHolder);
        resourcesAdapter = new SearchResourcesAdapter(resourcesHolder, this);
        verticalView.setAdapter(resourcesAdapter);
        resourcesAdapter.notifyDataSetChanged();
        allSupplies = new ArrayList<>();
        // Get User Info



        searchView = findViewById(R.id.searchResourcesView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                resourcesAdapter.getFilter().filter(newText);
                return false;
            }
        });


    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ResourcesToDonateActivity.class);
        intent.putExtra("key", resourcesHolder.get(position).getUserName()); // MUDAR O GETUSER()
        startActivity(intent);
    }
}