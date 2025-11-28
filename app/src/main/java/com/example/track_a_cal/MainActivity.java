package com.example.track_a_cal;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Breakfast button listener
        Button breakfastBtn = findViewById(R.id.btnQuickAddBreakfast);
        breakfastBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, FoodEntryActivity.class);
            startActivity(intent);
            finish();
        });

        // Lunch button listener
        Button lunchBtn = findViewById(R.id.btnQuickAddLunch);
        lunchBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, FoodEntryActivity.class);
            startActivity(intent);
            finish();
        });

        // Dinner button listener
        Button dinnerBtn = findViewById(R.id.btnQuickAddDinner);
        dinnerBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, FoodEntryActivity.class);
            startActivity(intent);
            finish();
        });

        // Snack button listener
        Button snackBtn = findViewById(R.id.btnQuickAddSnack);
        snackBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, FoodEntryActivity.class);
            startActivity(intent);
            finish();
        });

    }




}