package com.zybooks.mobile2app;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

// Enhancement 3
//Settings Activity class will house all methods needed for existing setting options
// and could be potentially expanded to include other account settings/preferences.
public class SettingsActivity extends AppCompatActivity {

    private int userId;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);

        setContentView(R.layout.activity_settings);
        Button exportButton = findViewById(R.id.exportButton);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        exportButton.setOnClickListener(v -> {
            exportEvents();
        });
    }

    private void exportEvents() {

        Cursor cursor = dbHelper.getEventsForUser(userId);

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No event's available to export.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder csvBuilder = new StringBuilder();

        csvBuilder.append("Title,Date,Type\n");

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EVENT_TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EVENT_DATE));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EVENT_TYPE));

            csvBuilder.append(title).append(",").append(date).append(",").append(type).append("\n");
        }

        cursor.close();

        saveCSVToFile(csvBuilder.toString());
    }

    private void saveCSVToFile(String csvData) {
        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File file = new File(path, "Mobile2App_Events.csv");

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(csvData.getBytes());
            fos.close();

            Toast.makeText(this, "Events Downloaded.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Export failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
