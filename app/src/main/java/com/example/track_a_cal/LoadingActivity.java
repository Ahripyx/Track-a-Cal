package com.example.track_a_cal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        // Adding 3 second delay
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(LoadingActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
            finish();
        }, 3000);

    }
}
