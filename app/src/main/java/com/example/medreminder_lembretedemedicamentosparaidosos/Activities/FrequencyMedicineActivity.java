package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class FrequencyMedicineActivity extends AppCompatActivity {

    private Button buttonFrequencyEveryDay, buttonFrequencyEveryOtherDay, buttonFrequencySpecificDay;
    private TextView howFrequency;
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
        howFrequency = findViewById(R.id.howFrequency);
        howFrequency.setText(R.string.How_often_do_you_take_this_medicine);

        buttonFrequencyEveryDay.setText(R.string.every_day);
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

        buttonFrequencyEveryOtherDay.setText(R.string.every_other_day);
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

        buttonFrequencySpecificDay.setText(R.string.specific_days);
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