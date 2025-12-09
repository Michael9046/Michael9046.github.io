---
layout: default
title: Enhancement 2
---

## Narrative
  For this enhancement I implemented a feature in a new settings activity to export the user’s saved events from the Database.  
  
  This displays capability of implementing data security practices while working with Android and SQLite databases to export and back up data.  Exporting sensitive data can sometimes protect it from unwanted access and keep it's integrity intact in case a system is compromised.
  
  I have touched on writing to and reading from a file in previous courses, but I did not recall using it with a database so this enhancement was a little more challenging.
  
  Creating the layout xml for the new settings page and java class was easy and basically a repeat of all the other Activity classes I’ve created for this Capstone and my CS 360 class.  When it came to the actual export method, I kept the SQL query in my Database helper but struggled on how to actually implement the query method in my export method and a good way to go about that.  It led me to a stack overflow page where people offered suggestions on how to convert SQLite to a CSV file. Where they used a single raw query I figured I’d use my own query method but followed their same design when formatting the file with the specific columns of the events database I wanted.  
  
  I also learned about using android’s Directory Downloads environment to save files from and I figured housing this in a separate method would be cleaner than putting it all into the same Export method.

```java
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

```
---

The code for this enhancement can be found in the Mobile2App source code under settingsActivity.java, and activity_settings.xml

---

### Navigation
<- [Return to Homepage](/index.md)

-> [View Full Source Code Repository →](https://github.com/Michael9046/Michael9046.github.io)
