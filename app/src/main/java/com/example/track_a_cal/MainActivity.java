package com.example.track_a_cal;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.track_a_cal.data.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static final String PREFS = "trackacal_prefs";
    private static final String KEY_LAST_DATE = "last_date";
    private static final String KEY_DAILY_GOAL = "daily_goal";
    private static final int DEFAULT_DAILY_GOAL = 2000;

    private DatabaseHelper db;
    private String currentDate;

    private TextView tvAppTitle;
    private TextView tvDailyGoalVal, tvRemainingCalValue;
    private ProgressBar progressBarCalories;
    private TextView tvBreakfastCalories, tvBreakfastFoods;
    private TextView tvLunchCalories, tvLunchFoods;
    private TextView tvDinnerCalories, tvDinnerFoods;
    private TextView tvSnacksCalories, tvSnacksFoods;
    private Button btnQuickAddBreakfast, btnQuickAddLunch, btnQuickAddDinner, btnQuickAddSnack;
    private Button btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        // Views
        tvAppTitle = findViewById(R.id.tvAppTitle);
        tvDailyGoalVal = findViewById(R.id.tvDailyGoalVal);
        tvRemainingCalValue = findViewById(R.id.tvRemainingCalValue);
        progressBarCalories = findViewById(R.id.progressBarCalories);

        tvBreakfastCalories = findViewById(R.id.tvBreakfastCalories);
        tvBreakfastFoods = findViewById(R.id.tvBreakfastFoods);
        tvLunchCalories = findViewById(R.id.tvLunchCalories);
        tvLunchFoods = findViewById(R.id.tvLunchFoods);
        tvDinnerCalories = findViewById(R.id.tvDinnerCalories);
        tvDinnerFoods = findViewById(R.id.tvDinnerFoods);
        tvSnacksCalories = findViewById(R.id.tvSnacksCalories);
        tvSnacksFoods = findViewById(R.id.tvSnacksFoods);

        btnQuickAddBreakfast = findViewById(R.id.btnQuickAddBreakfast);
        btnQuickAddLunch = findViewById(R.id.btnQuickAddLunch);
        btnQuickAddDinner = findViewById(R.id.btnQuickAddDinner);
        btnQuickAddSnack = findViewById(R.id.btnQuickAddSnack);
        btnHistory = findViewById(R.id.btnHistory);

        // Restore last date or default to today
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        currentDate = prefs.getString(KEY_LAST_DATE, isoToday());
        int dailyGoal = prefs.getInt(KEY_DAILY_GOAL, DEFAULT_DAILY_GOAL);

        updateTitleWithDate();

        tvDailyGoalVal.setText(String.valueOf(dailyGoal));
        progressBarCalories.setMax(dailyGoal);

        // populate screen
        refreshAll();

        // Title click to pick date
        tvAppTitle.setOnClickListener(v -> showDatePicker());

        btnHistory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));

        // Quick add buttons: open FoodEntryActivity with the meal preselected
        btnQuickAddBreakfast.setOnClickListener(e -> startFoodEntryWithMeal("Breakfast"));
        btnQuickAddLunch.setOnClickListener(e -> startFoodEntryWithMeal("Lunch"));
        btnQuickAddDinner.setOnClickListener(e -> startFoodEntryWithMeal("Dinner"));
        btnQuickAddSnack.setOnClickListener(e -> startFoodEntryWithMeal("Snack"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Read stored last_date in case HistoryActivity changed it
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String storedDate = prefs.getString(KEY_LAST_DATE, isoToday());
        if (!storedDate.equals(currentDate)) {
            currentDate = storedDate;
            updateTitleWithDate();
        }

        // refresh totals (in case entries were added/edited)
        refreshAll();

        // persist last date (keeps behavior consistent)
        prefs.edit().putString(KEY_LAST_DATE, currentDate).apply();
    }

    private void updateTitleWithDate() {
        String appName;
        try {
            appName = getString(R.string.app_name);
        } catch (Exception ex) {
            appName = "Calorie Tracker";
        }
        tvAppTitle.setText(appName + " - " + currentDate);
    }

    private void refreshAll() {
        // Total calories for the date
        int total = db.getTotalCaloriesForDate(currentDate);

        int goal = getSharedPreferences(PREFS, MODE_PRIVATE).getInt(KEY_DAILY_GOAL, DEFAULT_DAILY_GOAL);
        int remaining = Math.max(0, goal - total);

        // update top values
        tvDailyGoalVal.setText(String.valueOf(goal));
        tvRemainingCalValue.setText(String.valueOf(remaining));
        progressBarCalories.setMax(goal);
        progressBarCalories.setProgress(Math.min(total, goal));

        // per-meal totals and food lists
        updateMealSection("Breakfast", tvBreakfastCalories, tvBreakfastFoods);
        updateMealSection("Lunch", tvLunchCalories, tvLunchFoods);
        updateMealSection("Dinner", tvDinnerCalories, tvDinnerFoods);
        updateMealSection("Snack", tvSnacksCalories, tvSnacksFoods);
    }

    private void updateMealSection(String meal, TextView tvCalories, TextView tvFoods) {
        int totalMeal = 0;
        String foods = "";
        try {
            totalMeal = db.getTotalCaloriesForMeal(currentDate, meal);
            foods = db.getFoodsForMeal(currentDate, meal);
        } catch (Exception ex) {
            //ADD LATER: toast to tell user about error
        }
        tvCalories.setText("Calories: " + totalMeal);
        tvFoods.setText(foods);
    }

    private void startFoodEntryWithMeal(String meal) {
        Intent intent = new Intent(this, FoodEntryActivity.class);
        intent.putExtra("date", currentDate);
        intent.putExtra("preselectMeal", meal);
        startActivity(intent);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            c.setTime(f.parse(currentDate));
        } catch (Exception ignored) {}

        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar sel = Calendar.getInstance();
            sel.set(year, month, dayOfMonth);
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            currentDate = f.format(sel.getTime());
            updateTitleWithDate();
            refreshAll();
        }, y, m, d);
        dp.show();
    }

    private String isoToday() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }
}