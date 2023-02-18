package com.example.helpthenext.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpthenext.R;
import com.example.helpthenext.dataclasses.Notification;
import com.example.helpthenext.dataclasses.RecyclerViewNotificationInterface;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>{
    private final RecyclerViewNotificationInterface recyclerViewNotificationInterface;
    List<Notification> data;

    public NotificationsAdapter(List<Notification> data, RecyclerViewNotificationInterface recyclerViewNotificationInterface)
    {
        this.data = data;
        this.recyclerViewNotificationInterface = recyclerViewNotificationInterface;
    }

    @NonNull
    @Override
    public NotificationsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_of_notifications,parent,false);
        return new NotificationsAdapter.MyViewHolder(view, recyclerViewNotificationInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.MyViewHolder holder, int position) {
        Notification app = data.get(position);

        holder.senderName.setText(app.getSenderName());
        switch(app.getType()) {
            case "HOME_OFFER": case "HOME_REQUEST": holder.notificationType.setImageResource(R.drawable.ic_houseicon); break;
            case "RESOURCE_OFFER": case "RESOURCE_REQUEST": holder.notificationType.setImageResource(R.drawable.ic_resourceicon); break;
            case "MISSING_PERSON": holder.notificationType.setImageResource(R.drawable.ic_missingpersonicon); break;
            default: break;
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView senderName;
        ImageView notificationType;
        public MyViewHolder(@NonNull View itemView, RecyclerViewNotificationInterface recyclerViewNotificationInterface) {
            super(itemView);
            senderName = itemView.findViewById(R.id.txtNotificationSender);
            notificationType = itemView.findViewById(R.id.imgNotificationType);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewNotificationInterface != null) {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION) {
                            recyclerViewNotificationInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
