package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.ChoiceElderlyAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyCaregiverDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.List;

public class ChoiceElderlyActivity extends AppCompatActivity {

    private RecyclerView recycleElderly;
    private SharedPreferences sp;
    private String caregiver;
    private List<Elderly> elderlyList;
    private TextView choiceTheElderly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_elderly);

        recycleElderly = findViewById(R.id.recycleElderly);
        choiceTheElderly = findViewById(R.id.choiceTheElderly);

        choiceTheElderly.setText(R.string.Choose_the_elderly_person_who_will_take_the_medicine);

        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);

        caregiver = sp.getString("email", "");
        ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), new ElderlyCaregiver(caregiver));
        ElderlyCaregiver elderlyCaregiver = elderlyCaregiverDao.getElderlyCaregiver(caregiver);

        ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), new Elderly());
        elderlyList = elderlyDao.getElderlyByCareviger(elderlyCaregiver.get_id());

        if(elderlyList.size() == 0){
            setContentView(R.layout.activity_choice_elderly_empty);

            Button buttonAddElderly = findViewById(R.id.buttonAddElderly);
            buttonAddElderly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(ChoiceElderlyActivity.this, RegisterElderlyActivity.class);
                    startActivity(it);
                    finish();
                }
            });
        }

        ChoiceElderlyAdapter choiceElderlyAdapter = new ChoiceElderlyAdapter(elderlyList, this, sp, new MenuActivity());
        recycleElderly.setLayoutManager(new LinearLayoutManager(this));
        recycleElderly.setAdapter(choiceElderlyAdapter);
    }
}