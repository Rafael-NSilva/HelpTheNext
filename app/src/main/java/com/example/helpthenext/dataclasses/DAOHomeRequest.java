package com.example.helpthenext.dataclasses;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOHomeRequest {
    private final DatabaseReference databaseReference;

    public DAOHomeRequest() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://help-the-next-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = db.getReference(HomeRequest.class.getSimpleName());
    }

    public Task<Void> add(HomeRequest homeRequest) {
        if(homeRequest != null) {
            return databaseReference.push().setValue(homeRequest);
        } else {
            return null;
        }
    }

    public void read(HomeRequestCallback callback) {
        databaseReference.get().addOnSuccessListener(snapshot -> {
            if(snapshot.hasChildren()) {
                for(DataSnapshot data : snapshot.getChildren()) {
                        callback.loadInfo(data.getValue(HomeRequest.class));
                }
            }
        });
    }

    public DatabaseReference getReference(){
        return databaseReference;
    }
}