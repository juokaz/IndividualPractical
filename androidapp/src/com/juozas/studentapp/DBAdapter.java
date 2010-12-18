package com.juozas.studentapp;

import com.juozas.studentapp.data.*;

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
        	Log.d("DBAdapter", "Exception was thrown while creating taking table: " + e.getMessage());
        }
        
        try {
        	createCoursesPracticalTable();
        } catch (Exception e)
        {
        	Log.d("DBAdapter", "Exception was thrown while creating practicals table: " + e.getMessage());
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
    	Cursor mCursor = db.query(true, "courses", new String[] {
    						"key", "college", "school", "subject_area", "title", "level", "scqf_level",
							"acronym", "normal_year", "visitors_only", "blocks", "co", "sy", "first_meet", 
    						"location", "special_arrangements", "start", "end", "alts", "sites", "options"}, 
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
    
    public Cursor getSchool(String id)
    {
    	Cursor mCursor = db.query(true, "schools", new String[] {"key", "name"}, 
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
    
    public Cursor getCollege(String id)
    {
    	Cursor mCursor = db.query(true, "colleges", new String[] {"key", "name"}, 
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
    
    public Cursor getSubjectArea(String id)
    {
    	Cursor mCursor = db.query(true, "subject_areas", new String[] {"key", "name"}, 
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
    
    public Cursor getPractical(String id)
    {
    	Cursor mCursor = db.query(true, "practicals", new String[] {"id", "title", "course_id", "due", "notes", "completed"}, 
				"id = ?", 
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
    
    public Cursor getPracticals() 
    {
        return db.query("practicals", new String[] {"id", "title", "course_id", "due", "notes", "completed"}, 
                null,
                null,
                null, 
                null, 
                "due asc");
    }
    
    public boolean savePractical(Practical p)
    {   
    	long result = -1;
    	
    	try {
	    	SQLiteStatement s = db.compileStatement("INSERT INTO practicals VALUES(?, ?, ?, ?, ?, ?)");
	    	if (p.getId() != 0)
	    		s.bindLong(1, p.getId());
	    	else
	    		s.bindNull(1);
	    	s.bindString(2, p.getTitle());
	    	s.bindString(3, p.getCourse().getKey());
	    	s.bindLong(4, p.getDue().getTime());
	    	s.bindString(5, p.getNotes());
	    	s.bindLong(6, p.isCompleted() ? 1 : 0);
	    	
	    	result = s.executeInsert();
	    	
	    	s.close();
    	} catch (Exception e) {
    		Log.d("DBAdapter", "Practical is already in a table");
    		
    		try {
	    		SQLiteStatement s = db.compileStatement("UPDATE practicals SET title = ?, course_id = ?, due = ?, notes = ?, completed = ? " +
	    												"WHERE id = ?");
		    	s.bindString(1, p.getTitle());
		    	s.bindString(2, p.getCourse().getKey());
		    	s.bindLong(3, p.getDue().getTime());
		    	s.bindString(4, p.getNotes());
		    	s.bindLong(5, p.isCompleted() ? 1 : 0);
		    	s.bindLong(6, p.getId());
		    	
		    	s.execute();
		    	
		    	s.close();
		    	
		    	result = 1;
        	} catch (Exception e2) {
        		Log.d("DBAdapter", "Practical update failed: " + e2.getMessage());
        	}
    	}
    	
    	return result != -1;
	}
    
    public void deletePractical(Practical p)
    {
    	db.execSQL("DELETE FROM practicals WHERE id = ?;", new String[] { Integer.toString(p.getId()) });
    }
    
    private void createCoursesTakingTable() throws Exception
    {
    	Log.d("DB", "Creating taking table");
    	
    	// this will fail if table exists
    	db.execSQL("create table taking (key VARCHAR(10) PRIMARY KEY);");
    }
    
    private void createCoursesPracticalTable() throws Exception
    {
    	Log.d("DB", "Creating practicals table");
    	
    	// this will fail if table exists
    	db.execSQL("create table practicals (id INTEGER PRIMARY KEY," +
    										"title VARCHAR(255)," +
    										"course_id VARCHAR(10)," +
    										"due VARCHAR(50)," +
    										"notes TEXT," +
    										"completed INTEGER);");
    }
}
