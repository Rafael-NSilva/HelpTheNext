package com.example.helpthenext;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpthenext.Adapter.FilterAdapter;
import com.example.helpthenext.Adapter.ListOfResourcesAdapter;
import com.example.helpthenext.Adapter.NotificationsAdapter;
import com.example.helpthenext.Adapter.SearchResourcesAdapter;
import com.example.helpthenext.Model.ListOfResourcesModel;
import com.example.helpthenext.dataclasses.DAONotification;
import com.example.helpthenext.dataclasses.Notification;
import com.example.helpthenext.dataclasses.NotificationCallback;
import com.example.helpthenext.dataclasses.RecyclerViewNotificationInterface;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationsActivity extends AppCompatActivity implements NotificationCallback, RecyclerViewNotificationInterface {
    private Toolbar toolbar;
    private DAONotification daoNotification;
    private List<Notification> notifications;

    private RecyclerView listOfNotificationsView;

    private NotificationsAdapter notificationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notifications);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener((v) -> {
            super.onBackPressed();
        });

        daoNotification = new DAONotification();

        // Variaveis de Teste
        // daoNotification.add(new Notification(UUID.randomUUID().toString(), "Sender1", "Receiver1", Notification.NotificationType.RESOURCE_OFFER));
        // daoNotification.add(new Notification(UUID.randomUUID().toString(), "Sender2", "Receiver2", Notification.NotificationType.RESOURCE_OFFER));
        // daoNotification.add(new Notification(UUID.randomUUID().toString(), "Sender2", "Receiver1", Notification.NotificationType.HOME_OFFER));
        // daoNotification.add(new Notification(UUID.randomUUID().toString(), "Sender3", "Receiver1", Notification.NotificationType.MISSING_PERSON));


        listOfNotificationsView = findViewById(R.id.listNotifications);

        listOfNotificationsView.setLayoutManager(new LinearLayoutManager(this));

        // TODO - IR BUSCAR UTILIZADOR LOGADO DE MOMENTO PARA IR BUSCAR NOTIFICAÇÕES PARA ELE
        daoNotification.read(FirebaseAuth.getInstance().getUid(), this);
    }


    @Override
    public void loadInfo(List<Notification> notifications) {
        System.out.println(notifications);
        this.notifications = notifications;
        notificationsAdapter = new NotificationsAdapter(notifications, (RecyclerViewNotificationInterface) this);
        listOfNotificationsView.setAdapter(notificationsAdapter);
        notificationsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(NotificationsActivity.this, LostPersonMessage.class);
        intent.putExtra("senderUserID", notifications.get(position).getSenderID());
        intent.putExtra("notificationType", notifications.get(position).getType());
        intent.putExtra("messageContent", notifications.get(position).getMessageContent());
        startActivity(intent);
    }
}
