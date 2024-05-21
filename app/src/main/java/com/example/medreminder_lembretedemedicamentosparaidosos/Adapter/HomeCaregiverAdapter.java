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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.MenuActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.SearchMedicineActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.MedicineDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Fragments.HomeFragment;
import com.example.medreminder_lembretedemedicamentosparaidosos.Fragments.ReminderFragment;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Medicine;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.List;

public class HomeCaregiverAdapter extends RecyclerView.Adapter<HomeCaregiverAdapter.MyViewHolder> {
    private Context context;
    private List<Reminder> reminders;
    private SharedPreferences sp;
    private MenuActivity menuActivity;
    private TextView textWarning;
    private Button buttonConfirm, buttonCancel;
    public HomeCaregiverAdapter(List<Reminder> reminders, Context context, SharedPreferences sp, MenuActivity menuActivity) {
        this.reminders = reminders;
        this.context = context;
        this.sp = sp;
        this.menuActivity = menuActivity;
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
        TextView textNameMedicine, textTime, textElderly, textDose, textStatus;
        Button buttonCancelDose;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBox = itemView.findViewById(R.id.imageBox);
            textNameMedicine = itemView.findViewById(R.id.textNameMedicine);
            textTime = itemView.findViewById(R.id.textTime);
            textElderly = itemView.findViewById(R.id.textElderly);
            textDose = itemView.findViewById(R.id.textDose);
            textStatus = itemView.findViewById(R.id.textStatus);
            buttonCancelDose = itemView.findViewById(R.id.buttonCancelDose);

        }

        @SuppressLint("SetTextI18n")
        public void bind(Reminder reminder, Context context) {
            MedicineDao medicineDao = new MedicineDao(context, new Medicine(reminder.getMedicamento_id()));
            Medicine medicine = medicineDao.getMedicineByProcessNumber();
            ElderlyDao elderlyDao = new ElderlyDao(context, new Elderly());
            Elderly elderly = elderlyDao.getElderlyById(reminder.getIdoso_id());
            textElderly.setText(context.getString(R.string.elderly) + " " + elderly.getName());
            textNameMedicine.setText(medicine.getProduct_name());
            textTime.setText(context.getString(R.string.time) + " " + reminder.getTime());
            textDose.setText(context.getString(R.string.dose) + " " + reminder.getQuantity());
            if(reminder.getStatus()==10) {
                textStatus.setText(context.getString(R.string.status) + " " + context.getString(R.string.statusConfirmed));
            }else if(reminder.getStatus()==11) {
                textStatus.setText(context.getString(R.string.status) + " " + context.getString(R.string.statusCanceled));
            }else if(reminder.getStatus()==1) {
                textStatus.setText(context.getString(R.string.status) + " " + context.getString(R.string.statusSet));
            }else if(reminder.getStatus()==0){
                textStatus.setText(context.getString(R.string.status) + ": " + context.getString(R.string.statusToBeSet));
            }

            buttonCancelDose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWarningCancel(reminder);
                }
            });
            if(reminder.getPhoto_medicine_box()!=null){
                Glide.with(itemView.getContext())
                        .load(reminder.getPhoto_medicine_box())
                        .into(imageBox);
            }
        }
    }

    public void popupWarningCancel(Reminder reminder){
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.popup_warnings_delete, null);

        textWarning = popupView.findViewById(R.id.textWarning);
        textWarning.setText(R.string.textWarningCancel);
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(popupView);

        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        buttonConfirm = popupView.findViewById(R.id.buttonConfirm);
        buttonConfirm.setText(R.string.confirm);

        buttonCancel = popupView.findViewById(R.id.buttonCancel);
        buttonCancel.setText(R.string.cancel);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReminderDao reminderDao = new ReminderDao(context, new Reminder());
                reminderDao.setStatusAlarm(11, reminder.get_id());
                menuActivity.replaceFragment(new HomeFragment());
                Toast.makeText(context, "Dose do medicamento cancelada com sucesso", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}
