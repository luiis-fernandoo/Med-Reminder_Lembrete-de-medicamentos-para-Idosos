package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.ArrayList;
import java.util.List;

public class FrequencyMedicineSpecificDaysActivity extends AppCompatActivity {

     private Button buttonSunday, buttonMonday, buttonTuesday, buttonWednesday, buttonThursday, buttonFriday, buttonSaturday, buttonConfirmDays;
     private List<Button> selectedButtons = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_medicine_specific_days);

        Intent it = getIntent();
        String medicine = it.getStringExtra("medicine");
        String typeMedicine = it.getStringExtra("typeMedicine");
        String frequencyMedicine = it.getStringExtra("frequencyMedicine");

        buttonSunday = findViewById(R.id.buttonSunday);
        buttonMonday = findViewById(R.id.buttonMonday);
        buttonTuesday = findViewById(R.id.buttonTuesDay);
        buttonWednesday = findViewById(R.id.buttonWednesday);
        buttonThursday = findViewById(R.id.buttonThursday);
        buttonFriday = findViewById(R.id.buttonFriday);
        buttonSaturday = findViewById(R.id.buttonSaturday);
        buttonConfirmDays = findViewById(R.id.buttonConfirmDays);

        buttonSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButtonSelection(buttonSunday);
            }
        });

        buttonMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButtonSelection(buttonMonday);
            }
        });

        buttonTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButtonSelection(buttonTuesday);
            }
        });

        buttonWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButtonSelection(buttonWednesday);
            }
        });

        buttonThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButtonSelection(buttonThursday);
            }
        });

        buttonFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButtonSelection(buttonFriday);
            }
        });

        buttonSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButtonSelection(buttonSaturday);
            }
        });

        buttonConfirmDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> selectedButtonTexts = new ArrayList<>();
                for (Button button : selectedButtons) {
                    selectedButtonTexts.add(button.getText().toString());
                }
                Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyTimes", "specificDays");
                it.putStringArrayListExtra("selectedButtonTexts", selectedButtonTexts);

                startActivity(it);
            }
        });

    }

    private void toggleButtonSelection(Button button) {
        boolean isSelected = button.isSelected();
        button.setSelected(!isSelected);
        if (isSelected) {
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_type_medicine));
            selectedButtons.remove(button);
        } else {
            selectedButtons.add(button);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector));
        }
    }
}