package com.example.chatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ConfigMenuActivity extends AppCompatActivity {

    public TextView xTxtUsername, xTxtEmailUser;
    public AppCompatButton xBtnDesconect;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_menu);
        xBtnDesconect = findViewById(R.id.btnDesconect);
        xTxtUsername = findViewById(R.id.txtUsername);
        xTxtEmailUser = findViewById(R.id.txtEmailUser);

        //BOTÃO PARA DESLOGAR
        xBtnDesconect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Desconect();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    xTxtUsername.setText(documentSnapshot.getString("nome"));
                    xTxtEmailUser.setText(email);
                }
            }
        });
    }


    //DESCONECTAR
    private void Desconect(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ConfigMenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}