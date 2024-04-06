package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class FrequencyMedicineActivity extends AppCompatActivity {

    private Button buttonFrequencyEveryDay, buttonFrequencyEveryOtherDay, buttonFrequencySpecificDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_medicine);

        Intent it = getIntent();
        String medicine = it.getStringExtra("medicine");
        String typeMedicine = it.getStringExtra("typeMedicine");

        buttonFrequencyEveryDay = findViewById(R.id.buttonFrequencyEveryDay);
        buttonFrequencyEveryOtherDay = findViewById(R.id.buttonFrequencyEveryOtherDay);
        buttonFrequencySpecificDay = findViewById(R.id.buttonFrequencySpecificDay);

        buttonFrequencyEveryDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), FrequencyMedicineEveryDayActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", "everyDay");
                startActivity(it);
            }
        });

        buttonFrequencyEveryOtherDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), FrequencyMedicineEveryOtherDaysActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", "everyOtherDay");
                startActivity(it);
            }
        });

        buttonFrequencySpecificDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), FrequencyMedicineSpecificDaysActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", "specificDay");
                startActivity(it);
            }
        });
    }
}