package com.example.track_a_cal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private static final String PREFS = "trackacal_prefs";
    private static final String KEY_PRIVACY_AGREED = "privacy_agreed";

    // Displaying the loading UI, wait a bit, then open Main or PrivacyPolicy based on consent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
            boolean agreed = prefs.getBoolean(KEY_PRIVACY_AGREED, false);

            Intent intent;
            if (agreed){
                intent = new Intent(LoadingActivity.this, MainActivity.class);
            }
            else{
                intent = new Intent(LoadingActivity.this, PrivacyPolicyActivity.class);
            }

            startActivity(intent);
            finish();
        }, 3000);

    }
}
