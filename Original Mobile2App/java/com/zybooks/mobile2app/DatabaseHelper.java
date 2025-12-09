package com.zybooks.mobile2app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {
// Initializes all variables to be used in databases
    private static final String DATABASE_NAME = "Mobile2App.db";
    private static final int VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    public static final String TABLE_EVENTS = "events";
    public static final String COL_EVENT_ID = "id";
    public static final String COL_EVENT_TITLE = "title";
    public static final String COL_EVENT_DATE = "date";
    public static final String COL_EVENT_TYPE = "type";
    public static final String COL_EVENT_USER_ID = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Creates tables for User Credentials and for events.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "create table " + TABLE_USERS + " (" +
                COL_USER_ID + " integer primary key autoincrement, " +
                COL_USERNAME + " text unique, " +
                COL_PASSWORD + " text" + ")";

        String CREATE_EVENTS_TABLE = "create table " + TABLE_EVENTS + " (" +
                COL_EVENT_ID + " integer primary key autoincrement, " +
                COL_EVENT_TITLE + " text, " +
                COL_EVENT_DATE + " text, " +
                // Created TYPE Column for Enhancement 1
                COL_EVENT_TYPE + " text, " +
                COL_EVENT_USER_ID + " integer, " +
                "foreign key(" + COL_EVENT_USER_ID + ") references " + TABLE_USERS + "(" + COL_USER_ID + "))";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_EVENTS);
        db.execSQL("drop table if exists " + TABLE_USERS);
        onCreate(db);
    }

    // Creates new user & checks for existing ones
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        //TODO Hash passwords in DB.

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Checks user credentials for login
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + TABLE_USERS + " where " +
                COL_USERNAME + " = ? and " + COL_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    // Formats the date from the calenderView library to use in queries.
    public static String formatDate(int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy", Locale.US);
            return sdf.format(cal.getTime());
    }

    // TODO export option for events.


}