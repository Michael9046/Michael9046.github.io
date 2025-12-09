package com.zybooks.mobile2app;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executors;

public class SearchActivity extends AppCompatActivity {
    private EditText searchTitle;
    private RadioGroup radioGroupType;
    private LinearLayout resultsLayout;
    private DatabaseHelper dbHelper;

    @Override
    protected void OnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchTitle = findViewById(R.id.searchTitle);
        radioGroupType = findViewById(R.id.radioGroupType);
        resultsLayout = findViewById(R.id.resultsLayout);
        Button searchButton = findViewById(R.id.searchButton);

        dbHelper = new DatabaseHelper(this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    //Uses database function in DatabaseHelper to find events
    private void performSearch() {
        final String keyword = searchTitle.getText().toString().trim();

        String type;
        int selectedId = radioGroupType.getCheckedRadioButtonId();

        if (selectedId == R.id.radioBusiness) {
            type = "Business";
        } else if (selectedId == R.id.radioPersonal) {
            type = "Personal";
        } else if (selectedId == R.id.radioMisc) {
            type = "Misc";
        } else {
            type = "Any";
        }
        //Again uses background thread for database query
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = dbHelper.searchEvents(keyword, type);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayResults(cursor);
                    }
                });
            }
        });
    }

    //Displays results of search based on title and/or type.
    private void displayResults(Cursor cursor) {

        resultsLayout.removeAllViews();

        if (cursor.getCount() == 0) {
                TextView noResults = new TextView(this);
                noResults.setText("No matching events.");
                noResults.setTextSize(18);
                resultsLayout.addView(noResults);
                cursor.close();
                return;
        }

        while (cursor.moveToNext()) {

            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EVENT_TITLE)
            );
            String eventType = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EVENT_TYPE)
            );

                TextView tv = new TextView(this);
                tv.setText("● " + title);
                tv.setTextSize(18);

            }

        cursor.close();
    }

}

