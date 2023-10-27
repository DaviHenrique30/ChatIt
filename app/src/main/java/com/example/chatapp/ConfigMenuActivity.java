package com.example.chatapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.utils.AndroidUtil;
import com.example.chatapp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ConfigMenuActivity extends AppCompatActivity {

    public ImageView xUserPicture;
    public TextView xTxtUsername, xTxtEmailUser, xTxtDisconnect;
    public AppCompatButton xBtnSave;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectImageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_menu);
        getWindow().setStatusBarColor(ContextCompat.getColor(ConfigMenuActivity.this,R.color.blue));
        Toolbar toolbar = findViewById(R.id.toolbar);


        xBtnSave = findViewById(R.id.btnSave);
        xTxtUsername = findViewById(R.id.txtUsername);
        xTxtEmailUser = findViewById(R.id.txtEmailUser);
        xTxtDisconnect = findViewById(R.id.txtDisconnect);
        xUserPicture = findViewById(R.id.userPicture);


        //PEGAR URL DA FOTO
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
            if(result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                if(data != null && data.getData() != null){
                    selectImageUri = data.getData();
                    AndroidUtil.setProfilePic(getBaseContext(), selectImageUri, xUserPicture);
                }
            }
                }
                );


        //AO CLICAR NA IMAGEVIEW
        xUserPicture.setOnClickListener((v)->{
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });

        //TEXTO PARA DESLOGAR
        xTxtDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Desconect();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


//PUXAR EMAIL E NOME
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

    //SALVAR FOTO
    private void UpdateBtnClick(){
        if(selectImageUri != null) {
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectImageUri)
                    .addOnSuccessListener(task ->{
                       // UpdatetoFirestore();
                    });
        }
    }

/*    private void UpdatetoFirestore() {
        FirebaseUtil.currentUserDetails().set(currentUserModel)
                .addOnCompleteListener(task ->{
                    set
                })
    }*/

}