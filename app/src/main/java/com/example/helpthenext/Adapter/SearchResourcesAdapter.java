package com.example.helpthenext.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpthenext.Model.SearchResourcesModel;
import com.example.helpthenext.R;
import com.example.helpthenext.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class SearchResourcesAdapter extends RecyclerView.Adapter<SearchResourcesAdapter.MyViewHolder> implements Filterable {

    private final RecyclerViewInterface recyclerViewInterface;
    private ArrayList<SearchResourcesModel> data;
    private ArrayList<SearchResourcesModel> dataAll;

    public SearchResourcesAdapter(ArrayList<SearchResourcesModel> data,RecyclerViewInterface recyclerViewInterface)
    {
        this.data = data;
        this.dataAll = new ArrayList<>(data);
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public SearchResourcesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.vertical_cardview_search_for_resources,parent,false);
        return new SearchResourcesAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResourcesAdapter.MyViewHolder holder, int position) {
        SearchResourcesModel app = data.get(position);


        holder.userProfilePicture.setImageResource(app.getUserProfilePicture());
        holder.userName.setText(app.getUserName());
        holder.resourceName.setText(app.getResourceName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<SearchResourcesModel> filteredList = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                filteredList.addAll(dataAll);
            } else{
                for(SearchResourcesModel s : dataAll){
                    if(s.getResourceName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(s);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            data.clear();
            data.addAll((Collection<? extends SearchResourcesModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView userProfilePicture;
        TextView userName;
        TextView resourceName;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            userProfilePicture = itemView.findViewById(R.id.userPictureResources);
            userName = itemView.findViewById(R.id.userNameResources);
            resourceName = itemView.findViewById(R.id.resourceName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

}

