package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;

public class SucessSaveReminder extends AppCompatActivity {

    private Button buttonNext;
    private TextView successSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess_save_reminder);

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setText(R.string.next);

        successSave = findViewById(R.id.successSave);
        successSave.setText(R.string.Treatment_reminder_saved_successfully);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }
        }

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SucessSaveReminder.this, MenuActivity.class);
                startActivity(it);
            }
        });
    }
}