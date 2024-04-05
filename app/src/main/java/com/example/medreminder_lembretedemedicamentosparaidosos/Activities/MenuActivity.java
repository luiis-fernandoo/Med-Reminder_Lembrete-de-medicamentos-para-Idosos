package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.medreminder_lembretedemedicamentosparaidosos.Fragments.HomeFragment;
import com.example.medreminder_lembretedemedicamentosparaidosos.Fragments.MedicineFragment;
import com.example.medreminder_lembretedemedicamentosparaidosos.Fragments.ProfileFragment;
import com.example.medreminder_lembretedemedicamentosparaidosos.Fragments.ReminderFragment;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);

        replaceFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()== R.id.home){
                replaceFragment(new HomeFragment());
            } else if (item.getItemId()==R.id.medicine){
                replaceFragment(new MedicineFragment());
            } else if (item.getItemId()==R.id.profile){
                replaceFragment(new ProfileFragment());
            } else if (item.getItemId()==R.id.reminder){
                replaceFragment(new ReminderFragment());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}