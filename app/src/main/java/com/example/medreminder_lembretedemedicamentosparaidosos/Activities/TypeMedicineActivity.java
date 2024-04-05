package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class TypeMedicineActivity extends AppCompatActivity {

    private Button buttonPill, buttonDrops, buttonDust;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_medicine);

        Intent it = getIntent();
        String medicine = it.getStringExtra("medicine");

        buttonPill = findViewById(R.id.buttonPill);
        buttonDrops = findViewById(R.id.buttonDrops);
        buttonDust = findViewById(R.id.buttonDust);

        buttonPill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), FrequencyMedicineActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", "pill");
                startActivity(it);
            }
        });

        buttonDrops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), FrequencyMedicineActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", "drops");
                startActivity(it);
            }
        });

        buttonDust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), FrequencyMedicineActivity.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", "dust");
                startActivity(it);
            }
        });
    }
}