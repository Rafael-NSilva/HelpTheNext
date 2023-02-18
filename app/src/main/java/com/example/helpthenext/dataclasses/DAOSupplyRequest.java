package com.example.helpthenext.dataclasses;

import androidx.annotation.NonNull;

import com.example.helpthenext.Adapter.EditListofResourcesAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DAOSupplyRequest {
    private final DatabaseReference databaseReference;

    public DAOSupplyRequest() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://help-the-next-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = db.getReference(SupplyRequest.class.getSimpleName());
    }

    public Task<Void> add(SupplyRequest supplyRequest) {
        if(supplyRequest != null) {
            return databaseReference.push().setValue(supplyRequest);
        } else {
            return null;
        }
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        databaseReference.get().addOnSuccessListener(snapshot -> {
            if(snapshot.hasChildren()) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    data.child("supplies").child(key).getRef().removeValue();
                }
            }
        });
        return null;
    }

    public Task<Void> remove(String index) {
        databaseReference.get().addOnSuccessListener(snapshot -> {
            if(snapshot.hasChildren()) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    data.child("supplies").child(index).getRef().removeValue();
                }
            }
        });
        return null;
    }

    public void read(String userID, SupplyRequestCallback callback) {
        databaseReference.get().addOnSuccessListener(snapshot -> {
            ArrayList<Supply> supllies = new ArrayList<>();
            if(snapshot.hasChildren()) {
                for(DataSnapshot data : snapshot.getChildren()) {
                        if(data.child("userID").getValue().equals(userID)){
                            GenericTypeIndicator<ArrayList<Supply>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Supply>>(){};
                            supllies = data.child("supplies").getValue(genericTypeIndicator);
                            if(supllies != null) {
                                callback.loadInfo(supllies);
                            }
                        } else{
                            callback.loadInfo(supllies);
                        }
                }
            }
        });
    }

    public DatabaseReference getReference(){
        return databaseReference;
    }
}
