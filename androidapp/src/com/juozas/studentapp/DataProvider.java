package com.juozas.studentapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;

public class DataProvider {

	public static String DATA = "data";
	
	private DBAdapter dbadapter = null;

	public DataProvider(DBAdapter adapter)
	{
		dbadapter = adapter;
		dbadapter.open();
	}
	
	protected void finalize() throws Throwable
	{
		dbadapter.close();
	} 
	
	public List<Map<String, Course>> getCourses()
	{
		return this.getCourses("");
	}
	
	public List<Map<String, Course>> getCourses(String search)
	{
		List<Map<String, Course>> courses = new ArrayList<Map<String, Course>>();
        
        try
        {
	        Cursor c = dbadapter.getAllCourses(search);
	        if (c.moveToFirst())
	        {
	            do {          
	            	Course course = new Course(c.getString(c.getColumnIndex("key")));
	            	course.setTitle(c.getString(c.getColumnIndex("title")));
	            	
	            	Map<String, Course> item = new HashMap<String, Course>(1);
	            	item.put(DataProvider.DATA, course);
	            	
	            	courses.add(item);
	            } while (c.moveToNext());
	        }
        }
        catch (Exception e)
        {
        	
        }
        
        return courses;
	}
	
	public Course getCourse(String id)
	{
		Cursor cursor = dbadapter.getCourse(id);
		
		Course course = new Course(cursor.getString(cursor.getColumnIndex("key")));
		
		course.setTitle(cursor.getString(cursor.getColumnIndex("title")));
		course.setLocation(cursor.getString(cursor.getColumnIndex("location")));
		
		return course;
	}
}
