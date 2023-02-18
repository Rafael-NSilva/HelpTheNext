package com.example.helpthenext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.BundleCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.helpthenext.dataclasses.DAONotification;
import com.example.helpthenext.dataclasses.DAOUserInfo;
import com.example.helpthenext.dataclasses.Notification;
import com.example.helpthenext.dataclasses.UserInfoCallback;
import com.example.helpthenext.dataclasses.UserInformation;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class LostPersonMessage extends AppCompatActivity implements UserInfoCallback {

    private Toolbar toolbar;
    private EditText message;
    private Button sendBtn;
    private FirebaseAuth auth;
    private Notification notification;
    private DAONotification daoNotification;
    private DAOUserInfo daoUserInfo;
    private String senderUID;
    private String notificationType;
    private TextView rcvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_person_message);

        toolbar = findViewById(R.id.toolbarMessagge);
        toolbar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(LostPersonMessage.this, MapActivity.class));
        });

        daoNotification = new DAONotification();
        daoUserInfo = new DAOUserInfo();
        rcvText = findViewById(R.id.textView);
        message = findViewById(R.id.msg);
        sendBtn = findViewById(R.id.sendButton);
        Bundle extras = getIntent().getExtras();
        auth = FirebaseAuth.getInstance();

        if (extras != null && extras.containsKey("notificationType") && extras.containsKey("senderUserID")) {
            notificationType = extras.getString("notificationType");
            senderUID = extras.getString("senderUserID");
            getIntent().removeExtra("notificationType");
            getIntent().removeExtra("senderUserID");
        }
        if (extras!= null && extras.containsKey("messageContent")){
            rcvText.setText(extras.getString("messageContent"));
            rcvText.setVisibility(View.VISIBLE);
        }else {rcvText.setVisibility(View.INVISIBLE);}

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Troca do Recetor pelo Enviador
                daoUserInfo.readTwoUsers(auth.getUid(), senderUID, LostPersonMessage.this);

            }
        });

    }

    @Override
    public void loadUserInfo(UserInformation userInformation) {

    }

    @Override
    public void loadUsersInfo(List<UserInformation> userInformationList) {
        notification = new Notification(UUID.randomUUID().toString(),
                userInformationList.get(0).getFirstName() + " " + userInformationList.get(0).getLastName(),
                auth.getUid().toString(),
                userInformationList.get(1).getFirstName() + " " + userInformationList.get(1).getLastName(),
                senderUID,
                notificationType,
                message.getText().toString()
                );
        System.out.println(notification.getType() + " - AAAAAAAAAAAAAAAAAAAAAAAAA");
        daoNotification.add(notification);
        startActivity(new Intent(LostPersonMessage.this, MapActivity.class));
    }

    @Override
    public void loadImage(Uri missingImage) {

    }
}