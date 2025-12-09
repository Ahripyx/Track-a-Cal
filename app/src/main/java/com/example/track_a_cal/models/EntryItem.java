package com.example.track_a_cal.models;

public class EntryItem {

    // Private Properties
    private int id;
    private final String date;
    private final String title;
    private final int calories;
    private final int protein;
    private final int carbs;
    private final int fat;
    private final String serving;
    private final String mealCsv;

    // Public Properties
    public int getId() { return id; }
    public String getDate() { return date; }
    public String getTitle() { return title; }
    public int getCalories() { return calories; }
    public int getProtein() { return protein; }
    public int getCarbs() { return carbs; }
    public int getFat() { return fat; }
    public String getServing() { return serving; }
    public String getMealCsv() { return mealCsv; }

    public void setId(int id) { this.id = id; }

    public EntryItem(int id, String date, String title, int calories, int protein, int carbs, int fat, String serving, String mealCsv) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.serving = serving;
        this.mealCsv = mealCsv;
    }

    public EntryItem(String date, String title, int calories, int protein, int carbs, int fat, String serving, String mealCsv) {
        this(-1, date, title, calories, protein, carbs, fat, serving, mealCsv);
    }

}
