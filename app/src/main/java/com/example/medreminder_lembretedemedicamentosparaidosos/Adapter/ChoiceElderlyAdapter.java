package com.example.medreminder_lembretedemedicamentosparaidosos.Adapter;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.AddMedicineActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.ChoiceElderlyActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.SearchMedicineActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.TypeMedicineActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ChoiceElderlyAdapter extends RecyclerView.Adapter<ChoiceElderlyAdapter.MyViewHolder> {

    private Activity activity;
    private List<Elderly> elderlies;
    private SharedPreferences sp;
    public ChoiceElderlyAdapter(List<Elderly> elderlies, Activity activity, SharedPreferences sp) {
        this.elderlies = elderlies;
        this.activity = activity;
        this.sp = sp;
    }

    @NonNull
    @Override
    public ChoiceElderlyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.recycle_elderlys_layout, parent, false);
        return new ChoiceElderlyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position < elderlies.size()) {
            Log.d("", "Size: " + elderlies.size());
            Elderly elderly = elderlies.get(position);
            holder.bind(elderly, this.activity);
        } else {
            Log.e("Adapter", "Índice fora dos limites: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return elderlies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageElderlyChoice, clickToNext;
        TextView textNameElderly, textAgeElderly;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageElderlyChoice = itemView.findViewById(R.id.imageElderlyChoice);
            textNameElderly = itemView.findViewById(R.id.textNameElderly);
            textAgeElderly = itemView.findViewById(R.id.textAgeElderly);
            clickToNext = itemView.findViewById(R.id.clickToNext);
        }

        public void bind(Elderly elderly, Context context) {
            Log.d("", "Entrou " + elderly.getName());
            textNameElderly.setText(elderly.getName());
            textAgeElderly.setText(elderly.getAge());
            if(elderly.getProfile_photo()!=null){
                Glide.with(itemView.getContext())
                        .load(elderly.getProfile_photo())
                        .into(imageElderlyChoice);
            }
            clickToNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("chosenElderly", String.valueOf(elderly.get_id()));
                    editor.apply();
                    Intent it = new Intent(context, SearchMedicineActivity.class);
                    context.startActivity(it);
                }
            });
        }
    }
}
