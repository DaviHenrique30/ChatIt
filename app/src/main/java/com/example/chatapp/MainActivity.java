package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.chatapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //toolbar things
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.blue));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ChatIt");


        initNavigation();
    }
    //TOOLBAR MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.item_perfil) {
            GotoPerfil();
        } else if (item.getItemId() == R.id.item_desconnect) {
            Desconect();
        }

        return true;
    }

    //NAVBAR MENU
    private void initNavigation(){
        navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
    }

    //DESCONECTAR
    private void Desconect(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //IR PARA O PERFIL
    private void GotoPerfil(){
        Intent intent = new Intent(MainActivity.this, ConfigMenuActivity.class);
        startActivity(intent);
    }

}