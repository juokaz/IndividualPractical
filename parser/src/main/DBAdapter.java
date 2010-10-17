package main;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;

import main.data.*;

public class DBAdapter
{
	private Connection conn;
	
	public DBAdapter(String db) throws Exception 
	{
		Class.forName("org.sqlite.JDBC");
	    conn = DriverManager.getConnection("jdbc:sqlite:" + db);
	    
	    Statement stat = conn.createStatement();
	    
	    stat.executeUpdate("drop table if exists colleges;");
	    stat.executeUpdate("create table colleges (key VARCHAR(10) PRIMARY KEY, name VARCHAR(100));");
	    
	    stat.executeUpdate("drop table if exists schools;");
	    stat.executeUpdate("create table schools (key VARCHAR(10) PRIMARY KEY, name VARCHAR(100));");
	    
	    stat.executeUpdate("drop table if exists subject_areas;");
	    stat.executeUpdate("create table subject_areas (key VARCHAR(10) PRIMARY KEY, name VARCHAR(100));");

	    stat.executeUpdate("drop table if exists courses;");
	    stat.executeUpdate("create table courses (key VARCHAR(10) PRIMARY KEY" +
	    		", college VARCHAR(10)" + 
	    		", school VARCHAR(10)" + 
	    		", subject_area VARCHAR(10)" + 
	    		", title VARCHAR(100)" + 
	    		", level VARCHAR(5)" + 
	    		", scqf_level INTEGER" + 
	    		", acronym VARCHAR(2)" + 
	    		", normal_year INTEGER" + 
	    		", visitors_only VARCHAR(1)" + 
	    		", blocks VARCHAR(10)" + 
	    		", co VARCHAR(100)" + 
	    		", sy VARCHAR(100)" + 
	    		", special_arrangements VARCHAR(100)" + 
	    		", first_meet VARCHAR(100)" + 
	    		", location VARCHAR(100)" + 
	    		", start VARCHAR(100)" + 
	    		", end VARCHAR(100)" + 
	    		", alts VARCHAR(100)" + 
	    		", sites VARCHAR(100)" + 
	    		", options VARCHAR(100));");
	}
	
	protected void finalize() throws Throwable
	{
		conn.close();
	} 
	
	public void insertColleges(HashMap<String, College> colleges) throws Exception
	{		
		PreparedStatement prep = conn.prepareStatement("insert into colleges values (?, ?);");
		
		for ( Iterator iter = colleges.keySet().iterator(); iter.hasNext(); )
		{
			College college = colleges.get(iter.next());
			prep.setString(1, college.getKey());
		    prep.setString(2, college.getName());
		    prep.addBatch();
		}
	    
	    conn.setAutoCommit(false);
	    prep.executeBatch();
	    conn.setAutoCommit(true);
	}
	
	public void insertSchools(HashMap<String, School> schools) throws Exception
	{		
		PreparedStatement prep = conn.prepareStatement("insert into schools values (?, ?);");
		
		for ( Iterator iter = schools.keySet().iterator(); iter.hasNext(); )
		{
			School school = schools.get(iter.next());
			prep.setString(1, school.getKey());
		    prep.setString(2, school.getName());
		    prep.addBatch();
		}
	    
	    conn.setAutoCommit(false);
	    prep.executeBatch();
	    conn.setAutoCommit(true);
	}
	
	public void insertSubjectAreas(HashMap<String, SubjectArea> subjectAreas) throws Exception
	{		
		PreparedStatement prep = conn.prepareStatement("insert into subject_areas values (?, ?);");
		
		for ( Iterator iter = subjectAreas.keySet().iterator(); iter.hasNext(); )
		{
			SubjectArea subjectArea = subjectAreas.get(iter.next());
			prep.setString(1, subjectArea.getKey());
		    prep.setString(2, subjectArea.getName());
		    prep.addBatch();
		}
	    
	    conn.setAutoCommit(false);
	    prep.executeBatch();
	    conn.setAutoCommit(true);
	}
	
	public void insertCourses(HashMap<String, Course> courses) throws Exception
	{		
		PreparedStatement prep = conn.prepareStatement("insert into courses values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		
		for ( Iterator iter = courses.keySet().iterator(); iter.hasNext(); )
		{
			Course course = courses.get(iter.next());
			prep.setString(1, course.getKey());
		    prep.setString(2, course.getCollege());
		    prep.setString(3, course.getSchool());
		    prep.setString(4, course.getSubjectArea());
		    prep.setString(5, course.getTitle());
		    prep.setString(6, course.getLevel());
		    prep.setString(7, course.getSCQFLevel());
		    prep.setString(8, course.getAcronym());
		    prep.setString(9, course.getNormalYear());
		    prep.setString(10, course.getVisitorsOnly());
		    prep.setString(11, course.getBlocks());
		    prep.setString(12, course.getCO());
		    prep.setString(13, course.getSY());
		    prep.setString(14, course.getSpecialArrangements());
		    prep.setString(15, course.getFirstMeet());
		    prep.setString(16, course.getLocation());
		    prep.setString(17, course.getStart());
		    prep.setString(18, course.getEnd());
		    prep.setString(19, course.getAlts());
		    prep.setString(20, course.getSites());
		    prep.setString(21, course.getOptions());
		    prep.addBatch();
		}
	    
	    conn.setAutoCommit(false);
	    prep.executeBatch();
	    conn.setAutoCommit(true);
	}
}
