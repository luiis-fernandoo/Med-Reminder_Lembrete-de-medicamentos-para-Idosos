package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class AddMedicineActivity extends AppCompatActivity {

    private Button addMedicine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        addMedicine = findViewById(R.id.addMedicine);

        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(AddMedicineActivity.this, SearchMedicineActivity.class);
                startActivity(it);
            }
        });
    }
}