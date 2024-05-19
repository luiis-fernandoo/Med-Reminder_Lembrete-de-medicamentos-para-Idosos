package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ScheduleItem;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SetScheduleActivity extends AppCompatActivity {

    private TextView selectType, quantity, selectHour,textDose, warningText;
    private ImageView selectQuantity;
    private String valueQuantity = String.valueOf(1), hourReminder, typeMedicine, medicine, frequencyMedicine;
    private LinearLayout buttonOk;
    private Button buttonNext;
    private ArrayList<String> selectedButtonTexts;
    private int count = 1, frequencyDay, frequencyDifferenceDays, eachXhours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);
        Intent it = getIntent();
        medicine = it.getStringExtra("medicine");
        typeMedicine = it.getStringExtra("typeMedicine");
        frequencyMedicine = it.getStringExtra("frequencyMedicine");
        if (frequencyMedicine.equals("everyDay")) {
            frequencyDay = it.getIntExtra("frequencyDay", 1);
            if(it.getIntExtra("eachXhours", 0) != 0){
                eachXhours = it.getIntExtra("eachXhours", 0);
            }
        }else if(frequencyMedicine.equals("everyOtherDay")){
            frequencyDifferenceDays = it.getIntExtra("frequencyDifferenceDays", -1);
        }else if(frequencyMedicine.equals("specificDay")){
            selectedButtonTexts = it.getStringArrayListExtra("selectedButtonTexts");
        }

        selectQuantity = findViewById(R.id.selectQuantity);
        quantity = findViewById(R.id.quantity);

        selectType = findViewById(R.id.selectType);

        selectHour = findViewById(R.id.selectHour);

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setText(R.string.next);
        textDose = findViewById(R.id.textDose);
        textDose.setText(R.string.Select_the_time_of_the_first_dose);


        switch (typeMedicine) {
            case "pill":
                selectType.setText(R.string.pill);
                break;
            case "drops":
                selectType.setText(R.string.drops);
                break;
            case "dust":
                selectType.setText(R.string.dust);
                break;
        }

        ArrayList<ScheduleItem> scheduleItems = new ArrayList<>();


        selectQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_quantity(view);
            }
        });

        selectHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHourClock();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                scheduleItems.add(new ScheduleItem(hourReminder, valueQuantity));
                if(count == frequencyDay){
                    intentEveryDay(scheduleItems);
                }else if(frequencyDifferenceDays != 0 && frequencyDifferenceDays != -1){
                    intentEveryOtherDay(scheduleItems);
                }else if(selectedButtonTexts != null){
                    intentSpecificDay(scheduleItems);
                }else{
                    count ++;
                    textDose.setText(getApplicationContext().getString(R.string.select_the_x_time) +" " + count + "° dose:");
                    selectHour.setText("00:00");
                }
            }
        });

    }

    public void popup_quantity(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_select_quantity, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(popupView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        buttonOk = popupView.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextInput = popupView.findViewById(R.id.inputNameEdit);
                valueQuantity = editTextInput.getText().toString();
                int quant = Integer.parseInt(valueQuantity);
                if(quant <= 0){
                    popup_warning(view);
                }else{
                    quantity.setText(valueQuantity);
                }
                alertDialog.dismiss();
            }
        });
    }

    public void selectHourClock(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                String formattedHour = String.format(Locale.getDefault(), "%02d", hourOfDay);
                String formattedMinute = String.format(Locale.getDefault(), "%02d", minute);
                hourReminder = formattedHour + ":" + formattedMinute;
                selectHour.setText(hourReminder);
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);

        timePickerDialog.show();
    }

    public void intentEveryDay(ArrayList<ScheduleItem> scheduleItems){
        Intent it = new Intent(SetScheduleActivity.this, PhotoMedicineReminder.class);
        it.putExtra("medicine", medicine);
        it.putExtra("typeMedicine", typeMedicine);
        it.putExtra("frequencyMedicine", frequencyMedicine);
        it.putExtra("frequencyDay", frequencyDay);
        it.putExtra("timeAndQuantity", scheduleItems);
        if(eachXhours != 0){
            it.putExtra("eachXhours", eachXhours);
        }
        startActivity(it);
    }

    public void intentEveryOtherDay(ArrayList<ScheduleItem> scheduleItems){
        Intent it = new Intent(SetScheduleActivity.this, PhotoMedicineReminder.class);
        it.putExtra("medicine", medicine);
        it.putExtra("typeMedicine", typeMedicine);
        it.putExtra("frequencyMedicine", frequencyMedicine);
        it.putExtra("frequencyDifferenceDays", frequencyDifferenceDays);
        it.putExtra("timeAndQuantity", scheduleItems);
        startActivity(it);
    }

    public void intentSpecificDay(ArrayList<ScheduleItem> scheduleItems){
        Intent it = new Intent(SetScheduleActivity.this, PhotoMedicineReminder.class);
        it.putExtra("medicine", medicine);
        it.putExtra("typeMedicine", typeMedicine);
        it.putExtra("frequencyMedicine", frequencyMedicine);
        it.putExtra("selectedButtonTexts", selectedButtonTexts);
        Log.d("", "Na int de set: " + selectedButtonTexts);
        it.putExtra("timeAndQuantity", scheduleItems);
        startActivity(it);
    }

    public void popup_warning(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_warnings_layout, null);

        warningText = popupView.findViewById(R.id.warningText);
        warningText.setText("O número não pode ser menor que 4, por favor, escolha outro número o escolha outra opção.");
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