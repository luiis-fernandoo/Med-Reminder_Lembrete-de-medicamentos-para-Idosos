package com.example.medreminder_lembretedemedicamentosparaidosos.Adapter;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.TypeMedicineActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ChoiceElderlyAdapter extends RecyclerView.Adapter<ChoiceElderlyAdapter.MyViewHolder> {

    private Activity activity;
    private List<Elderly> elderlies;
    public void setElderlies(List<Elderly> elderlies) {
        this.elderlies = elderlies;
    }

    public ChoiceElderlyAdapter(Activity activity) {
        this.activity = activity;
    }
    @NonNull
    @Override
    public ChoiceElderlyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.recycle_elderlys_layout, parent, false);

        return new ChoiceElderlyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Elderly elderly = elderlies.get(position);
        holder.bind(elderly, this.activity);
    }

    @Override
    public int getItemCount() {
        return elderlies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageElderlyChoice;
        TextView textNameElderly, textAgeElderly;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageElderlyChoice = itemView.findViewById(R.id.imageElderlyChoice);
            textNameElderly = itemView.findViewById(R.id.textNameElderly);
            textAgeElderly = itemView.findViewById(R.id.textAgeElderly);
        }

        public void bind(Elderly elderly, Context context) {

        }
    }
}
