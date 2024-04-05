package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FrequencyMedicineEveryOtherDaysActivity extends AppCompatActivity {

    private String selectedDateFormatted;
    CalendarView calendarView;
    Button buttonConfirmFrequencyDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_medicine_every_other_days);

        calendarView = findViewById(R.id.calendarView);
        buttonConfirmFrequencyDate = findViewById(R.id.buttonConfirmFrequencyDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                Date selectedDateAsDate = selectedDate.getTime();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
                selectedDateFormatted = dateFormat.format(selectedDateAsDate);

                Toast.makeText(FrequencyMedicineEveryOtherDaysActivity.this, "Data selecionada: " + selectedDateFormatted.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        buttonConfirmFrequencyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}