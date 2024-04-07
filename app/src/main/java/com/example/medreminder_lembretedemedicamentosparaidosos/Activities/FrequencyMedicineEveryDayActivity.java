package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class FrequencyMedicineEveryDayActivity extends AppCompatActivity {

    Button buttonOneTimePerDay, buttonTwoTimesPerDay, buttonThreeTimesPerDay, buttonMoreThanThreeTimesPerDay, buttonEarchXhours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_medicine_every_day);

        Intent it = getIntent();
        String medicine = it.getStringExtra("medicine");
        String typeMedicine = it.getStringExtra("typeMedicine");
        String frequencyMedicine = it.getStringExtra("frequencyMedicine");

        buttonOneTimePerDay = findViewById(R.id.buttonOneTimePerDay);
        buttonTwoTimesPerDay = findViewById(R.id.buttonTwoTimesPerDay);
        buttonThreeTimesPerDay = findViewById(R.id.buttonThreeTimesPerDay);
        buttonMoreThanThreeTimesPerDay = findViewById(R.id.buttonMoreThanThreeTimesPerDay);
        buttonEarchXhours = findViewById(R.id.buttonEachXhours);


        buttonOneTimePerDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyTimes", "everyDay");
                it.putExtra("frequencyDay", 1);
                startActivity(it);
            }
        });

        buttonTwoTimesPerDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyTimes", "everyDay");
                it.putExtra("frequencyDay", 2);
                startActivity(it);
            }
        });

        buttonThreeTimesPerDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyTimes", "everyDay");
                it.putExtra("frequencyDay", 3);
                startActivity(it);
            }
        });

        buttonMoreThanThreeTimesPerDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyTimes", "everyDay");
                it.putExtra("frequencyDay", 1);
                startActivity(it);
            }
        });

        buttonEarchXhours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyTimes", "everyDay");
                it.putExtra("frequencyDay", 1);
                startActivity(it);
            }
        });

    }
}