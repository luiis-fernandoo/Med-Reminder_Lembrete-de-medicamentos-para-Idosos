package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyCaregiverDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView goRegister, notRegister;
    private EditText emailLogin, passwordLogin;
    private Button buttonLogin;
    private String selectedUserType;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        goRegister = findViewById(R.id.goRegister);
        notRegister = findViewById(R.id.notRegister);
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        buttonLogin = findViewById(R.id.buttonLogin);

        sp = getSharedPreferences("app", Context.MODE_PRIVATE);

        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(LoginActivity.this, RegisterAcitivity.class);
                startActivity(it);
            }
        });

        notRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "Convidado";
                Elderly elderly = new Elderly(name);
                ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), elderly);
                if(elderlyDao.VerifyGuest()){
                    SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Guest", name);
                    editor.putString("selectedUserType", "Idoso");
                    editor.apply();
                    Log.d("", "Shared de convidado: " + sp.getString("Guest", ""));
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
                else if(elderlyDao.insertNewElderly()){
                    SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Guest", name);
                    editor.putString("selectedUserType", "Idoso");
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString();

                if (!isValidEmail(email)) {
                    Toast.makeText(LoginActivity.this, "O endereço de e-mail é inválido.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(LoginActivity.this, "A senha deve ter pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Elderly elderly = new Elderly(email, password);
                ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), elderly);
                if(elderlyDao.VerifyLogin()){
                    saveEmailSharedPreferences(email, "Idoso");
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    ElderlyCaregiver elderlyCaregiver = new ElderlyCaregiver(email, password);
                    ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), elderlyCaregiver);
                    if(elderlyCaregiverDao.VerifyLogin()){
                        saveEmailSharedPreferences(email, "Cuidador de idoso");
                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }else{
                        loginFirebase(email, password);
                    }
                }
            }
        });
    }

    public void loginFirebase(String email, String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("name");
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String name = snapshot.getValue(String.class);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("name", name);
                                        editor.putString("email", email);
                                        editor.apply();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            DatabaseReference userRefType = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("typeUser");
                            userRefType.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        String typeUser = snapshot.getValue(String.class);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("selectedUserType", typeUser);
                                        editor.apply();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            });

                            if(sp.getString("selectedUserType", "").equals("Idoso")){
                                Elderly elderly = new Elderly(sp.getString("name", ""), email, password);
                                ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), elderly);
                                if(elderlyDao.insertNewElderly()){
                                    saveEmailSharedPreferences(email, sp.getString("selectedUserType", ""));
                                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                ElderlyCaregiver elderlyCaregiver = new ElderlyCaregiver(sp.getString("name", ""), email, password);
                                ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), elderlyCaregiver);
                                if(elderlyCaregiverDao.insertNewElderlyCaregiver()){
                                    saveEmailSharedPreferences(email, sp.getString("selectedUserType", ""));
                                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "O usuário não foi encontrado", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void saveEmailSharedPreferences(String email, String selectedUserType){
        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", email);
        editor.putString("selectedUserType", selectedUserType);
        editor.apply();
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}