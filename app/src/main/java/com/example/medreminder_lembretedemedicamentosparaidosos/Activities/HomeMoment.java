package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class HomeMoment extends AppCompatActivity {

    private ImageView imageMoment;
    private TextView textMoment, logout;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_moment);

        imageMoment = findViewById(R.id.imageMoment);
        textMoment = findViewById(R.id.textMoment);
        logout = findViewById(R.id.logout);

        sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        String image = sp.getString("profile_image_url", "");
        Log.d("", "Shared image" + image);
        Glide.with(getApplicationContext())
                .load(image)
                .into(imageMoment);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sp.edit();

        editor.remove("email");
        editor.remove("name");
        editor.remove("profile_image_url");

        editor.apply();

        Log.d("", "Dentro do metodo " + sp.getString("profile_image_url", ""));
        Intent intent = new Intent(HomeMoment.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}