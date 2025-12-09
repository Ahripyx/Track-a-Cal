package com.example.track_a_cal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.track_a_cal.data.DatabaseHelper;
import com.example.track_a_cal.models.EntryItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FoodEntryActivity extends AppCompatActivity {

    private EditText etFoodName, etCalories, etProtein, etCarbs, etFat, etServingSize;
    private CheckBox cbBreakfast, cbLunch, cbDinner, cbSnack;
    private Button btnSaveBtn, btnBack;
    private TextView tvTitle;

    private String date; // yyyy-MM-dd
    private DatabaseHelper dbHelper;


    // Setting up the UI, preselect a meal if requested, and wire buttons
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);

        // Views
        tvTitle = findViewById(R.id.tvFoodEntryTitle);
        etFoodName = findViewById(R.id.etFoodName);
        etCalories = findViewById(R.id.etCalories);
        etProtein = findViewById(R.id.etProtein);
        etCarbs = findViewById(R.id.etCarbs);
        etFat = findViewById(R.id.etFat);
        etServingSize = findViewById(R.id.etServingSize);

        cbBreakfast = findViewById(R.id.cbBreakfast);
        cbLunch = findViewById(R.id.cbLunch);
        cbDinner = findViewById(R.id.cbDinner);
        cbSnack = findViewById(R.id.cbSnack);

        btnSaveBtn = findViewById(R.id.btnSaveEntry);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DatabaseHelper(this);

        // Retrieve extras
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        if (date == null || date.isEmpty()) {
            date = isoToday();
        }

        String pre = intent.getStringExtra("preselectMeal");
        if (pre != null) {
            switch (pre) {
                case "Breakfast": cbBreakfast.setChecked(true); break;
                case "Lunch": cbLunch.setChecked(true); break;
                case "Dinner": cbDinner.setChecked(true); break;
                case "Snack": cbSnack.setChecked(true); break;
            }
        }

        // show date in the title so user knows which date they're adding to
        String appName;
        try { appName = getString(R.string.app_name); } catch (Exception ex) { appName = "Calorie Tracker"; }
        tvTitle.setText(appName + " - " + date);

        btnBack.setOnClickListener(e -> finish());

        // Save handler
        btnSaveBtn.setOnClickListener(e -> {
            saveEntryToDb();
        });
    }

    // Validating inputs, building an EntryItem, and saving it to the database
    private void saveEntryToDb() {
        String title = etFoodName.getText().toString().trim();
        String calText = etCalories.getText().toString().trim();

        if (calText.isEmpty()) {
            Toast.makeText(this, "Please enter calories.", Toast.LENGTH_SHORT).show();
            return;
        }

        int calories;
        try {
            calories = Integer.parseInt(calText);
            if (calories < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Invalid calories value.", Toast.LENGTH_SHORT).show();
            return;
        }

        int protein = parseIntSafe(etProtein.getText().toString().trim());
        int carbs = parseIntSafe(etCarbs.getText().toString().trim());
        int fat = parseIntSafe(etFat.getText().toString().trim());
        String serving = etServingSize.getText().toString().trim();

        // collect meals CSV
        StringBuilder meals = new StringBuilder();
        if (cbBreakfast.isChecked()) meals.append("Breakfast");
        if (cbLunch.isChecked()) appendWithComma(meals, "Lunch");
        if (cbDinner.isChecked()) appendWithComma(meals, "Dinner");
        if (cbSnack.isChecked()) appendWithComma(meals, "Snack");

        if (meals.length() == 0) {
            Toast.makeText(this, "Please select at least one meal type.", Toast.LENGTH_SHORT).show();
            return;
        }

        EntryItem e = new EntryItem(date, title, calories, protein, carbs, fat, serving, meals.toString());
        long id = dbHelper.insertEntry(e);
        if (id > 0) {
            e.setId((int) id);
            Toast.makeText(this, "Entry saved.", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Failed to save entry.", Toast.LENGTH_SHORT).show();
        }
    }

    private int parseIntSafe(String s) {
        if (s == null || s.isEmpty()) return 0;
        try { return Integer.parseInt(s); } catch (NumberFormatException ex) { return 0; }
    }

    private void appendWithComma(StringBuilder sb, String s) {
        if (sb.length() > 0) sb.append(",");
        sb.append(s);
    }

    // Return today's date in ISO yyyy-MM-dd format
    private String isoToday() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }
}
