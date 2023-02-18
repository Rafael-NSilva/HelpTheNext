package com.example.helpthenext.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpthenext.Model.FilterModel;
import com.example.helpthenext.R;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder>
{
    ArrayList<FilterModel> data;

    public FilterAdapter(ArrayList<FilterModel> data)
    {
        this.data = data;
    }

    @NonNull
    @Override
    public FilterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.horizontal_cardview_search_for_resources,parent,false);
        return new FilterAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.MyViewHolder holder, int position)
    {
        FilterModel app = data.get(position);

        holder.mName.setText(app.getName());
        holder.mImage.setImageResource(app.getImage());
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mName;
        ImageView mImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mImage = itemView.findViewById(R.id.image);
        }
    }
}