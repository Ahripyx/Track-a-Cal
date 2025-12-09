package com.example.track_a_cal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String PREFS = "trackacal_prefs";
    private static final String KEY_PRIVACY_AGREED = "privacy_agreed";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Waiting 3 seconds
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoadingActivity.class);
            startActivity(intent);
            finish();
        }, 3000);

    }
}
