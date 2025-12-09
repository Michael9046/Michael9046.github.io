---
layout: default
title: Enhancement 2
---

## Narrative
  This enhancement incorporated Enhancement 1 into it's search function.  I wanted each enhancement to improve the user experience while demonstrating course outcomes.  The application was lacking a search function for users.  Before this enhancement they had to manually search on the calendar view for their events.  This was the perfect feature to demonstrate my skills working with algorithms and data structures. 
  
  Using Android’s built in SQLite Database I had to create my own queries using an array.  This allowed me to account for different amounts of options in the type field or title field when the user searches an event.  The Android developer site offers a lot of explanations and starting points on working with this.  
  
  Going into this enhancement I believed it would be a simple query building step like I have experienced in previous courses working with MongoDB or MySQL.  I assumed writing one long query for the search function and checking each filter would work.  Like how I created my checkUser method in the databasehelper.  The difference there though was the query always expected two arguments, there was never a time where one or both are missing.

  When implementing my searchEvents originally, I noticed it would fail when I wouldn’t select all the search fields.  I learned that SQLite’s rawQuery always requires an argument for each placeholder if I use one query.  So, I decided to create a query that stores user selection in an array to use in the query so it can account for different selections and even empty ones. 

```java
//Search query used in Enhancement 2
public Cursor searchEvents(String keyword, String type) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = " SELECT " + COL_EVENT_TITLE + ", "
                + COL_EVENT_TYPE +  " FROM " + TABLE_EVENTS +
                " WHERE 1=1 ";

        java.util.ArrayList<String> queryArgs = new java.util.ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            query += " AND " + COL_EVENT_TITLE + " LIKE ?";
            queryArgs.add("%" + keyword + "%");
        }

        if (type != null && !type.equals("Any")) {
            query += " AND " + COL_EVENT_TYPE + " = ? ";
            queryArgs.add(type);
        }

        String[] argsArray = queryArgs.toArray(new String[0]);

        return db.rawQuery(query, argsArray);

    }

```

---
<p align="center">
  <img src="/assets/images/Search.png" alt="Search Form" width="60%">
</p>


  This code and more for the search function can be found in the Repository under the Mobile2App source code under databasehelper.java, searchActivity.java, and activity_search.xml

---

### Next Page  
-> [Go to Enhancement 3 — Exporting User Events](enhancement3.md)
