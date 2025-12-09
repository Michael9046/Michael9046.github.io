---
layout: default
title: Enhancement 1
---

## Narrative
  The intent of this Enhancement was to show my ability and skill in Software Engineering & Design.
I believe I met the outcomes I planned on for this enhancement.  I was able to add a Type field to the Event’s table in the database.  This is paired with the Event’s Title, Time, Date, and any relevant Notes.  I also added and color coded the radio buttons where the user selects the types when creating an event.  I also added a feature to account for no selection being made.  The radio buttons were introduced to me in my CS 360 classs.  A quick study on Android design features helped me implement them.  I am still not sure if I should (or how to implement) color code the events in the EventsLayout section on the CalendarView page.  I may revisit it later when I come back to this project.

  This enhancement was pretty straight forward.  I added the Event Type Field to my already existing database
  and I updated the associated xml file.  Keeping the original design and working within those constraints.  
 
```java
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

```
  Following this I was able to add the type selection logic into the original saveEvent method.
This works together with the xml layout created for the Event Form.

```java
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
```


