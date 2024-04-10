package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ScheduleItem;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.ArrayList;

public class ResetReminder extends AppCompatActivity {

    private String remainingPills, warningPills, medicine, typeMedicine, frequencyMedicine, frequencyTimes, currentPhotoPathOne, currentPhotoPathTwo;
    private ArrayList<String> selectedButtonTexts;
    private ArrayList<ScheduleItem> scheduleItems;
    private int frequencyDay, frequencyDifferenceDays;
    private EditText inputRemainingPills, inputWarningPills;
    private Button buttonNext, buttonSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_reminder);



        Intent it = getIntent();
        medicine = it.getStringExtra("medicine");
        typeMedicine = it.getStringExtra("typeMedicine");
        frequencyMedicine = it.getStringExtra("frequencyMedicine");
        frequencyTimes = it.getStringExtra("frequencyTimes");
        if(frequencyTimes != null){
            if (frequencyTimes.equals("everyDay")) {
                frequencyDay = it.getIntExtra("frequencyDay", 1);
            }else if(frequencyTimes.equals("everyOtherDay")){
                frequencyDifferenceDays = it.getIntExtra("frequencyDifferenceDays", -1);
            }else if(frequencyTimes.equals("specificDays")){
                selectedButtonTexts = it.getStringArrayListExtra("selectedButtonTexts");
            }
        }
        scheduleItems = it.getParcelableArrayListExtra("timeAndQuantity");
        currentPhotoPathOne = it.getStringExtra("currentPhotoPathOne");
        currentPhotoPathTwo = it.getStringExtra("currentPhotoPathTwo");

        inputRemainingPills = findViewById(R.id.inputRemainingPills);
        inputWarningPills = findViewById(R.id.inputWarningPills);
        buttonNext = findViewById(R.id.buttonNext);
        buttonSkip = findViewById(R.id.buttonSkip);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remainingPills = inputRemainingPills.getText().toString();
                warningPills = inputWarningPills.getText().toString();

                Intent it = new Intent(ResetReminder.this, MenuActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyTimes", frequencyTimes);
                if(frequencyTimes != null){
                    if (frequencyTimes.equals("everyDay")) {
                        it.putExtra("frequencyDay", frequencyDay);
                    }else if(frequencyTimes.equals("everyOtherDay")){
                        it.putExtra("frequencyDifferenceDays", frequencyDifferenceDays);
                    }else if(frequencyTimes.equals("specificDays")){
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
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}