package com.example.helpthenext.dataclasses;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOHomeOffer {
    private final DatabaseReference databaseReference;

    public DAOHomeOffer() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://help-the-next-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = db.getReference(HomeOffer.class.getSimpleName());
    }

    public Task<Void> add(HomeOffer homeOffer) {
        if(homeOffer != null) {
            return databaseReference.push().setValue(homeOffer);
        } else {
            return null;
        }
    }

    public DatabaseReference getReference(){
        return databaseReference;
    }
}
