package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class EachXhoursActivity extends AppCompatActivity {

    private EditText inputEachHour;
    private Button buttonNext;
    private int result;
    private TextView enterHowManyTimes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_xhours);

        Intent it = getIntent();
        String medicine = it.getStringExtra("medicine");
        String typeMedicine = it.getStringExtra("typeMedicine");
        String frequencyMedicine = it.getStringExtra("frequencyMedicine");

        inputEachHour = findViewById(R.id.inputNameEdit);
        inputEachHour.setHint(R.string.type_here);
        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setText(R.string.next);
        enterHowManyTimes = findViewById(R.id.enterHowManyTimes);
        enterHowManyTimes.setText(R.string.how_many_hours);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = Integer.parseInt(inputEachHour.getText().toString());
                Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                it.putExtra("frequencyDay", 1);
                it.putExtra("eachXhours", result);
                startActivity(it);
            }
        });
    }
}