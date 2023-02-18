package com.example.helpthenext.dataclasses;

import android.graphics.Bitmap;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DAOUserInfo {
    private final DatabaseReference databaseReference;
    private String currentUserID;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    public DAOUserInfo() {
        currentUserID = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://help-the-next-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = db.getReference("Users");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public String uploadImage(Uri selectedImage, String userId) {
        System.out.println(selectedImage.toString());
        if (selectedImage != null) {
            StorageReference ref = storageReference.child("users/" + userId);
            databaseReference.child(userId).child("profileImageRef").setValue(ref.toString());
            ref.putFile(selectedImage);
            return ref.toString();
        }
        return "NoPath";
    }

    public void getImage(String reference, UserInfoCallback callback) {
        StorageReference ref = firebaseStorage.getReferenceFromUrl(reference);
        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downUri = task.getResult();
                callback.loadImage(downUri);
            }
        });
    }

    public void readCurrentUser(UserInfoCallback callback) {
        readUser(currentUserID, callback);
    }

    public void readUser(String userID, UserInfoCallback callback) {
        databaseReference.child(userID).get().addOnSuccessListener(snapshot -> {
            UserInformation userInformation = snapshot.getValue(UserInformation.class);
            callback.loadUserInfo(userInformation);
        });
    }

    public void readTwoUsers(String userId1, String userId2, UserInfoCallback callback) {
            databaseReference.child(userId1).get().addOnSuccessListener(snapshot -> {
                List<UserInformation> users = new ArrayList<>();
                users.add(snapshot.getValue(UserInformation.class));
                System.out.println(userId1 + " | " + userId2);
                databaseReference.child(userId2).get().addOnSuccessListener(snapshot2 -> {
                    users.add(snapshot2.getValue(UserInformation.class));
                    callback.loadUsersInfo(users);
                });
            });
    }

    public void changeInfo(String userId, String attributeToChange, String newValue) {
        databaseReference.child(userId).child(attributeToChange).setValue(newValue);
    }

}
