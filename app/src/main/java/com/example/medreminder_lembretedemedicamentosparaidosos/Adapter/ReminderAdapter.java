package com.example.medreminder_lembretedemedicamentosparaidosos.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.MenuActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.MedicineDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Fragments.ReminderFragment;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Medicine;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {
    private Context context;
    private List<Reminder> reminders;
    private SharedPreferences sp;
    private LinearLayout buttonOk;
    private TextView editTime, textWarning, textRemaining;
    private MenuActivity menuActivity;
    private Button buttonConfirm, buttonCancel;
    private EditText inputRemaining;

    private int layout;

    public ReminderAdapter(List<Reminder> reminders, Context context, SharedPreferences sp, MenuActivity menuActivity) {
        this.reminders = reminders;
        this.context = context;
        this.sp = sp;
        this.menuActivity = menuActivity;
    }

    @NonNull
    @Override
    public ReminderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_reminders_layout, parent, false);
        return new ReminderAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.MyViewHolder holder, int position) {
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
        TextView textNameMedicine, textTime, textDose, textDate;
        Button buttonEdit, buttonDeleteReminder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBox = itemView.findViewById(R.id.imageBox);
            textNameMedicine = itemView.findViewById(R.id.textNameMedicine);
            textTime = itemView.findViewById(R.id.textTime);
            textDose = itemView.findViewById(R.id.textDose);
            textDate = itemView.findViewById(R.id.textDate);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDeleteReminder = itemView.findViewById(R.id.buttonDeleteReminder);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Reminder reminder, Context context) {
            MedicineDao medicineDao = new MedicineDao(context, new Medicine(reminder.getMedicamento_id()));
            Medicine medicine = medicineDao.getMedicineByProcessNumber();
            textNameMedicine.setText(medicine.getProduct_name());
            textDose.setText(context.getString(R.string.dose) + ": " + reminder.getQuantity());
            textTime.setText(context.getString(R.string.time) + ": " + reminder.getTime());
            if(reminder.getDate()!=null){
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfCalendar = new SimpleDateFormat("dd-MM-yyy", new Locale("pt_BR"));
                String dateString = sdfCalendar.format(reminder.getDate());
                textDate.setText(context.getString(R.string.date) + ": " + dateString);
            }else if(reminder.getDayOfWeek() != null){
                textDate.setText(context.getString(R.string.date) + ": " + reminder.getDayOfWeek());
            }else{
                textDate.setText(context.getString(R.string.date) + ": " + context.getString(R.string.every_day));
            }
            if(reminder.getPhoto_medicine_box()!=null){
                Glide.with(itemView.getContext())
                        .load(reminder.getPhoto_medicine_box())
                        .into(imageBox);
            }

            buttonEdit.setText(R.string.edit);
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup_edit(reminder);
                }
            });

            buttonDeleteReminder.setText(R.string.delete);
            buttonDeleteReminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWarningDelete(reminder);
                }
            });
        }
    }

    public void popup_edit(Reminder reminder){
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.pop_up_edit_reminder, null);

        TextView timeEdit = popupView.findViewById(R.id.timeEdit);
        timeEdit.setText(R.string.time);
        editTime = popupView.findViewById(R.id.editTime);
        editTime.setText(reminder.getTime());

        TextView doseEdit = popupView.findViewById(R.id.doseEdit);
        doseEdit.setText(R.string.dose);
        EditText inputDoseEdit = popupView.findViewById(R.id.inputDoseEdit);
        inputDoseEdit.setText(reminder.getQuantity());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(popupView);

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHourClock();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        buttonOk = popupView.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = inputDoseEdit.getText().toString();
                reminder.setQuantity(quantity);
                String time = editTime.getText().toString();
                reminder.setTime(time);

                ReminderDao reminderDao = new ReminderDao(context, reminder);
                if(reminder.getEveryday().equals("S")){
                    if (reminderDao.updateReminderById(reminder)) {
                        Toast.makeText(context, "Lembrete atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        menuActivity.replaceFragment(new ReminderFragment());
                    }else{
                        Toast.makeText(context, "Erro ao atualizar lembrete!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (reminderDao.updateReminder(reminder)) {
                        Toast.makeText(context, "Lembrete atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        menuActivity.replaceFragment(new ReminderFragment());
                    }else{
                        Toast.makeText(context, "Erro ao atualizar lembrete!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public void selectHourClock(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                String formattedHour = String.format(Locale.getDefault(), "%02d", hourOfDay);
                String formattedMinute = String.format(Locale.getDefault(), "%02d", minute);
                String hourReminder = formattedHour + ":" + formattedMinute;
                editTime.setText(hourReminder);
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);

        timePickerDialog.show();
    }

    public void popupWarningDelete(Reminder reminder){
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.popup_warnings_delete, null);

        textWarning = popupView.findViewById(R.id.textWarning);
        textWarning.setText(R.string.textWarning);
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
                ReminderDao reminderDao = new ReminderDao(context, reminder);
                if (reminderDao.deleteReminderByMedicineId(reminder.getMedicamento_id())) {
                    Toast.makeText(context, "Lembrete deletado com sucesso!", Toast.LENGTH_SHORT).show();
                    menuActivity.replaceFragment(new ReminderFragment());
                }else {
                    Toast.makeText(context, "Erro ao deletar lembrete!", Toast.LENGTH_SHORT).show();
                }
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
