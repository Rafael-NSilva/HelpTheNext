package com.example.helpthenext;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.helpthenext.dataclasses.DAOHomeRequest;
import com.example.helpthenext.dataclasses.HomeRequest;
import com.example.helpthenext.dataclasses.HomeRequestCallback;
public class ViewAccommodationRequest extends AppCompatActivity implements HomeRequestCallback {
    private Toolbar toolbar;
    private TextView textPropertyTypeView, textNumberOfPeopleView, textOtherDetailsView;
    private DAOHomeRequest daoSupplyRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accommodation_request);

        //Toolbar
        toolbar = findViewById(R.id.toolbarSearchForAccommodationView);

        toolbar.setNavigationOnClickListener(view->{
            startActivity(new Intent(this, MapActivity.class));
            //super.onBackPressed();
        });

        textPropertyTypeView = findViewById(R.id.textPropertyTypeView);
        textNumberOfPeopleView = findViewById(R.id.textNumberOfPeopleView);
        textOtherDetailsView = findViewById(R.id.textOtherDetailsView);

        daoSupplyRequest = new DAOHomeRequest();
        daoSupplyRequest.read(this);

    }

    @Override
    public void loadInfo(HomeRequest homeRequest) {
        textPropertyTypeView.setText(homeRequest.getHomeInfo());
        textNumberOfPeopleView.setText(String.valueOf(homeRequest.getHomeSize()));
        textOtherDetailsView.setText(homeRequest.getOtherDetails());

    }
}
