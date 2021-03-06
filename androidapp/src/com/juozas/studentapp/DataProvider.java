package com.juozas.studentapp;

import com.juozas.studentapp.data.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.database.Cursor;
import android.util.Log;

public class DataProvider {

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
	
	public List<Course> getCourses()
	{
		return this.getCourses("");
	}
	
	public List<Course> getCourses(String search)
	{
		return processCourses(dbadapter.getAllCourses(search));
	}
	
	public List<Course> getCoursesTaking()
	{
		return this.getCoursesTaking("");
	}
	
	public List<Course> getCoursesTaking(String search)
	{
		return processCourses(dbadapter.getTakingCourses(search));
	}
	
	public List<Course> getCoursesTakingFull()
	{
		List<Course> courses = new ArrayList<Course>();
		List<Course> taking =  this.getCoursesTaking("");
		
		for (Course course_ : taking) {
			course_ = getCourse(course_.getKey());
			
			courses.add(course_);
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
		
		course.setEvents(cursor.getString(cursor.getColumnIndex("start")), 
						 cursor.getString(cursor.getColumnIndex("end")), 
						 cursor.getString(cursor.getColumnIndex("alts")), 
						 cursor.getString(cursor.getColumnIndex("sites")));
		
		course.setOptions(cursor.getString(cursor.getColumnIndex("options")));
		
		Cursor cursor_ = dbadapter.getCollege(cursor.getString(cursor.getColumnIndex("college")));

		if (cursor_.getCount() > 0)
			course.setCollege(new College(cursor_.getString(cursor_.getColumnIndex("key")), cursor_.getString(cursor_.getColumnIndex("name"))));
		
		cursor_.close();
		cursor_ = dbadapter.getSchool(cursor.getString(cursor.getColumnIndex("school")));
		
		if (cursor_.getCount() > 0)
			course.setSchool(new School(cursor_.getString(cursor_.getColumnIndex("key")), cursor_.getString(cursor_.getColumnIndex("name"))));
				
		cursor_.close();
		cursor_ = dbadapter.getSubjectArea(cursor.getString(cursor.getColumnIndex("subject_area")));

		if (cursor_.getCount() > 0)
			course.setSubjectArea(new SubjectArea(cursor_.getString(cursor_.getColumnIndex("key")), cursor_.getString(cursor_.getColumnIndex("name"))));
		
		cursor_.close();
		
		cursor.close();
		
		return course;
	}
	
	public boolean isUserTakingThis(String id)
	{
		Cursor cursor = dbadapter.getIsTaking(id);
		
		boolean taking = cursor.getCount() > 0;
		
		cursor.close();
		
		return taking;
	}
	
	public boolean saveCourse(String id)
	{
		// can't take this course, it's causing clashes
		if (!doClashesOccur(id))
			return false;
		
		return dbadapter.saveCourse(id);
	}
	
	public boolean removeCourse(String id)
	{
		dbadapter.removeCourse(id);
		
		return !isUserTakingThis(id);
	}
	
	public List<Practical> getPracticals()
	{
		ArrayList<Practical> practicals = new ArrayList<Practical>();
		
		Cursor c = dbadapter.getPracticals();
		
        try
        {
	        if (c.moveToFirst())
	        {
	            do {          
	            	practicals.add(parsePractical(c));
	            } while (c.moveToNext());
	        }
        }
        catch (Exception e)
        {
        	Log.d("DataProvider", "Error processing practicals: " + e.getMessage() + e.getStackTrace());
        }
        
        c.close();
        
        return practicals;
	}
	
	public Practical getPractical(String id)
	{
		Cursor cursor = dbadapter.getPractical(id);
		
		Practical practical = parsePractical(cursor);
		
		cursor.close();
		
		return practical;
	}
	
	public boolean savePractical(Practical p)
	{
		return dbadapter.savePractical(p);
	}
	
	public void deletePractical(Practical p)
	{
		dbadapter.deletePractical(p);
	}
	
	private Practical parsePractical(Cursor c)
	{
		Course course = getCourse(c.getString(c.getColumnIndex("course_id")));
    	
    	Practical practical = new Practical(
    			course,
    			c.getString(c.getColumnIndex("title")),
    			new Date(c.getLong(c.getColumnIndex("due"))));
    	practical.setId(c.getInt(c.getColumnIndex("id")));
    	practical.setNotes(c.getString(c.getColumnIndex("notes")));
    	practical.setCompleted(c.getInt(c.getColumnIndex("completed")) == 1);
    	
    	return practical;
	}
	
	private List<Course> processCourses(Cursor c)
	{
		List<Course> courses = new ArrayList<Course>();
        
        try
        {
	        if (c.moveToFirst())
	        {
	            do {          
	            	Course course = new Course(c.getString(c.getColumnIndex("key")));
	            	course.setTitle(c.getString(c.getColumnIndex("title")));
	            	
	            	courses.add(course);
	            } while (c.moveToNext());
	        }
        }
        catch (Exception e)
        {
        	Log.d("DataProvider", "Error processing courses: " + e.getMessage());
        }
        
        c.close();
        
        return courses;
	}
	
	private boolean doClashesOccur(String id)
	{
		// we want to take this course
		Course course = getCourse(id);
		
		boolean ok = true;
		
		List<Course> taking = getCoursesTakingFull();
		
		for (Course course_ : taking) {			
			// different semester
			if (course.isFirstSemester() && course_.isSecondSemester())
				continue;
			
			// check for events clashes
			for (Event event1 : course.getEvents()) {
				for (Event event2 : course_.getEvents()) {
					if (event1.getStartInt() == event2.getStartInt()) {
						ok = false;
					}
				}
			}
		}
		
		return ok;
	}
}
