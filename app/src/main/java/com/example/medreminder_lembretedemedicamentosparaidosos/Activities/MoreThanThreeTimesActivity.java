package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class MoreThanThreeTimesActivity extends AppCompatActivity {

    private Button buttonNext, buttonHelp;
    private EditText inputMoreThanThreeTimes;
    private int result;
    private LinearLayout buttonOk;
    private TextView warningText, enterHowManyTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_than_three_times);

        Intent it = getIntent();
        String medicine = it.getStringExtra("medicine");
        String typeMedicine = it.getStringExtra("typeMedicine");
        String frequencyMedicine = it.getStringExtra("frequencyMedicine");

        buttonHelp = findViewById(R.id.buttonHelp);
        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setText(R.string.next);

        inputMoreThanThreeTimes = findViewById(R.id.inputNameEdit);
        inputMoreThanThreeTimes.setHint(R.string.type_here);

        enterHowManyTimes = findViewById(R.id.enterHowManyTimes);
        enterHowManyTimes.setText(R.string.Enter_how_many_times_you_take_this_medicine_per_day);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = Integer.parseInt(inputMoreThanThreeTimes.getText().toString());
                if(result <= 3){
                    popup_warning(view);
                }else{
                    Intent it = new Intent(getApplicationContext(), SetScheduleActivity.class);
                    it.putExtra("medicine", medicine);
                    it.putExtra("typeMedicine", typeMedicine);
                    it.putExtra("frequencyMedicine", frequencyMedicine);
                    it.putExtra("frequencyDay", result);
                    startActivity(it);
                }
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupHelp();
            }
        });
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

    public void popupHelp(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_help, null);

        TextView textHelp = popupView.findViewById(R.id.textHelp);
        textHelp.setText(R.string.textHelpMoreThan);

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