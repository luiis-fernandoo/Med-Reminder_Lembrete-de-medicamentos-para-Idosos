package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FeedEntry.DBHelpers dbHelper = new FeedEntry.DBHelpers(this);
        dbHelper.deleteDatabase(this);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                String savedEmail = sp.getString("email", "");

                if (!savedEmail.isEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, HomeMoment.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent it = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        }, 3000);
    }
}