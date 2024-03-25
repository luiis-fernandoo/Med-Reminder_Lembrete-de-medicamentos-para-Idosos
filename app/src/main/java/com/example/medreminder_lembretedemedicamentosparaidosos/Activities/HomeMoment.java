package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class HomeMoment extends AppCompatActivity {

    private ImageView imageMoment;
    private TextView textMoment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_moment);

        imageMoment = findViewById(R.id.imageMoment);
        textMoment = findViewById(R.id.textMoment);

        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        String image = sp.getString("profile_image_url", "");
        Glide.with(getApplicationContext())
                .load(image)
                .into(imageMoment);

    }
}