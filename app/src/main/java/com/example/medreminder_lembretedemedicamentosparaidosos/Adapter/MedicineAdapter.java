package com.example.medreminder_lembretedemedicamentosparaidosos.Adapter;

import android.annotation.SuppressLint;
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
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.DetailsMedicineActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.MedicineDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Medicine;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {
    private Context context;
    private List<Medicine> medicines;
    private SharedPreferences sp;
    public MedicineAdapter(List<Medicine> medicines, Context context, SharedPreferences sp) {
        this.medicines = medicines;
        this.context = context;
        this.sp = sp;
    }

    @NonNull
    @Override
    public MedicineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medice_layout, parent, false);
        return new MedicineAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineAdapter.MyViewHolder holder, int position) {
        if (position < medicines.size()) {
            Medicine medicine = medicines.get(position);
            holder.bind(medicine, this.context);
        } else {
            Log.e("Adapter", "Índice fora dos limites: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBox, details;
        TextView textNameMedicine;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBox = itemView.findViewById(R.id.imageBox);
            textNameMedicine = itemView.findViewById(R.id.textNameMedicine);
            details = itemView.findViewById(R.id.details);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Medicine medicine, Context context) {
            ReminderDao reminderDao = new ReminderDao(context, new Reminder());
            Reminder reminder = reminderDao.getPhotoByMedicine(medicine.getProcess_number());
            textNameMedicine.setText(medicine.getProduct_name());
            if(reminder.getPhoto_medicine_box()!=null){
                Glide.with(itemView.getContext())
                        .load(reminder.getPhoto_medicine_box())
                        .into(imageBox);
            }
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(context, DetailsMedicineActivity.class);
                    it.putExtra("numProcesso", medicine.getProcess_number());
                    context.startActivity(it);
                }
            });
        }
    }
}
