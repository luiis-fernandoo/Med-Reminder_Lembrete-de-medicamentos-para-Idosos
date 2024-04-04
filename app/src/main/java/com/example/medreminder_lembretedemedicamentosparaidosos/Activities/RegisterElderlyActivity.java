package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyCaregiverDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Fragments.HomeFragment;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterElderlyActivity extends AppCompatActivity {

    private TextView laterElderly;
    private Button buttonRegisterElderly;
    private ImageView imageElderly;
    private EditText nameRegisterElderly, ageRegisterElderly;
    private Uri selectedImageUri;
    private String currentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_elderly);

        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);

        String emailCareviger = sp.getString("email", "");

        ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), new ElderlyCaregiver());
        ElderlyCaregiver caregiver = elderlyCaregiverDao.getElderlyCaregiver(emailCareviger);

        laterElderly = findViewById(R.id.laterElderly);
        buttonRegisterElderly = findViewById(R.id.buttonRegisterElderly);
        nameRegisterElderly = findViewById(R.id.nameRegisterElderly);
        ageRegisterElderly = findViewById(R.id.ageRegisterElderly);
        imageElderly = findViewById(R.id.imageElderly);

        imageElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        laterElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(RegisterElderlyActivity.this, HomeFragment.class);
                startActivity(it);
                finish();
            }
        });

        buttonRegisterElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Elderly elderly = new Elderly(nameRegisterElderly.getText(), "Null", currentPhotoPath, "Null", ageRegisterElderly.getText(), caregiver.get_id());
                ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), elderly);
                if(elderlyDao.insertNewElderly()){
                    Toast.makeText(RegisterElderlyActivity.this, "Idoso cadastrado com sucesso!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK && data != null) {
                selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    imageElderly.setVisibility(View.VISIBLE);
                    imageElderly.setImageURI(selectedImageUri);
                    saveImageToInternalStorage(selectedImageUri);
                } else {
                    Toast.makeText(this, "Selecione uma imagem válida", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Seleção de imagem cancelada ou falhou", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImageToInternalStorage(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            File directory = new File(getFilesDir() + "/MedReminder");
            if (!directory.exists()) {
                directory.mkdir();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "IMG_" + timeStamp + ".jpg";
            File file = new File(directory, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.close();
            currentPhotoPath = file.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}