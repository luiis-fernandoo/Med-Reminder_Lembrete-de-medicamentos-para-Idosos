package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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
    private Button buttonLogin, buttonHelp;
    private String selectedUserType;
    private Dialog progressDialog;
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
        buttonHelp = findViewById(R.id.buttonHelp);

        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_loading);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);

        sp = getSharedPreferences("app", Context.MODE_PRIVATE);

        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(LoginActivity.this, RegisterAcitivity.class);
                startActivity(it);
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupHelp();
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
                progressDialog.show();
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
                    progressDialog.dismiss();
                    startActivity(intent);
                    finish();
                }else{
                    ElderlyCaregiver elderlyCaregiver = new ElderlyCaregiver(email, password);
                    ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), elderlyCaregiver);
                    if(elderlyCaregiverDao.VerifyLogin()){
                        saveEmailSharedPreferences(email, "Cuidador de idoso");
                        progressDialog.dismiss();
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

    public void popupHelp(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_help, null);

        TextView textHelp = popupView.findViewById(R.id.textHelp);
        textHelp.setText(R.string.textHelpLogin);

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