package com.example.helpthenext.dataclasses;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.EventListener;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class DAOMissingPerson {
    private final DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    public DAOMissingPerson() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://help-the-next-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = db.getReference(MissingPerson.class.getSimpleName());
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public String uploadImage(Uri selectedImage, String creator, String missingName) {
        if (selectedImage != null) {
            StorageReference ref = storageReference.child("missingPerson/images/" + creator.trim() + "_" + UUID.randomUUID().toString());
            ref.putFile(selectedImage);
            return ref.toString();
        }
        return "NoPath";
    }

    public Task<Void> add(MissingPerson missingPerson) {
        if (missingPerson != null) {
            return databaseReference.push().setValue(missingPerson);
        } else {
            return null;
        }
    }

    public void read(String missingPersonID, MissingPersonCallback callback) {
        databaseReference.get().addOnSuccessListener(snapshot -> {
            if(snapshot.hasChildren()) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    if(data.getValue(MissingPerson.class).getId().equals(missingPersonID)) {
                        callback.loadInfo(data.getValue(MissingPerson.class));
                    }
                }
            }
        });
    }

    public void getImage(String reference, MissingPersonCallback callback) {
        StorageReference ref = firebaseStorage.getReferenceFromUrl(reference);
        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downUri = task.getResult();
                callback.loadImage(downUri);
            }
        });
    }

    public DatabaseReference getReference() {
        return databaseReference;
    }
}
