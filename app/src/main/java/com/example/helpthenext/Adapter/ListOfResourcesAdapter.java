package com.example.helpthenext.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpthenext.Model.ListOfResourcesModel;
import com.example.helpthenext.R;
import com.example.helpthenext.dataclasses.Supply;

import java.util.ArrayList;

public class ListOfResourcesAdapter extends RecyclerView.Adapter<ListOfResourcesAdapter.MyViewHolder>{

    ArrayList<Supply> data;


    public ListOfResourcesAdapter(ArrayList<Supply> data)
    {
        this.data = data;
    }

    @NonNull
    @Override
    public ListOfResourcesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_of_resources,parent,false);
        return new ListOfResourcesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfResourcesAdapter.MyViewHolder holder, int position) {
        Supply app = data.get(position);
        if(app != null){
            holder.resourceName.setText(app.getResourceName());
            holder.quantity.setText(String.valueOf(app.getResourceQuantity()));
            holder.unit.setText(app.getQuantityMeasurement());
        } else{
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView resourceName;
        TextView quantity;
        TextView unit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            resourceName = itemView.findViewById(R.id.resourceName);
            quantity = itemView.findViewById(R.id.quantity);
            unit = itemView.findViewById(R.id.unit);

        }
    }

}


