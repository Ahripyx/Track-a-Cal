package com.example.track_a_cal.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.track_a_cal.models.EntryItem;

import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "trackacal.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ENTRIES = "entries";
    public static final String COL_ID = "_id";
    public static final String COL_DATE = "date"; // yyyy-MM-dd
    public static final String COL_TITLE = "title";
    public static final String COL_CALORIES = "calories";
    public static final String COL_PROTEIN = "protein";
    public static final String COL_CARBS = "carbs";
    public static final String COL_FAT = "fat";
    public static final String COL_SERVING = "serving";
    public static final String COL_MEALS = "meals";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_ENTRIES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_DATE + " TEXT NOT NULL," +
                COL_TITLE + " TEXT," +
                COL_CALORIES + " INTEGER," +
                COL_PROTEIN + " INTEGER," +
                COL_CARBS + " INTEGER," +
                COL_FAT + " INTEGER," +
                COL_SERVING + " TEXT," +
                COL_MEALS + " TEXT" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        onCreate(db);
    }

    // Insert an entry and return new row id
    public long insertEntry(EntryItem e) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_DATE, e.getDate());
        cv.put(COL_TITLE, e.getTitle());
        cv.put(COL_CALORIES, e.getCalories());
        cv.put(COL_PROTEIN, e.getProtein());
        cv.put(COL_CARBS, e.getCarbs());
        cv.put(COL_FAT, e.getFat());
        cv.put(COL_SERVING, e.getServing());
        cv.put(COL_MEALS, e.getMealCsv());
        long id = db.insert(TABLE_ENTRIES, null, cv);
        db.close();
        return id;
    }

    // Get all entries for a given date
    public List<EntryItem> getEntriesForDate(String isoDate) {
        List<EntryItem> out = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_ENTRIES, null, COL_DATE + "=?", new String[]{isoDate}, null, null, COL_ID + " ASC");
        if (c.moveToFirst()) {
            do {
                EntryItem e = new EntryItem(
                        c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                        c.getString(c.getColumnIndexOrThrow(COL_DATE)),
                        c.getString(c.getColumnIndexOrThrow(COL_TITLE)),
                        c.getInt(c.getColumnIndexOrThrow(COL_CALORIES)),
                        c.getInt(c.getColumnIndexOrThrow(COL_PROTEIN)),
                        c.getInt(c.getColumnIndexOrThrow(COL_CARBS)),
                        c.getInt(c.getColumnIndexOrThrow(COL_FAT)),
                        c.getString(c.getColumnIndexOrThrow(COL_SERVING)),
                        c.getString(c.getColumnIndexOrThrow(COL_MEALS))
                );
                out.add(e);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return out;
    }

    // Sum calories for a date
    public int getTotalCaloriesForDate(String isoDate) {
        int total = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(" + COL_CALORIES + ") FROM " + TABLE_ENTRIES + " WHERE " + COL_DATE + "=?", new String[]{isoDate});
        if (c.moveToFirst()) {
            total = c.isNull(0) ? 0 : c.getInt(0);
        }
        c.close();
        db.close();
        return total;
    }

    // Sum calories for a date and a specific meal
    public int getTotalCaloriesForMeal(String isoDate, String meal) {
        int total = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[] args = new String[]{isoDate, "%" + meal + "%"};
        Cursor c = db.rawQuery("SELECT SUM(" + COL_CALORIES + ") FROM " + TABLE_ENTRIES + " WHERE " + COL_DATE + "=? AND " + COL_MEALS + " LIKE ?", args);
        if (c.moveToFirst()) {
            total = c.isNull(0) ? 0 : c.getInt(0);
        }
        c.close();
        db.close();
        return total;
    }

    // Get a simple bullet list of food titles for a given date and meal
    public String getFoodsForMeal(String isoDate, String meal) {
        StringBuilder sb = new StringBuilder();
        SQLiteDatabase db = getReadableDatabase();
        String[] args = new String[]{isoDate, "%" + meal + "%"};
        Cursor c = db.query(TABLE_ENTRIES, null, COL_DATE + "=? AND " + COL_MEALS + " LIKE ?", args, null, null, COL_ID + " ASC");
        if (c.moveToFirst()) {
            do {
                String t = c.getString(c.getColumnIndexOrThrow(COL_TITLE));
                if (t == null || t.trim().isEmpty()) continue;
                if (sb.length() > 0) sb.append("\n");
                sb.append("• ").append(t);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return sb.toString();
    }

    public List<String> getDistinctDates() {
        List<String> out = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT " + COL_DATE + " FROM " + TABLE_ENTRIES + " ORDER BY " + COL_DATE + " DESC", null);
        if (c.moveToFirst()) {
            do {
                out.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return out;
    }
}
