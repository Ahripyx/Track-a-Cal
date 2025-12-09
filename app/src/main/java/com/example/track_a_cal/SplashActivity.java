package com.example.track_a_cal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String PREFS = "trackacal_prefs";
    private static final String KEY_PRIVACY_AGREED = "privacy_agreed";

    // Simple splash screen activity shown briefly on app start
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Setting the layout and going to LoadingActivity after a short delay
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoadingActivity.class);
            startActivity(intent);
            finish();
        }, 3000);

    }
}
