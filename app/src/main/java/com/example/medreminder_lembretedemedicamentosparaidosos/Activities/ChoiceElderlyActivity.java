package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.ChoiceElderlyAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyCaregiverDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.ArrayList;
import java.util.List;

public class ChoiceElderlyActivity extends AppCompatActivity {

    private RecyclerView recycleElderly;
    private SharedPreferences sp;
    private String caregiver;
    private ChoiceElderlyAdapter choiceElderlyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_elderly);

        recycleElderly = findViewById(R.id.recycleElderly);

        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);

        caregiver = sp.getString("email", "");
        ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), new ElderlyCaregiver(caregiver));
        ElderlyCaregiver elderlyCaregiver = elderlyCaregiverDao.getElderlyCaregiver(caregiver);

        ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), new Elderly());
        List<Elderly> elderlyList = new ArrayList<>();
        elderlyList = elderlyDao.getElderlyByCareviger(elderlyCaregiver.get_id());

        recycleElderly = findViewById(R.id.recycleElderly);
        recycleElderly.setLayoutManager(new LinearLayoutManager(this));
        choiceElderlyAdapter = new ChoiceElderlyAdapter(this);
        recycleElderly.setAdapter(choiceElderlyAdapter);

        choiceElderlyAdapter.setElderlies(elderlyList);

    }
}