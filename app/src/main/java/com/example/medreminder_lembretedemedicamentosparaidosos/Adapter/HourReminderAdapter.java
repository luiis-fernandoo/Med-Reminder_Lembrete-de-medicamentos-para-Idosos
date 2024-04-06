package com.example.medreminder_lembretedemedicamentosparaidosos.Adapter;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.Calendar;

public class HourReminderAdapter  extends RecyclerView.Adapter<HourReminderAdapter.MyViewHolder>{
    private Activity activity;
    private int quantityTimes;
    private TextView selectTime, quantity;
    private ImageView selectQuantity;
    private String horario, valueQuantity;

    public HourReminderAdapter(Activity activity, int quantityTimes) {
        this.activity = activity;
        this.quantityTimes = quantityTimes;
    }
    @NonNull
    @Override
    public HourReminderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.hour_medicine_cards_layout, parent, false);
        return new HourReminderAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourReminderAdapter.MyViewHolder holder, int position) {
        if (position < quantityTimes) {
            holder.bind(quantityTimes);
        } else {
            Log.e("Adapter", "Índice fora dos limites: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return quantityTimes;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            selectTime = itemView.findViewById(R.id.selectTime);
            selectQuantity = itemView.findViewById(R.id.selectQuantity);
            quantity = itemView.findViewById(R.id.quantity);

        }

        public void bind(int medicine) {
            selectTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectHourClock();
                }
            });

            selectQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exibirPopup(view);
                }
            });
        }
    }

    public void selectHourClock(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                horario = hourOfDay + ":" + minute;
                selectTime.setText(horario);
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);

        timePickerDialog.show();
    }

    private void exibirPopup(View view) {
        view = LayoutInflater.from(activity).inflate(R.layout.popup_select_quantity, null);
        Spinner spinner = view.findViewById(R.id.medicine_quantity_spinner);

        String[] options = {"1", "2", "3", "4", "5"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, options);
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
                Toast.makeText(activity, "Nenhuma quantidade definida", Toast.LENGTH_SHORT).show();
            }
        });

        androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(activity)
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
}
