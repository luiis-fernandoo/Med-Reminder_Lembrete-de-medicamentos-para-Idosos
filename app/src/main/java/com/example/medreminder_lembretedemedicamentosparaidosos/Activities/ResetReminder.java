package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ScheduleItem;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.ArrayList;

public class ResetReminder extends AppCompatActivity {

    private String remainingPills, warningPills, medicine, typeMedicine, frequencyMedicine, currentPhotoPathOne, currentPhotoPathTwo;
    private ArrayList<String> selectedButtonTexts;
    private ArrayList<ScheduleItem> scheduleItems;
    private int frequencyDay, frequencyDifferenceDays;
    private EditText inputRemainingPills, inputWarningPills;
    private Button buttonNext, buttonSkip;
    private TextView warningText;
    private LinearLayout buttonOk;

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
                frequencyDay = it.getIntExtra("frequencyDay", 1);
            }else if(frequencyMedicine.equals("everyOtherDay")){
                frequencyDifferenceDays = it.getIntExtra("frequencyDifferenceDays", -1);
                Log.d("", "PhotoPackaging "+ frequencyDifferenceDays);
            }else if(frequencyMedicine.equals("specificDay")){
                selectedButtonTexts = it.getStringArrayListExtra("selectedButtonTexts");
            }
        }
        scheduleItems = it.getParcelableArrayListExtra("timeAndQuantity");
        currentPhotoPathOne = it.getStringExtra("currentPhotoPathOne");
        currentPhotoPathTwo = it.getStringExtra("currentPhotoPathTwo");

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
                    Intent it = new Intent(ResetReminder.this, SucessSaveReminder.class);
                    it.putExtra("medicine", medicine);
                    it.putExtra("typeMedicine", typeMedicine);
                    it.putExtra("frequencyMedicine", frequencyMedicine);
                    if(frequencyMedicine != null){
                        if (frequencyMedicine.equals("everyDay")) {
                            it.putExtra("frequencyDay", frequencyDay);
                        }else if(frequencyMedicine.equals("everyOtherDay")){
                            it.putExtra("frequencyDifferenceDays", frequencyDifferenceDays);
                        }else if(frequencyMedicine.equals("specificDay")){
                            it.putExtra("selectedButtonTexts", selectedButtonTexts);
                        }
                    }
                    it.putExtra("timeAndQuantity", scheduleItems);
                    it.putExtra("currentPhotoPathOne", currentPhotoPathOne);
                    it.putExtra("currentPhotoPathTwo", currentPhotoPathTwo);
                    it.putExtra("remainingPills", remainingPills);
                    it.putExtra("warningPills", warningPills);

                    startActivity(it);
                }
            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ResetReminder.this, SucessSaveReminder.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                if(frequencyMedicine != null){
                    if (frequencyMedicine.equals("everyDay")) {
                        it.putExtra("frequencyDay", frequencyDay);
                    }else if(frequencyMedicine.equals("everyOtherDay")){
                        it.putExtra("frequencyDifferenceDays", frequencyDifferenceDays);
                    }else if(frequencyMedicine.equals("specificDay")){
                        it.putExtra("selectedButtonTexts", selectedButtonTexts);
                    }
                }
                it.putExtra("timeAndQuantity", scheduleItems);
                it.putExtra("currentPhotoPathOne", currentPhotoPathOne);
                it.putExtra("currentPhotoPathTwo", currentPhotoPathTwo);
                startActivity(it);
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
}