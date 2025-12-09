package com.example.track_a_cal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private static final String PREFS = "trackacal_prefs";
    private static final String KEY_PRIVACY_AGREED = "privacy_agreed";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        TextView tvBody = findViewById(R.id.tv_privacy_policy_body);
        CheckBox agreeChk = findViewById(R.id.checkbox_agree);
        Button acceptBtn = findViewById(R.id.button_accept);

        // full privacy policy text shown to the user
        StringBuilder sb = new StringBuilder();
        sb.append("Privacy Policy - Track-a-Cal\n\n");
        sb.append("Date: 2025-12-09\n\n");
        sb.append("Please read this policy carefully. If you have questions, see the Contact section near the end.\n\n");
        sb.append("Summary\n");
        sb.append("Track-a-Cal stores all food entries and settings locally on your device (SQLite database / SharedPreferences)\n");
        sb.append("The app does not create an account, does not send your food entries to a server, and does not share your data with third parties by default.\n");
        sb.append("If optional features such as cloud backup or analytics are added in the future, we will update this policy and make that clearly visible in the app.\n\n");

        sb.append("What Data the App Collects\n\n");
        sb.append("Track-a-Cal stores the following information locally on your device when you use the app:\n\n");
        sb.append("Food/Entry Data You Enter\n");
        sb.append("- Date\n");
        sb.append("- Title / description\n");
        sb.append("- Calories\n");
        sb.append("- Optional nutrition fields (protein, carbs, fat)\n");
        sb.append("- Serving size\n");
        sb.append("- Meal tags\n\n");

        sb.append("App Preferences\n");
        sb.append("- Daily calorie goal\n");
        sb.append("- Flag for first-time privacy agreement\n\n");

        sb.append("Technical Data Created by the App\n");
        sb.append("- Local SQLite database file.\n");
        sb.append("- No remote logs, analytics, crash reports, or diagnostics are collected or transmitted.\n\n");

        sb.append("How the App Uses Your Data\n");
        sb.append("- To show and manage your daily calorie totals and meal lists\n");
        sb.append("- To calculate totals and per-meal summaries\n");
        sb.append("- To persist your preferences\n\n");

        sb.append("Sharing and Third Parties\n");
        sb.append("By default, Track-a-Cal does not share your data with anyone.\n");
        sb.append("If optional cloud backup, analytics, or crash-reporting features are added, they will be listed in-app and this Privacy Policy will be updated to explain what is shared, for what purpose, and how to opt out.\n\n");

        sb.append("Storage & Security\n");
        sb.append("All data is stored locally in the app’s private storage.\n");
        sb.append("On a standard Android device, other apps cannot access this private data unless the device is rooted or the user explicitly grants permission.\n");
        sb.append("The app does not transmit your entries to remote servers by default.\n");
        sb.append("You are responsible for securing your device to protect locally stored data.\n\n");

        sb.append("Deleting Data\n");
        sb.append("Uninstalling the app removes its private data.\n\n");

        sb.append("Changes to This Policy\n");
        sb.append("This Privacy Policy may be updated from time to time.\n");
        sb.append("If changes are made, the app will display a notice and the date above will be updated.\n\n");

        sb.append("Contact\n");
        sb.append("If you have questions about this policy, need help deleting data, or want to request changes, contact:\n\n");
        sb.append("Email: track-a-cal@gmail.com\n");

        // Setting the built text on the TextView
        tvBody.setText(sb.toString());

        // When Accept is tapped, save consent and move to the main screen
        acceptBtn.setOnClickListener(e -> {
            if (agreeChk.isChecked()) {
                // Saving the privacy acceptance
                SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
                prefs.edit().putBoolean(KEY_PRIVACY_AGREED, true).apply();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else{
                Toast.makeText(this, "You must agree to the privacy policy to continue.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
