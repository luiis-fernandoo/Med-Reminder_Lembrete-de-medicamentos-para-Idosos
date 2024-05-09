package com.example.medreminder_lembretedemedicamentosparaidosos.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.SearchMedicineActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.MedicineDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Medicine;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.List;

public class HomeCaregiverAdapter extends RecyclerView.Adapter<HomeCaregiverAdapter.MyViewHolder> {
    private Context context;
    private List<Reminder> reminders;
    private SharedPreferences sp;
    public HomeCaregiverAdapter(List<Reminder> reminders, Context context, SharedPreferences sp) {
        this.reminders = reminders;
        this.context = context;
        this.sp = sp;
    }

    @NonNull
    @Override
    public HomeCaregiverAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_home_caregiver_layout, parent, false);
        return new HomeCaregiverAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCaregiverAdapter.MyViewHolder holder, int position) {
        if (position < reminders.size()) {
            Log.d("", "Size: " + reminders.size());
            Reminder reminder = reminders.get(position);
            holder.bind(reminder, this.context);
        } else {
            Log.e("Adapter", "Índice fora dos limites: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout clickToNext;
        ImageView imageBox;
        TextView textNameMedicine, textTime, textElderly;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBox = itemView.findViewById(R.id.imageBox);
            textNameMedicine = itemView.findViewById(R.id.textNameMedicine);
            textTime = itemView.findViewById(R.id.textTime);
            textElderly = itemView.findViewById(R.id.textElderly);
            clickToNext = itemView.findViewById(R.id.clickToNext);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Reminder reminder, Context context) {
            MedicineDao medicineDao = new MedicineDao(context, new Medicine(reminder.getMedicamento_id()));
            Medicine medicine = medicineDao.getMedicineByProcessNumber();
            ElderlyDao elderlyDao = new ElderlyDao(context, new Elderly());
            Elderly elderly = elderlyDao.getElderlyById(reminder.getIdoso_id());
            textElderly.setText("Idoso: " + elderly.getName());
            textNameMedicine.setText(medicine.getProduct_name());
            textTime.setText("Horário: " + reminder.getTime());
            if(reminder.getPhoto_medicine_box()!=null){
                Glide.with(itemView.getContext())
                        .load(reminder.getPhoto_medicine_box())
                        .into(imageBox);
            }
            clickToNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("", "Oi");
                }
            });
        }
    }
}
