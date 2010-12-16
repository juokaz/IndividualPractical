package com.juozas.studentapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

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
        
        try {
        	createCoursesTakingTable();
        } catch (Exception e)
        {
        	Log.d("DBAdapter", "Exception was thrown while creating a table" + e.getMessage());
        }
        
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
    
    public Cursor getTakingCourses(String search)
    {
    	return db.query("courses", new String[] {"key", "title"}, 
                "title LIKE ? AND key IN (SELECT key FROM taking)", 
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
    
    public boolean saveCourse(String id)
    {   
    	long result = -1;
    	
    	try {
	    	SQLiteStatement s = db.compileStatement("INSERT INTO TAKING VALUES(?)");
	    	s.bindString(1, id);
	    	
	    	result = s.executeInsert();
	    	
	    	s.close();
    	} catch (Exception e) {
    		Log.d("DBAdapter", "Course is already in taking table");
    	}
    	
    	return result != -1;
	}
    
    public void removeCourse(String id)
    {
    	// this will fail if table exists
    	db.execSQL("DELETE FROM taking WHERE KEY = ?;", new String[] { id });
    }
    
    public Cursor getIsTaking(String id)
    {
    	Cursor mCursor = db.query(true, "taking", new String[] {"key"}, 
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
    
    private void createCoursesTakingTable() throws Exception
    {
    	Log.d("DB", "Creating taking table");
    	
    	// this will fail if table exists
    	db.execSQL("create table taking (key VARCHAR(10) PRIMARY KEY);");
    }
}
