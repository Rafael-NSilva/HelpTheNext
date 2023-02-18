package com.example.helpthenext.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpthenext.EditListOfResourcesActivity;
import com.example.helpthenext.Model.ListOfResourcesModel;
import com.example.helpthenext.R;
import com.example.helpthenext.dataclasses.DAOSupplyRequest;
import com.example.helpthenext.dataclasses.Supply;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EditListofResourcesAdapter extends RecyclerView.Adapter<EditListofResourcesAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Supply> data;
    private DAOSupplyRequest daoSupplyRequest;

    public EditListofResourcesAdapter(Context context,  ArrayList<Supply> data, DAOSupplyRequest daoSupplyRequest)
    {
        this.context = context;
        this.data = data;
        this.daoSupplyRequest = daoSupplyRequest;
    }

    @NonNull
    @Override
    public EditListofResourcesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_of_resources,parent,false);
        return new EditListofResourcesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditListofResourcesAdapter.MyViewHolder holder, int position) {
        Supply app = data.get(position);
        if(app != null){
            holder.resourceName.setText(app.getResourceName());
            holder.quantity.setText(String.valueOf(app.getResourceQuantity()));
            holder.unit.setText(app.getQuantityMeasurement());
        } else{
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }




        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daoSupplyRequest.remove(String.valueOf(position));
                data.remove(position);
                notifyItemRemoved(position);
                //db.child("-N39SWseT1Mazd_C1w_h").removeValue();
            }
        });


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.edit_resources_cardview);

                EditText editResourceName = dialog.findViewById(R.id.editResourceName);
                TextView txtSliderQuantity = dialog.findViewById(R.id.txtSliderQuantity);
                Slider slider = dialog.findViewById(R.id.editQuantitySlider);
                Spinner spinner = dialog.findViewById(R.id.spinnerEditUnit);
                Button submitNewResource = dialog.findViewById(R.id.submitNewResource);

                editResourceName.setText((data.get(position)).getResourceName());
                slider.setValue(Float.parseFloat(String.valueOf((data.get(position)).getResourceQuantity())));
                txtSliderQuantity.setText((data.get(position)).getQuantityMeasurement());




                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.unit_of_measurements, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);


                spinner.setSelection(adapter.getPosition(data.get(position).getQuantityMeasurement()));


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                slider.addOnChangeListener(new Slider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                        txtSliderQuantity.setText(String.valueOf(value));
                        notifyItemChanged(position);
                    }
                });

                submitNewResource.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String spinnerA = "";
                        String nameOfResource = "";
                        String sliderValue = "";

                        if(editResourceName.getText().toString().isEmpty()){
                            Toast.makeText(context, "Teste 1 ", Toast.LENGTH_SHORT).show();
                        } else{
                            nameOfResource = editResourceName.getText().toString();
                        }
                        if(spinner.getSelectedItem().toString().isEmpty()){
                            Toast.makeText(context, "teste 2", Toast.LENGTH_SHORT).show();
                        } else {
                            spinnerA = spinner.getSelectedItem().toString();
                        }
                        if(txtSliderQuantity.getText().toString().isEmpty()){
                            Toast.makeText(context, "teste 3", Toast.LENGTH_SHORT).show();
                        } else{
                            sliderValue = txtSliderQuantity.getText().toString();
                        }

                        data.set(position, new Supply(nameOfResource, Double.parseDouble(sliderValue), spinnerA));
                        notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView resourceName;
        TextView quantity;
        TextView unit;
        CardView linearLayout;
        ImageView deleteItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            resourceName = itemView.findViewById(R.id.resourceName);
            quantity = itemView.findViewById(R.id.quantity);
            unit = itemView.findViewById(R.id.unit);
            linearLayout = itemView.findViewById(R.id.cardViewListOfResources);
            deleteItem = itemView.findViewById(R.id.imageViewDeleteResource);
            deleteItem.setVisibility(View.VISIBLE);
        }
    }

}


