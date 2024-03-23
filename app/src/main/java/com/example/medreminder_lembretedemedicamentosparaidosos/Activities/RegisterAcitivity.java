package com.example.medreminder_lembretedemedicamentosparaidosos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.medreminder_lembretedemedicamentosparaidosos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterAcitivity extends AppCompatActivity {

    private EditText emailRegister, nameRegister, passwordRegister;
    private Button buttonRegister;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivity);

        emailRegister = findViewById(R.id.emailRegister);
        nameRegister = findViewById(R.id.nameRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        buttonRegister = findViewById(R.id.buttonRegister);
        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                            Toast.makeText(RegisterAcitivity.this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                            // Aqui você pode redirecionar o usuário para outra atividade, por exemplo
                        } else {
                            // Se o cadastro falhar, exibir uma mensagem para o usuário
                            Toast.makeText(RegisterAcitivity.this, "Falha ao criar usuário: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void exibirPopup(View view) {
        view = LayoutInflater.from(this).inflate(R.layout.popup_select_user, null);
        Spinner spinner = view.findViewById(R.id.user_spinner);
        
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
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
}