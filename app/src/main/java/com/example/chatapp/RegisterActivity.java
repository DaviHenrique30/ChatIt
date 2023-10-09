package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextView xTxtToLogin;
    private EditText xEditEmail, xEditPassword, xEditUsername;
    private AppCompatButton xBtnRegister;
    private ProgressBar xProgressBar;
    String[] mensagens = {"Preencha todos os campos!", "Cadastro realizado com sucesso"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //INICIO DOS COMPONENTES
        xTxtToLogin = findViewById(R.id.txtToLogin);
        xEditUsername = findViewById(R.id.edit_Name);
        xEditEmail = findViewById(R.id.editEmail);
        xEditPassword = findViewById(R.id.editPassword);
        xBtnRegister = findViewById(R.id.btnRegister);
        xProgressBar = findViewById(R.id.progressBarRegister);

        // BOTÃO REGISTRAR
        xBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = xEditUsername.getText().toString();
                String email = xEditEmail.getText().toString();
                String password = xEditPassword.getText().toString();

                if (nome == null || nome.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
            }else {
                    createUser(v);
                }}
        });

        //LEVAR PARA A TELA DE LOGIN
        xTxtToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);}});

    }
//REGISTRAR USUARIO
    private void createUser(View v){
        //saveUsername();

        String nome = xEditUsername.getText().toString();
        String email = xEditEmail.getText().toString();
        String password = xEditPassword.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            SalvarDadosUser();

                            Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();

                            xProgressBar.setVisibility(View.VISIBLE);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    TelaPrincipal();
                                }
                            },3000);

                            //Tratamento de erros
                        }else {
                            String erro;
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e) {
                                erro = "Insira uma senha com no mínimo 6 digitos!";
                            }catch (FirebaseAuthUserCollisionException e) {
                                erro = "Email inserido já existe!";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                erro = "Email inválido";
                            }catch (Exception e){
                                erro = "Erro ao cadastrar usuário";}
                            Snackbar snackbar = Snackbar.make(v, erro,Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }
                    }
                });}
//SALVAR NOME DO USUARIO
    private void SalvarDadosUser(){
        String nome = xEditUsername.getText().toString();

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = database.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("database", "Sucesso ao salvar dados");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("database", "Falha ao salvar dados" + e.toString());
                    }
                });
    }

    //IR PARA A MAIN
    private void TelaPrincipal(){
        Intent intent = new Intent(RegisterActivity.this,ConfigMenuActivity.class);
        startActivity(intent);
        finish();
    }
}

