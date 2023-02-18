package com.example.helpthenext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.helpthenext.Adapter.DocumentsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentsActivity extends AppCompatActivity {
    private Toolbar appBar;
    private View divider;
    private Spinner countrySpinner;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private Map<String, List<String>> countryCollection;
    private List<String> groupList;
    private List<String> childList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documents);

        appBar = findViewById(R.id.toolbar);
        divider = findViewById(R.id.divider);
        expandableListView = findViewById(R.id.docsList);
        countrySpinner = findViewById(R.id.country_select);

        //Mete a seta a funcionar
        appBar.setNavigationOnClickListener(view->{
            startActivity(new Intent(this, MapActivity.class));
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countries_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String defaultCountry = getResources().getStringArray(R.array.countries_array)[0];
                if(adapterView.getSelectedItem().toString().equals(defaultCountry)){
                    divider.setVisibility(View.INVISIBLE);
                    expandableListView.setAdapter((ExpandableListAdapter) null);
                } else {
                    createCollection();
                    divider.setVisibility(View.VISIBLE);
                    expandableListAdapter = new DocumentsAdapter(DocumentsActivity.this, groupList, (HashMap<String, List<String>>) countryCollection);
                    expandableListView.setAdapter(expandableListAdapter);

                    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        int lastExpandedPosition = -1;
                        @Override
                        public void onGroupExpand(int i) {
                            if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                                expandableListView.collapseGroup(lastExpandedPosition);
                            }
                            lastExpandedPosition = i;
                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Não faz nada
            }
        });

        createGroupList();
        createCollection();
    }

    private void createCollection() {
        childList = new ArrayList<>();
        countryCollection = new HashMap<>();
        String[] countryArray = getResources().getStringArray(R.array.countries_array);
        for(String group : groupList){
            if(countrySpinner.getSelectedItem().toString().equals(countryArray[1])){
                childList.add(getResources().getString(R.string.portugalAsylumDetails));
            } else if(countrySpinner.getSelectedItem().toString().equals(countryArray[2])){
                childList.add(getResources().getString(R.string.spainAsylumDetails));
            } else if(countrySpinner.getSelectedItem().toString().equals(countryArray[3])){
                childList.add(getResources().getString(R.string.unitedKingdomAsylumDetails));
            }
            countryCollection.put(group, childList);
        }
    }

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add(getResources().getString(R.string.asylumDocs));
    }
}