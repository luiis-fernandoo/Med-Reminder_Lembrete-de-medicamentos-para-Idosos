package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ScheduleItem;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PhotoMedicinePackagingActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imagePhotoCapture, iconCameraPhoto;
    private TextView textViewHeader;
    private String currentPhotoPath, medicine, typeMedicine, frequencyMedicine, frequencyTimes, currentPhotoPathPrevius;
    private ArrayList<String> selectedButtonTexts;
    private ArrayList<ScheduleItem> scheduleItems;
    private int frequencyDay, frequencyDifferenceDays;
    private Button buttonNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_medicine_packaging);

        Intent it = getIntent();
        medicine = it.getStringExtra("medicine");
        typeMedicine = it.getStringExtra("typeMedicine");
        frequencyMedicine = it.getStringExtra("frequencyMedicine");
        if(frequencyMedicine != null){
            if (frequencyMedicine.equals("everyDay")) {
                frequencyDay = it.getIntExtra("frequencyDay", 1);
            }else if(frequencyMedicine.equals("everyOtherDay")){
                frequencyDifferenceDays = it.getIntExtra("frequencyDifferenceDays", -1);
            }else if(frequencyMedicine.equals("specificDay")){
                selectedButtonTexts = it.getStringArrayListExtra("selectedButtonTexts");
            }
        }
        scheduleItems = it.getParcelableArrayListExtra("timeAndQuantity");
        currentPhotoPathPrevius = it.getStringExtra("currentPhotoPath");

        imagePhotoCapture = findViewById(R.id.imagePhotoCapture);
        iconCameraPhoto = findViewById(R.id.iconCameraPhoto);
        textViewHeader = findViewById(R.id.textViewHeader);
        buttonNext = findViewById(R.id.buttonNext);

        iconCameraPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(PhotoMedicinePackagingActivity.this, ResetReminder.class);
                it.putExtra("medicine", medicine);
                it.putExtra("typeMedicine", typeMedicine);
                it.putExtra("frequencyMedicine", frequencyMedicine);
                if(frequencyMedicine != null){
                    if (frequencyMedicine.equals("everyDay")) {
                        it.putExtra("frequencyDay", frequencyDay);
                    }else if(frequencyMedicine.equals("everyOtherDay")){
                        it.putExtra("frequencyDifferenceDays", frequencyDifferenceDays);
                    }else if(frequencyMedicine.equals("specificDay")){
                        it.putExtra("selectedButtonTexts", selectedButtonTexts);
                    }
                }
                it.putExtra("timeAndQuantity", scheduleItems);
                it.putExtra("currentPhotoPathOne", currentPhotoPathPrevius);
                it.putExtra("currentPhotoPathTwo", currentPhotoPath);
                startActivity(it);
            }
        });
    }

    //    private void saveImageUrlInSharedPreferences(String imageUrl) {
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("medicine", imageUrl);
//        editor.apply();
//    }

    private void saveImageToInternalStorage(Bitmap bitmap) {
        try {
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                    // Obtém a imagem capturada da intent
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

                    // Exibe a imagem capturada no ImageView
                    imagePhotoCapture.setImageBitmap(imageBitmap);
                    textViewHeader.setText("SUA FOTO DA EMBALAGEM DO MEDICAMENTO:");
                    saveImageToInternalStorage(imageBitmap);
                } else {
                    Toast.makeText(this, "Falha ao capturar a imagem", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Captura de imagem cancelada ou falhou", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    private void displayImage() {
//        if (currentPhotoPath != null) {
//            // Carrega a imagem do caminho do arquivo
//            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
//
//            // Verifica se o bitmap foi carregado com sucesso
//            if (bitmap != null) {
//                // Define o bitmap no ImageView
//                imagePhotoCapture.setImageBitmap(bitmap);
//            } else {
//                Toast.makeText(this, "Falha ao carregar a imagem", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "Caminho da imagem não encontrado", Toast.LENGTH_SHORT).show();
//        }
//    }
}