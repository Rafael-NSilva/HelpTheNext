package com.example.helpthenext.dataclasses;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

public class DAOSupplyOffer {
    private final DatabaseReference databaseReference;

    public DAOSupplyOffer() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://help-the-next-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = db.getReference(SupplyOffer.class.getSimpleName());
    }

    public Task<Void> add(SupplyOffer supplyOffer) {
        if(supplyOffer != null) {
            return databaseReference.push().setValue(supplyOffer);
        } else {
            return null;
        }
    }

    public void read(SupplyOfferCallback callback) {
        databaseReference.get().addOnSuccessListener(snapshot -> {
            ArrayList<SupplyOffer> supplies = new ArrayList<>();
            if(snapshot.hasChildren()) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    System.out.println("AAAAAAAAAAAAA "+ data);
                    SupplyOffer a = data.getValue(SupplyOffer.class);
                    System.out.println(a.toString());
                    supplies.add(data.getValue(SupplyOffer.class));
                }
                System.out.println(supplies.toString());;
                callback.loadSupplyInfo(supplies);
            }
        });
    }

    public DatabaseReference getReference(){
        return databaseReference;
    }
}
