package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.HourReminderAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.SearchAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.text.ParseException;
import java.util.Calendar;

public class SetScheduleActivity extends AppCompatActivity {

    private TextView selectType, quantity, selectHour;
    private ImageView selectQuantity;
    private String valueQuantity, hourReminder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);
        Intent it = getIntent();
        String medicine = it.getStringExtra("medicine");
        String typeMedicine = it.getStringExtra("typeMedicine");
        String frequencyMedicine = it.getStringExtra("frequencyMedicine");
        if (it.getStringExtra("frequencyTimes").equals("everyDay")) {
            String frequencyDay = it.getStringExtra("frequencyDay");
        }

        selectQuantity = findViewById(R.id.selectQuantity);
        quantity = findViewById(R.id.quantity);
        selectType = findViewById(R.id.selectType);
        selectHour = findViewById(R.id.selectHour);

        switch (typeMedicine) {
            case "pill":
                selectType.setText("comprimido(s)");
                break;
            case "drops":
                selectType.setText("Gotas(s)");
                break;
            case "dust":
                selectType.setText("Sachê(s)");
                break;
        }

        selectQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibirPopup(view);
            }
        });

        selectHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHourClock();
            }
        });

    }
    private void exibirPopup(View view) {
        view = LayoutInflater.from(this).inflate(R.layout.popup_select_quantity, null);
        Spinner spinner = view.findViewById(R.id.medicine_quantity_spinner);

        String[] options = {"1", "2", "3", "4", "5"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueQuantity = options[position];
                quantity.setText(valueQuantity);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SetScheduleActivity.this, "Nenhuma quantidade definida", Toast.LENGTH_SHORT).show();
            }
        });

        androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Selecionar quantidade")
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        valueQuantity = (String) spinner.getSelectedItem();
                        Log.d("", "quantity: " + valueQuantity);
                    }
                })
                .create();
        dialog.show();
    }

    public void selectHourClock(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                hourReminder = hourOfDay + ":" + minute;
                selectHour.setText(hourReminder);
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);

        timePickerDialog.show();
    }
}