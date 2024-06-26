package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

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

import com.example.medreminder_lembretedemedicamentosparaidosos.Broadcast.MyWorker;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyCaregiverDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.InsertLogHelper;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ScheduleItem;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ResetReminder extends AppCompatActivity {

    private String remainingPills, warningPills, medicine, typeMedicine,dayOfWeek, frequencyMedicine, currentPhotoPathOne, currentPhotoPathTwo, everyday, time, quantity;
    private Date date;
    private ArrayList<String> selectedButtonTexts;
    private ArrayList<ScheduleItem> scheduleItems;
    private int idoso_id, frequencyDifferenceDays, cuidador_id = 0, eachXhours;
    private EditText inputRemainingPills, inputWarningPills;
    private Button buttonNext, buttonSkip, buttonHelp;
    private TextView warningText, whichReminder, howManyLeft, whenWarning;
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
                if(it.getIntExtra("eachXhours", 0) != 0){
                    eachXhours = it.getIntExtra("eachXhours", 0);
                }
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
        if(typeUser.equals("Idoso")){
            ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), new Elderly());
            if(sp.getString("Guest", "").equals("Convidado")){
                Elderly elderly = elderlyDao.getElderlyByName("Convidado");
                idoso_id = elderly.get_id();
            }else{
                String email = sp.getString("email", "");
                Elderly elderly = elderlyDao.getElderlyByEmail(email);
                idoso_id = elderly.get_id();
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
            String email = sp.getString("email", null);
            ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), new ElderlyCaregiver());
            ElderlyCaregiver elderlyCaregiver = elderlyCaregiverDao.getElderlyCaregiver(email);
            cuidador_id = elderlyCaregiver.get_id();
        }

        if(!typeMedicine.equals("pill")){
            registerReminder();
            Intent intent = new Intent(this, SucessSaveReminder.class);
            startActivity(intent);
        }

        inputRemainingPills = findViewById(R.id.inputNameEdit);

        inputWarningPills = findViewById(R.id.inputWarningPills);

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setText(R.string.next);

        buttonSkip = findViewById(R.id.buttonSkip);
        buttonSkip.setText(R.string.skip);

        whichReminder = findViewById(R.id.whichReminder);
        whichReminder.setText(R.string.Do_you_want_to_set_a_refill_reminder_for_your_medicine);

        whenWarning = findViewById(R.id.whenWarning);
        whenWarning.setText(R.string.How_many_pills_do_you_want_to_be_notified_with);

        howManyLeft = findViewById(R.id.howManyLeft);
        howManyLeft.setText(R.string.How_many_pills_are_left);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remainingPills = inputRemainingPills.getText().toString();
                warningPills = inputWarningPills.getText().toString();

                int remaining = Integer.parseInt(remainingPills);
                int warning = Integer.parseInt(warningPills);
                if(remainingPills.equals("") || warningPills.equals("")) {
                    popup_warning(view);
                }else if(remaining < 1 || warning < 0){
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

        buttonHelp = findViewById(R.id.buttonHelp);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupHelp();
            }
        });
    }

    public void popup_warning(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_warnings_layout, null);

        warningText = popupView.findViewById(R.id.warningText);
        warningText.setText("Insira um valor válido");
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
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        if(everyday.equals("S")) {
            try {
                if(eachXhours != 0){
                    DateTimeFormatter formatter = null;
                    time = scheduleItems.get(0).getTime();
                    quantity = scheduleItems.get(0).getQuantity();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        formatter = DateTimeFormatter.ofPattern("HH:mm");
                        LocalTime timeReal = LocalTime.parse(time, formatter);
                        for (int i = 0; i < 24; i += eachXhours) {
                            LocalTime newTime = timeReal.plusHours(i);
                            String timeString = newTime.format(formatter);

                            ReminderDao reminderDaoTwo = new ReminderDao(getApplicationContext(), new Reminder(
                                    idoso_id, cuidador_id,
                                    medicine, typeMedicine,
                                    everyday, timeString,
                                    date, quantity,
                                    dayOfWeek, remainingPills,
                                    warningPills, currentPhotoPathOne,
                                    currentPhotoPathTwo));
                            reminderDaoTwo.insertNewReminder();
                        }
                    }
                }else{
                    for (int i = 0; i < scheduleItems.size(); i++) {
                        time = scheduleItems.get(i).getTime();
                        quantity = scheduleItems.get(i).getQuantity();
                        ReminderDao reminderDao = new ReminderDao(getApplicationContext(), new Reminder(
                                idoso_id, cuidador_id,
                                medicine, typeMedicine,
                                everyday, time,
                                date, quantity,
                                dayOfWeek, remainingPills,
                                warningPills, currentPhotoPathOne,
                                currentPhotoPathTwo));
                        reminderDao.insertNewReminder();
                    }
                }
                workManager.enqueue(new OneTimeWorkRequest.Builder(MyWorker.class).build());
                InsertLogHelper.i("Success_register_reminder" , "Sucess on register for time: " + time);
                Intent it = new Intent(ResetReminder.this, SucessSaveReminder.class);
                startActivity(it);
            }catch (Exception e){
                InsertLogHelper.i("Error_register_reminder" , "Error: " + e);
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
                            idoso_id, cuidador_id,
                            medicine, typeMedicine,
                            everyday, time,
                            date, quantity,
                            dayOfWeek, remainingPills,
                            warningPills, currentPhotoPathOne,
                            currentPhotoPathTwo));
                    reminderDao.insertNewReminder();
                }
                workManager.enqueue(new OneTimeWorkRequest.Builder(MyWorker.class).build());
                InsertLogHelper.i("Success_register_reminder" , "Sucess on register for time: " + time);
                Intent it = new Intent(ResetReminder.this, SucessSaveReminder.class);
                startActivity(it);
            }catch (Exception e){
                InsertLogHelper.i("Error_register_reminder" , "Error: " + e);
                Toast.makeText(this, "Não foi possível cadastrar o medicamento, tente novamente", Toast.LENGTH_SHORT).show();
            }
        }else if(everyday.equals("N") && selectedButtonTexts != null){
            try {
                for (int i=0; i<selectedButtonTexts.size(); i++){
                    time = scheduleItems.get(0).getTime();
                    quantity = scheduleItems.get(0).getQuantity();
                    dayOfWeek = selectedButtonTexts.get(i).toLowerCase();
                            ReminderDao reminderDao = new ReminderDao(getApplicationContext(), new Reminder(
                            idoso_id, cuidador_id,
                            medicine, typeMedicine,
                            everyday, time,
                            date, quantity,
                            dayOfWeek, remainingPills,
                            warningPills, currentPhotoPathOne,
                            currentPhotoPathTwo));
                    reminderDao.insertNewReminder();
                }
                workManager.enqueue(new OneTimeWorkRequest.Builder(MyWorker.class).build());
                InsertLogHelper.i("Success_register_reminder" , "Sucess on register for time: " + time);
                Intent it = new Intent(ResetReminder.this, SucessSaveReminder.class);
                startActivity(it);
            }catch (Exception e){
                InsertLogHelper.i("Error_register_reminder" , "Error: " + e);
                Toast.makeText(this, "Não foi possível cadastrar o medicamento, tente novamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void popupHelp(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_help, null);

        TextView textHelp = popupView.findViewById(R.id.textHelp);
        textHelp.setText(R.string.textHelpReset);

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setView(popupView);

        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button buttonOk = popupView.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}