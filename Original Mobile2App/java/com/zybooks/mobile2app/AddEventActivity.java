package com.zybooks.mobile2app;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;

// Uses the activity_event_form to add events to database
public class AddEventActivity extends AppCompatActivity {

    private EditText titleField, dateField, timeField, notesField;
    private DatabaseHelper dbHelper;
    private int userId;
    private android.widget.RadioGroup radioGroupType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);

        // Uses fields from activity_event_form
        titleField = findViewById(R.id.eventTitle);
        dateField = findViewById(R.id.eventDate);
        timeField = findViewById(R.id.eventTime);
        notesField = findViewById(R.id.eventNotes);
        //Enhancement 1 Type Field
        radioGroupType = findViewById(R.id.radioGroupType);
        Button saveButton = findViewById(R.id.saveEventButton);

        dbHelper = new DatabaseHelper(this);

        // Uses user ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        // Logic for the saveEventButton
        saveButton.setOnClickListener(v -> saveEvent());
    }

    private void saveEvent() {
        String title = titleField.getText().toString().trim();
        String date = dateField.getText().toString().trim();
        String time = timeField.getText().toString().trim();
        String notes = notesField.getText().toString().trim();

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Title and date are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Enhancement 1 Sets Event Type when saving/defaults to Misc if none selected
        int selectedId = radioGroupType.getCheckedRadioButtonId();
        String eventType;

        if (selectedId = R.id.radioBusiness) {
            eventType = "Business";
        }
        else if (selectedId = R.id.radioPersonal) {
            eventType = "Personal";
        }
        else {
            eventType = "Misc";
        }



        String fullDate = date;
        Log.d("SAVE_EVENT", "Saving event with date: " + fullDate);


        // Adds event to the Table_Events Database using background Thread
        Executors.newSingleThreadExecutor().execute(() -> {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_EVENT_TITLE, title);
            values.put(DatabaseHelper.COL_EVENT_DATE, fullDate);
            values.put(DatabaseHelper.COL_EVENT_USER_ID, userId);
            values.put(DatabaseHelper.COL_EVENT_TYPE, eventType);

            long result;
            try {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                result = db.insert(DatabaseHelper.TABLE_EVENTS, null, values);
                db.close(); //
            } catch (Exception e) {
                result = -1;
            }

            long finalResult = result;

            runOnUiThread(() -> {
                if (finalResult != -1) {
                    Toast.makeText(this, "Event saved!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error saving event.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

}
