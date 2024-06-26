package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class TypeMedicineActivity extends AppCompatActivity {

    private TextView chooseTheFarmaceutic;
    private Button buttonPill, buttonDrops, buttonDust, buttonHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_medicine);

        Intent it = getIntent();
        String medicine = it.getStringExtra("medicine");

        buttonPill = findViewById(R.id.buttonPill);
        buttonPill.setText(R.string.pill);

        buttonDrops = findViewById(R.id.buttonDrops);
        buttonDrops.setText(R.string.drops);

        buttonDust = findViewById(R.id.buttonDust);
        buttonDust.setText(R.string.dust);

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

        buttonHelp = findViewById(R.id.buttonHelp);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupHelp();
            }
        });
    }

    public void popupHelp(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_help, null);

        TextView textHelp = popupView.findViewById(R.id.textHelp);
        textHelp.setText(R.string.textHelpType);

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