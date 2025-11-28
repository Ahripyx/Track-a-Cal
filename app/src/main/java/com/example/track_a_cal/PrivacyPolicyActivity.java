package com.example.track_a_cal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        CheckBox agreeChk = findViewById(R.id.checkbox_agree);
        Button acceptBtn = findViewById(R.id.button_accept);

        acceptBtn.setOnClickListener(e -> {
            if (agreeChk.isChecked()) {
                // Save acceptance here later to SQL

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else{
                Toast.makeText(this, "You must agree to the privacy policy to continue.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
