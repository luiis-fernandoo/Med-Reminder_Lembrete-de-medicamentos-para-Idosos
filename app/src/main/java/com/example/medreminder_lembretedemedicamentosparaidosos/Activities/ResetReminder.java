package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ScheduleItem;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ResetReminder extends AppCompatActivity {

    private String remainingPills, warningPills, medicine, typeMedicine,dayOfWeek, frequencyMedicine, currentPhotoPathOne, currentPhotoPathTwo, everyday, time, quantity;
    private Date date;
    private ArrayList<String> selectedButtonTexts;
    private ArrayList<ScheduleItem> scheduleItems;
    private int idoso_id, frequencyDifferenceDays;
    private EditText inputRemainingPills, inputWarningPills;
    private Button buttonNext, buttonSkip;
    private TextView warningText;
    private LinearLayout buttonOk;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_reminder);

        Intent it = getIntent();
        medicine = it.getStringExtra("medicine");
        typeMedicine = it.getStringExtra("typeMedicine");
        frequencyMedicine = it.getStringExtra("frequencyMedicine");
        if(frequencyMedicine != null){
            if (frequencyMedicine.equals("everyDay")) {
                everyday = "S";
            }else if(frequencyMedicine.equals("everyOtherDay")){
                everyday = "N";
                frequencyDifferenceDays = it.getIntExtra("frequencyDifferenceDays", -1);
            }else if(frequencyMedicine.equals("specificDay")){
                everyday = "N";
                selectedButtonTexts = it.getStringArrayListExtra("selectedButtonTexts");
            }
        }

        scheduleItems = it.getParcelableArrayListExtra("timeAndQuantity");
        currentPhotoPathOne = it.getStringExtra("currentPhotoPathOne");
        currentPhotoPathTwo = it.getStringExtra("currentPhotoPathTwo");

        sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        String typeUser = sp.getString("selectedUserType", "");
        Log.d("", "Select" + typeUser);
        if(typeUser.equals("Idoso")){
            ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), new Elderly());
            if(sp.getString("Guest", "").equals("Convidado")){
                Elderly elderly = elderlyDao.getElderlyByName("Convidado");
                idoso_id = elderly.get_id();
                Log.d("","Entrou no errado: " + idoso_id);
            }else{
                String email = sp.getString("email", "");
                Elderly elderly = elderlyDao.getElderlyByEmail(email);
                idoso_id = elderly.get_id();
                Log.d("","Entrou no certo: " + idoso_id);
            }
        }else{
            if(sp.getInt("chosenElderlyById", 0) != 0){
                idoso_id = sp.getInt("chosenElderlyById", 0);
            }else if(sp.getString("chosenElderly", null) != null){
                String name = sp.getString("chosenElderly", null);
                ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), new Elderly());
                Elderly elderly = elderlyDao.getElderlyByName(name);
                idoso_id = elderly.get_id();
            }
        }

        inputRemainingPills = findViewById(R.id.inputMoreThanThreeTimes);
        inputWarningPills = findViewById(R.id.inputWarningPills);
        buttonNext = findViewById(R.id.buttonNext);
        buttonSkip = findViewById(R.id.buttonSkip);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remainingPills = inputRemainingPills.getText().toString();
                warningPills = inputWarningPills.getText().toString();

                int remaining = Integer.parseInt(remainingPills);
                int warning = Integer.parseInt(warningPills);
                if(remaining < 1 || warning < 0){
                    popup_warning(view);
                }else{
                    registerReminder();
                }
            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerReminder();
            }
        });
    }

    public void popup_warning(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_warnings_layout, null);

        warningText = popupView.findViewById(R.id.warningText);
        warningText.setText("Valor inserido invalido!");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(popupView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        buttonOk = popupView.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void registerReminder(){
        if(everyday.equals("S")) {
            try {
                for (int i = 0; i < scheduleItems.size(); i++) {
                    time = scheduleItems.get(i).getTime();
                    quantity = scheduleItems.get(i).getQuantity();
                    ReminderDao reminderDao = new ReminderDao(getApplicationContext(), new Reminder(
                            idoso_id,
                            medicine, typeMedicine,
                            everyday, time,
                            date, quantity,
                            dayOfWeek, remainingPills,
                            warningPills, currentPhotoPathOne,
                            currentPhotoPathTwo));
                    reminderDao.insertNewReminder();
                }
                Intent it = new Intent(ResetReminder.this, SucessSaveReminder.class);
                startActivity(it);
            }catch (Exception e){
                Log.d("", "Erro: " + e);
                Toast.makeText(this, "Não foi possível cadastrar o medicamento, tente novamente", Toast.LENGTH_SHORT).show();
            }

        }else if(everyday.equals("N") && frequencyDifferenceDays > 0){
            Calendar nextDoseDate = Calendar.getInstance();
            try {
                for (int i=0; i<15; i++){
                    nextDoseDate.add(Calendar.DAY_OF_MONTH, + (frequencyDifferenceDays + 1));
                    while (nextDoseDate.get(Calendar.DAY_OF_MONTH) > 28 &&
                            nextDoseDate.get(Calendar.DAY_OF_MONTH) < 31) {
                        nextDoseDate.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    date = nextDoseDate.getTime();
                    time = scheduleItems.get(0).getTime();
                    quantity = scheduleItems.get(0).getQuantity();
                    ReminderDao reminderDao = new ReminderDao(getApplicationContext(), new Reminder(
                            idoso_id,
                            medicine, typeMedicine,
                            everyday, time,
                            date, quantity,
                            dayOfWeek, remainingPills,
                            warningPills, currentPhotoPathOne,
                            currentPhotoPathTwo));
                    reminderDao.insertNewReminder();
                }
                Intent it = new Intent(ResetReminder.this, SucessSaveReminder.class);
                startActivity(it);
            }catch (Exception e){
                Log.d("", "Erro: " + e);
                Toast.makeText(this, "Não foi possível cadastrar o medicamento, tente novamente", Toast.LENGTH_SHORT).show();
            }
        }else if(everyday.equals("N") && selectedButtonTexts != null){
            try {
                for (int i=0; i<selectedButtonTexts.size(); i++){
                    time = scheduleItems.get(0).getTime();
                    quantity = scheduleItems.get(0).getQuantity();
                    dayOfWeek = selectedButtonTexts.get(i);
                            ReminderDao reminderDao = new ReminderDao(getApplicationContext(), new Reminder(
                            idoso_id,
                            medicine, typeMedicine,
                            everyday, time,
                            date, quantity,
                            dayOfWeek, remainingPills,
                            warningPills, currentPhotoPathOne,
                            currentPhotoPathTwo));
                    reminderDao.insertNewReminder();
                }
                Intent it = new Intent(ResetReminder.this, SucessSaveReminder.class);
                startActivity(it);
            }catch (Exception e){
                Log.d("", "Erro: " + e);
                Toast.makeText(this, "Não foi possível cadastrar o medicamento, tente novamente", Toast.LENGTH_SHORT).show();
            }
        }
    }
}