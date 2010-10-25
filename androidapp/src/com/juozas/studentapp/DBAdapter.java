package com.juozas.studentapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

// Idea from http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/comment-page-1/
public class DBAdapter 
{	
    private DataBaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        DBHelper = new DataBaseHelper(ctx);
    }  
    
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getDatabase();
        return this;
    }
  
    public void close() 
    {
        DBHelper.close();
    }
    
    public Cursor getAllCourses(String search) 
    {
        return db.query("courses", new String[] {"key", "title"}, 
                "title LIKE ?", 
                new String[] { "%" + search + "%"}, 
                null, 
                null, 
                "title asc");
    }
    
    public Cursor getCourse(String id) 
    {
    	Cursor mCursor = db.query(true, "courses", new String[] {"key", "title", "location"}, 
            		"key = ?", 
            		new String[] { id },
            		null, 
            		null, 
            		null, 
            		null);
    	
	    if (mCursor != null) {
	        mCursor.moveToFirst();
	    }
	    return mCursor;
    }
}
