package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class MoreThanThreeTimesActivity extends AppCompatActivity {

    private Button buttonNext;
    private EditText inputMoreThanThreeTimes;
    private int result;
    private LinearLayout buttonOk;
    private TextView warningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_than_three_times);

        Intent it = getIntent();
        String medicine = it.getStringExtra("medicine");
        String typeMedicine = it.getStringExtra("typeMedicine");
        String frequencyMedicine = it.getStringExtra("frequencyMedicine");

        buttonNext = findViewById(R.id.buttonNext);
        inputMoreThanThreeTimes = findViewById(R.id.inputMoreThanThreeTimes);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = Integer.parseInt(inputMoreThanThreeTimes.getText().toString());
                if(result <= 3){
                    popup_warning(view);
                }
                Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyDay", result);
                startActivity(it);
            }
        });
    }

    public void popup_warning(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_warnings_layout, null);

        warningText = findViewById(R.id.warningText);
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