package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class FrequencyMedicineEveryDayActivity extends AppCompatActivity {

    Button buttonOneTimePerDay, buttonTwoTimesPerDay, buttonThreeTimesPerDay, buttonMoreThanThreeTimesPerDay, buttonEarchXhours;
    TextView howManyTimesYouTakeTheMedicine;
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
        howManyTimesYouTakeTheMedicine = findViewById(R.id.howManyTimesYouTakeTheMedicine);

        howManyTimesYouTakeTheMedicine.setText(R.string.How_often_do_you_take_this_medicine_per_day);

        buttonOneTimePerDay.setText(R.string.one_time_per_day);
        buttonOneTimePerDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyDay", 1);
                startActivity(it);
            }
        });

        buttonTwoTimesPerDay.setText(R.string.two_times_per_day);
        buttonTwoTimesPerDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyDay", 2);
                startActivity(it);
            }
        });

        buttonThreeTimesPerDay.setText(R.string.three_times);
        buttonThreeTimesPerDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyDay", 3);
                startActivity(it);
            }
        });

        buttonMoreThanThreeTimesPerDay.setText(R.string.more_than_three_times_per_day);
        buttonMoreThanThreeTimesPerDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), MoreThanThreeTimesActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyDay", 1);
                startActivity(it);
            }
        });

        buttonEarchXhours.setText(R.string.each_hours);
        buttonEarchXhours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), EachXhoursActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                startActivity(it);
            }
        });

    }
}