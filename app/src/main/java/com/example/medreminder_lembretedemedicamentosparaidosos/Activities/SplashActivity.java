package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ImageView gifPrimary, gifSecondary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

         gifPrimary = findViewById(R.id.gifPrimary);
         gifSecondary = findViewById(R.id.gifSecondary);

        Glide.with(this).asGif().load(R.drawable.ajudando).into(gifPrimary);
        Glide.with(this).asGif().load(R.drawable.trabalho_em_progresso).into(gifSecondary);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                String savedEmail = sp.getString("email", "");
                String guest = sp.getString("Guest", "");

                if (!savedEmail.isEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }else if(guest.equals("Convidado")){
                    Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent it = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        }, 4000);
    }
}