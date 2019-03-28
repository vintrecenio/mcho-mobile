package com.mcho.recipient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mcho.recipient.utils.SharedPrefs;

public class SplashActivity extends AppCompatActivity {

    private SharedPrefs sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sf = new SharedPrefs(this);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if(sf.getToken() != ""){
                proceedToMain();
            }else{
                proceedToLogin();
            }
        }, 3000);
    }

    private void proceedToLogin(){
        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void proceedToMain(){
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
