package com.example.chatapp;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class BiometricLogin extends AppCompatActivity {

    private static final int REQUEST_CODE = 101010;

    private ImageView imgFingerprint;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric_login);

        imgFingerprint = findViewById(R.id.imgBiometria);



        //BIOMETRIA 1
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "Sensor de biometria não existe", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "Sensor de biometria não disponivel", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
        }

        Biometria();
    }

    private void Biometria(){
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(BiometricLogin.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                TelaPrincipal();
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        imgFingerprint.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }

    private void TelaPrincipal(){
        Intent intent = new Intent(BiometricLogin.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}

