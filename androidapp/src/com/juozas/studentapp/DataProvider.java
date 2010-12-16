package com.juozas.studentapp;

import com.juozas.studentapp.data.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.util.Log;

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
		return _processCourses(dbadapter.getAllCourses(search));
	}
	
	public List<Map<String, Course>> getCoursesTaking()
	{
		return this.getCoursesTaking("");
	}
	
	public List<Map<String, Course>> getCoursesTaking(String search)
	{
		return _processCourses(dbadapter.getTakingCourses(search));
	}
	
	private List<Map<String, Course>> _processCourses(Cursor c)
	{
		List<Map<String, Course>> courses = new ArrayList<Map<String, Course>>();
        
        try
        {
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
        	Log.d("DataProvider", "Error processing courses: " + e.getMessage());
        }
        
        return courses;
	}
	
	public Course getCourse(String id)
	{
		Cursor cursor = dbadapter.getCourse(id);
		
		Course course = new Course(cursor.getString(cursor.getColumnIndex("key")));
		
		course.setTitle(cursor.getString(cursor.getColumnIndex("title")));
		course.setLevel(cursor.getString(cursor.getColumnIndex("level")));
		course.setSCQFLevel(cursor.getString(cursor.getColumnIndex("scqf_level")));
		course.setAcronym(cursor.getString(cursor.getColumnIndex("acronym")));
		course.setNormalYear(cursor.getString(cursor.getColumnIndex("normal_year")));
		course.setVisitorsOnly(cursor.getString(cursor.getColumnIndex("visitors_only")));
		course.setBlocks(cursor.getString(cursor.getColumnIndex("blocks")));
		course.setCO(cursor.getString(cursor.getColumnIndex("co")));
		course.setSY(cursor.getString(cursor.getColumnIndex("sy")));
		course.setFirstMeet(cursor.getString(cursor.getColumnIndex("first_meet")));
		course.setLocation(cursor.getString(cursor.getColumnIndex("location")));
		course.setSpecialArrangements(cursor.getString(cursor.getColumnIndex("special_arrangements")));
		
		Cursor cursor_ = dbadapter.getCollege(cursor.getString(cursor.getColumnIndex("college")));

		if (cursor_.getCount() > 0)
			course.setCollege(new College(cursor_.getString(cursor_.getColumnIndex("key")), cursor_.getString(cursor_.getColumnIndex("name"))));
		
		cursor_ = dbadapter.getSchool(cursor.getString(cursor.getColumnIndex("school")));
		
		if (cursor_.getCount() > 0)
			course.setSchool(new School(cursor_.getString(cursor_.getColumnIndex("key")), cursor_.getString(cursor_.getColumnIndex("name"))));
				
		cursor_ = dbadapter.getSubjectArea(cursor.getString(cursor.getColumnIndex("subject_area")));

		if (cursor_.getCount() > 0)
			course.setSubjectArea(new SubjectArea(cursor_.getString(cursor_.getColumnIndex("key")), cursor_.getString(cursor_.getColumnIndex("name"))));
		
		return course;
	}
	
	public boolean isUserTakingThis(String id)
	{
		Cursor cursor = dbadapter.getIsTaking(id);
		
		return cursor.getCount() > 0;
	}
	
	public boolean saveCourse(String id)
	{
		return dbadapter.saveCourse(id);
	}
	
	public boolean removeCourse(String id)
	{
		dbadapter.removeCourse(id);
		
		return !isUserTakingThis(id);
	}
}
