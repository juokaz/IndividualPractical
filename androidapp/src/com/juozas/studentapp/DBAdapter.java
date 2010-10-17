package com.juozas.studentapp;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
        return this;
    }
  
    public void close() 
    {
        DBHelper.close();
    }
    
    public Cursor getAllCourses() 
    {
        return db.query("courses", new String[] {
        		"title"}, 
                null, 
                null, 
                null, 
                null, 
                "title asc");
    }
}
