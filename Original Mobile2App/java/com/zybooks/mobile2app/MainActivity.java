package com.zybooks.mobile2app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private LinearLayout eventListLayout;
    private DatabaseHelper dbHelper;
    private String selectedDate = "";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendarView = findViewById(R.id.calendarView);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        eventListLayout = findViewById(R.id.eventListLayout);
        dbHelper = new DatabaseHelper(this);

       // Formats date from CalendarView when clicking dates.
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int month, int day, int year) {
                selectedDate = (month + 1) + "/" + day + "/" + year;
                String formattedDate = DatabaseHelper.formatDate(month, day, year);
                loadEventsForDate(formattedDate);
            }
        });

        // Uses selected Calendar date when selecting the FAB to create an event.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                intent.putExtra("selected_date", selectedDate);
                startActivity(intent);
            }
        });
    }

    // Searches for title of events and prints them to eventLayout in a bulleted list format
    private void loadEventsForDate(String dateStr) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(
                    " SELECT " + DatabaseHelper.COL_EVENT_TITLE +  "," + DatabaseHelper.COL_EVENT_TYPE +
                            " FROM " + DatabaseHelper.TABLE_EVENTS +
                            " WHERE " + DatabaseHelper.COL_EVENT_DATE + " = ?",
                    new String[]{dateStr}
            );

            final StringBuilder eventsBuilder = new StringBuilder();
            final boolean[] hasEvents = {false};

            while (cursor.moveToNext()) {
                hasEvents[0] = true;
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EVENT_TITLE));
                eventsBuilder.append(title).append("\n");
            }

            cursor.close();
            db.close();

            mainHandler.post(() -> {
                eventListLayout.removeAllViews();

                if (hasEvents[0]) {
                    for (String line : eventsBuilder.toString().split("\n")) {
                        TextView eventText = new TextView(MainActivity.this);
                        eventText.setText("- " + line);
                        eventText.setTextSize(16);
                        eventListLayout.addView(eventText);
                    }
                } else {
                    TextView noEventsView = new TextView(MainActivity.this);
                    noEventsView.setText("No Events Scheduled");
                    noEventsView.setTextSize(16);
                    eventListLayout.addView(noEventsView);
                }
            });
        });
    }

}

