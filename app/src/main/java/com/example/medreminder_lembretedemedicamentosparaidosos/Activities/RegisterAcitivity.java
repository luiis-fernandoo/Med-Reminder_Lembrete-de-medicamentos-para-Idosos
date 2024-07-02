package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyCaregiverDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterAcitivity extends AppCompatActivity {

    private EditText emailRegister, nameRegister, passwordRegister;
    private TextView typeUser, registerText;
    private Button buttonRegister, buttonHelp;
    private FirebaseAuth firebaseAuth;
    private CircleImageView photo_profile;
    private static final int REQUEST_IMAGE_PICK = 2;
    private String selectedUserType, currentPhotoPath;
    private Uri selectedImageUri;
    private SharedPreferences sp;
    private Dialog progressDialog;

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
        registerText = findViewById(R.id.registerText);
        buttonHelp = findViewById(R.id.buttonHelp);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_loading);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);

        sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerFirebase(view);
            }
        });

        typeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibirPopup(view);
            }
        });

        photo_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupHelp();
            }
        });
    }

    public void registerFirebase(View view){
        progressDialog.show();
        String email = emailRegister.getText().toString().trim();
        String password = passwordRegister.getText().toString().trim();
        String name = nameRegister.getText().toString().trim();
        if (!isValidEmail(email)) {
            Toast.makeText(RegisterAcitivity.this, "O endereço de e-mail é inválido.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(RegisterAcitivity.this, "A senha deve ter pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if(selectedUserType == null){
            Toast.makeText(RegisterAcitivity.this, "Selecione o tipo de usuário.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
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
                            userRef.child("name").setValue(name);
                            userRef.child("typeUser").setValue(selectedUserType);

                            if (selectedImageUri != null) {
                                uploadProfileImage(userID, userRef);
                                saveImageToInternalStorage(selectedImageUri);
                            }
                            saveEmailSharedPreferences(email, selectedUserType);
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task1 -> {});
                            if(TextUtils.equals(typeUser.getText().toString().trim(),"Idoso") || TextUtils.equals(typeUser.getText().toString().trim(),"Elderly")){
                                Elderly elderly = new Elderly(name, email, currentPhotoPath, password);
                                ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), elderly);
                                if(elderlyDao.insertNewElderly()){
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterAcitivity.this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(RegisterAcitivity.this, MenuActivity.class);
                                    startActivity(it);
                                    finish();
                                }
                            }else{
                                ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), new ElderlyCaregiver(name, email, currentPhotoPath, password));
                                if(elderlyCaregiverDao.insertNewElderlyCaregiver()){
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterAcitivity.this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(RegisterAcitivity.this, RegisterElderlyActivity.class);
                                    startActivity(it);
                                    finish();
                                }
                            }

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterAcitivity.this, "Falha ao criar usuário: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadProfileImage(String userID, DatabaseReference userRef) {
        String imageFileName = "profile_images/" + userID + ".jpg";

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(imageFileName);

        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        userRef.child("profile_image_url").setValue(uri.toString());
                        saveImageUrlInSharedPreferences(uri.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterAcitivity.this, "Falha ao enviar a imagem de perfil para o Firebase Storage." + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                if(selectedUserType.equals("Idoso")) {
                    typeUser.setText(R.string.elderly);
                }else{
                    typeUser.setText(R.string.elderlyCaregiver);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(RegisterAcitivity.this, "Nenhum usuário selecionado", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
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
        if (requestCode == REQUEST_IMAGE_PICK) {
            if (resultCode == RESULT_OK && data != null) {
                selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    photo_profile.setVisibility(View.VISIBLE);
                    photo_profile.setImageURI(selectedImageUri);
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

    private void saveImageUrlInSharedPreferences(String imageUrl) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("profile_image_url", imageUrl);
        editor.apply();
    }

    public void saveEmailSharedPreferences(String email, String selectedUserType){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", email);
        editor.putString("selectedUserType", selectedUserType);
        editor.putInt("isFirstTime", 1);
        editor.apply();
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void popupHelp(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_help, null);

        TextView textHelp = popupView.findViewById(R.id.textHelp);
        textHelp.setText(R.string.textHelpRegister);

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setView(popupView);

        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button buttonOk = popupView.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}