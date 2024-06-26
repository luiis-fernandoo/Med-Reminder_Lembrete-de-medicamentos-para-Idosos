package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.ArrayList;
import java.util.List;

public class FrequencyMedicineSpecificDaysActivity extends AppCompatActivity {

     private Button buttonHelp, buttonSunday, buttonMonday, buttonTuesday, buttonWednesday, buttonThursday, buttonFriday, buttonSaturday, buttonConfirmDays;
     private List<Button> selectedButtons = new ArrayList<>();
     private TextView WhichDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_medicine_specific_days);

        Intent it = getIntent();
        String medicine = it.getStringExtra("medicine");
        String typeMedicine = it.getStringExtra("typeMedicine");
        String frequencyMedicine = it.getStringExtra("frequencyMedicine");

        WhichDays = findViewById(R.id.WhichDays);
        buttonSunday = findViewById(R.id.buttonSunday);
        buttonMonday = findViewById(R.id.buttonMonday);
        buttonTuesday = findViewById(R.id.buttonTuesDay);
        buttonWednesday = findViewById(R.id.buttonWednesday);
        buttonThursday = findViewById(R.id.buttonThursday);
        buttonFriday = findViewById(R.id.buttonFriday);
        buttonSaturday = findViewById(R.id.buttonSaturday);
        buttonConfirmDays = findViewById(R.id.buttonConfirmDays);
        buttonHelp = findViewById(R.id.buttonHelp);

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
                it.putStringArrayListExtra("selectedButtonTexts", selectedButtonTexts);
                Log.d("", "Na classe: " + selectedButtonTexts);
                startActivity(it);
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupHelp();
            }
        });
    }

    private void toggleButtonSelection(Button button) {
        boolean isSelected = button.isSelected();
        button.setSelected(!isSelected);
        if (isSelected) {
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_type_medicine));
            button.setTextColor(ContextCompat.getColor(this, R.color.red_end));
            selectedButtons.remove(button);
        } else {
            selectedButtons.add(button);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector));
            button.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    public void popupHelp(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_help, null);

        TextView textHelp = popupView.findViewById(R.id.textHelp);
        textHelp.setText(R.string.textHelpSpecific);

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setView(popupView);

        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button buttonOk = popupView.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}