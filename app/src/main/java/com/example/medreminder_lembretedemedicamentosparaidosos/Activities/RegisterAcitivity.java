package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterAcitivity extends AppCompatActivity {

    private EditText emailRegister, nameRegister, passwordRegister;
    private TextView typeUser;
    private Button buttonRegister;
    private FirebaseAuth firebaseAuth;
    private ImageView photo_profile;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private String selectedUserType;
    private Uri selectedImageUri;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivity);

        emailRegister = findViewById(R.id.emailRegister);
        nameRegister = findViewById(R.id.nameRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        buttonRegister = findViewById(R.id.buttonRegister);
        typeUser = findViewById(R.id.typeUser);
        photo_profile = findViewById(R.id.photo_profile);
        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        typeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        photo_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });
    }

    public void RegisterFirebase(View view){
        String email = emailRegister.getText().toString().trim();
        String password = passwordRegister.getText().toString().trim();
        String name = nameRegister.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userID = firebaseUser.getUid();
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                            DatabaseReference userRef = usersRef.child(userID);

                            userRef.child("email").setValue(email);
                            userRef.child("nome").setValue(name);
                            userRef.child("typeUser").setValue(selectedUserType);
                            uploadProfileImage(userID);

                            Toast.makeText(RegisterAcitivity.this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterAcitivity.this, "Falha ao criar usuário: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadProfileImage(String userID) {
        String imageFileName = "profile_images/" + userID + ".jpg";

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(imageFileName);

        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                        usersRef.child(userID).child("profile_image_url").setValue(uri.toString());
                        saveImageUrlInSharedPreferences(uri.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterAcitivity.this, "Falha ao enviar a imagem de perfil para o Firebase Storage.", Toast.LENGTH_SHORT).show();
                });
    }

    private void exibirPopup(View view) {
        view = LayoutInflater.from(this).inflate(R.layout.popup_select_user, null);
        Spinner spinner = view.findViewById(R.id.user_spinner);

        String[] options = {"Idoso", "Cuidador de idoso"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUserType = options[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(RegisterAcitivity.this, "Nenhum usuário selecionado", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.Theme_MedReminderLembreteDeMedicamentosParaIdosos)
                .setView(view)
                .setTitle("Selecionar usuário")
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userType = (String) spinner.getSelectedItem();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            photo_profile.setVisibility(View.VISIBLE);
            photo_profile.setImageURI(selectedImageUri);
            saveImageToInternalStorage(selectedImageUri);
        }
    }

    private void saveImageToInternalStorage(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            File directory = new File(Environment.getExternalStorageDirectory() + "/MedReminder");
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

            saveUrlToDatabase(currentPhotoPath);

            Toast.makeText(this, "Imagem salva com sucesso.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUrlToDatabase(String url) {
        Log.d("", "Url da imagem de perfil para testes " + url);
    }

    private void saveImageUrlInSharedPreferences(String imageUrl) {
        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("profile_image_url", imageUrl);
        editor.apply();
    }
}