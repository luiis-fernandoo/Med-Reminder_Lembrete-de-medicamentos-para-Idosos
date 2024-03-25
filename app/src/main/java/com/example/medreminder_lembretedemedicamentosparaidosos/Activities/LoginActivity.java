package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private TextView goRegister, notRegister, typeUserLogin;
    private EditText emailLogin, passwordLogin;
    private Button buttonLogin;
    private String selectedUserType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        goRegister = findViewById(R.id.goRegister);
        notRegister = findViewById(R.id.notRegister);
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        typeUserLogin = findViewById(R.id.typeUserLogin);

        typeUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibirPopup(view);
            }
        });

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
                if(elderlyDao.insertNewElderly()){
                    Intent intent = new Intent(LoginActivity.this, HomeMoment.class);
                    startActivity(intent);
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString();
                String typeUser = selectedUserType;

                if(typeUser.equals("Idoso")){
                    Elderly elderly = new Elderly(email, password);
                    ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), elderly);
                    if(elderlyDao.VerifyLogin()){
                        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("email", email);
                        Intent intent = new Intent(LoginActivity.this, HomeMoment.class);
                        startActivity(intent);
                    }else{
                        loginFirebase(email, password);
                    }
                }else{
                    ElderlyCaregiver elderlyCaregiver = new ElderlyCaregiver(email, password);
                    ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), elderlyCaregiver);
                    if(elderlyCaregiverDao.VerifyLogin()){
                        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("email", email);
                        Intent intent = new Intent(LoginActivity.this, HomeMoment.class);
                        startActivity(intent);
                    }else{
                        loginFirebase(email, password);
                    }
                }
            }
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
                typeUserLogin.setText(selectedUserType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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

    public void loginFirebase(String email, String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("email", email);
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("name");
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String name = snapshot.getValue(String.class);
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
                                        editor.putString("typeUser", typeUser);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            if(sp.getString("typeUser", "").equals("Idoso")){
                                Elderly elderly = new Elderly(sp.getString("name", ""), email, password);
                                ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), elderly);
                                if(elderlyDao.insertNewElderly()){
                                    Intent intent = new Intent(LoginActivity.this, HomeMoment.class);
                                    startActivity(intent);
                                }
                            }else{
                                ElderlyCaregiver elderlyCaregiver = new ElderlyCaregiver(sp.getString("name", ""), email, password);
                                ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), elderlyCaregiver);
                                if(elderlyCaregiverDao.insertNewElderlyCaregiver()){
                                    Intent intent = new Intent(LoginActivity.this, HomeMoment.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                });
    }
}