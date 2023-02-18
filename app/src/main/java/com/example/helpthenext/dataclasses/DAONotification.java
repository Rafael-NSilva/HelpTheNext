package com.example.helpthenext.dataclasses;

import android.net.Uri;

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

public class DAONotification {
    private final DatabaseReference databaseReference;


    public DAONotification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase db = FirebaseDatabase.getInstance("https://help-the-next-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = db.getReference(Notification.class.getSimpleName());
    }

    public Task<Void> add(Notification notification) {
        if (notification != null) {
            return databaseReference.push().setValue(notification);
        } else {
            return null;
        }
    }

    /**
     * Função para ler da base de dados notificações
     * @param receiverID Utilizador que recebeu as notificações
     * @param callback Função a ser executada após a informação da base de dados ser recebida
     */
    public void read(String receiverID, NotificationCallback callback) {
        databaseReference.get().addOnSuccessListener(snapshot -> {
            if(snapshot.hasChildren()) {
                List<Notification> notifications = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()) {
                    if(data.getValue(Notification.class).getReceiverID().equals(receiverID)) {
                        notifications.add(data.getValue(Notification.class));
                    }
                }
                callback.loadInfo(notifications);
            }
        });
    }


}
