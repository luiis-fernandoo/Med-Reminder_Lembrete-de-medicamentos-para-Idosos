package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FrequencyMedicineEveryOtherDaysActivity extends AppCompatActivity {

    private String selectedDateFormatted;
    private int differenceInDays;
    CalendarView calendarView;
    Button buttonConfirmFrequencyDate;
    private TextView intervalDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_medicine_every_other_days);

        Intent it = getIntent();
        String medicine = it.getStringExtra("medicine");
        String typeMedicine = it.getStringExtra("typeMedicine");
        String frequencyMedicine = it.getStringExtra("frequencyMedicine");

        intervalDays = findViewById(R.id.intervalDays);
        calendarView = findViewById(R.id.calendarView);
        buttonConfirmFrequencyDate = findViewById(R.id.buttonConfirmFrequencyDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                long differenceInMillis = selectedDate.getTimeInMillis() - System.currentTimeMillis();

                differenceInDays = (int) (differenceInMillis / (1000 * 60 * 60 * 24));

                intervalDays.setText(String.valueOf(differenceInDays));
            }
        });
        buttonConfirmFrequencyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), FrequencyMedicineSpecificDaysActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyTimes", "everyOtherDay");
                it.putExtra("frequencyDifferenceDays", differenceInDays);
                startActivity(it);
            }
        });
    }
}