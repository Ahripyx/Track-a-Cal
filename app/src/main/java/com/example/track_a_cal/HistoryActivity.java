package com.example.track_a_cal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.track_a_cal.data.DatabaseHelper;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static final String PREFS = "trackacal_prefs";
    private static final String KEY_LAST_DATE = "last_date";

    private DatabaseHelper db;
    private ListView lvDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = new DatabaseHelper(this);
        lvDates = findViewById(R.id.lvDates);

        List<String> dates = db.getDistinctDates();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dates);
        lvDates.setAdapter(adapter);

        lvDates.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            String date = dates.get(position);
            SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
            prefs.edit().putString(KEY_LAST_DATE, date).apply();

            Toast.makeText(this, "Switched to " + date, Toast.LENGTH_SHORT).show();

            finish();
        });
    }
}
