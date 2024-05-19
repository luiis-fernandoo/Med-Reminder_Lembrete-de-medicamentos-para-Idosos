package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
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
    private TextView intervalDays, warningText, whenYouNeed, intervalText, daysText;
    private LinearLayout buttonOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_medicine_every_other_days);

        Intent it = getIntent();
        String medicine = it.getStringExtra("medicine");
        String typeMedicine = it.getStringExtra("typeMedicine");
        String frequencyMedicine = it.getStringExtra("frequencyMedicine");

        intervalText = findViewById(R.id.intervalText);
        intervalText.setText(R.string.the_range_of_days_are);
        daysText = findViewById(R.id.daysText);
        daysText.setText(R.string.days);
        intervalDays = findViewById(R.id.intervalDays);
        calendarView = findViewById(R.id.calendarView);
        buttonConfirmFrequencyDate = findViewById(R.id.buttonConfirmFrequencyDate);
        whenYouNeed = findViewById(R.id.whenYouNeed);
        whenYouNeed.setText(R.string.when_you_need_to_take_the_next_dose);

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
                if(differenceInDays <=0){
                    popup_warning(view);
                }else{
                    Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                    it.putExtra("medicine", medicine);
                    it.putExtra("typeMedicine", typeMedicine);
                    it.putExtra("frequencyMedicine", frequencyMedicine);
                    it.putExtra("frequencyDifferenceDays", differenceInDays);
                    startActivity(it);
                }
            }
        });
    }

    public void popup_warning(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_warnings_layout, null);

        warningText = popupView.findViewById(R.id.warningText);
        warningText.setText("Escolha uma data a frente da data atual, por favor.");

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