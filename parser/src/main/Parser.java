package main;

import main.data.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.*;

public class Parser {
	
	private static final String BASIC = "CoursesBasicData.js";
	
	private static final String COURSES = "Courses12Data.js";
	
	private static final String EVENTS = "Events12Data.js";
	
	private File folder;
	
	public Parser(File folder)
	{
		this.folder = folder;
	}
	
	public void parse(DBAdapter adapter)
	{
		HashMap<String, College> colleges = parseColleges();
		HashMap<String, School> schools = parseSchools();
		HashMap<String, SubjectArea> subjectAreas = parseSubjectAreas();
		
		HashMap<String, Course> courses = new HashMap<String, Course>();
		HashMap<String, Course> courses_ = parseCourses();
	    
		parseEvents(courses_);
		
		Iterator it = courses_.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        if (((Course) pairs.getValue()).getNormalYear().compareTo("1") == 0) {
				courses.put((String) pairs.getKey(), (Course) pairs.getValue());
			}
	    }
		
		if (Main.DEBUG) {
			System.out.println("Colleges: " + colleges.size());
			System.out.println("Schools: " + schools.size());
			System.out.println("Subject areas: " + subjectAreas.size());
			System.out.println("Courses: " + courses.size());
		}
		
		try
		{
			adapter.insertColleges(colleges);
			adapter.insertSchools(schools);
			adapter.insertSubjectAreas(subjectAreas);
			adapter.insertCourses(courses);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
	}
	
	private HashMap<String, College> parseColleges()
	{
		String file = folder.toString() + File.separatorChar + BASIC;
		
		String data = readData(file);

		// Match colleges
        Matcher matcher = Pattern.compile("Colleges\\['(\\S+)'\\] = \"(.+)\";").matcher(data);
        
        HashMap<String, College> colleges = new HashMap<String, College>();
        
        while (matcher.find()) { 
        	colleges.put(matcher.group(1), new College(matcher.group(1), matcher.group(2))); 
		}
        
        return colleges;
	}
	
	private HashMap<String, School> parseSchools()
	{
		String file = folder.toString() + File.separatorChar + BASIC;
		
		String data = readData(file);

		// Match schools
        Matcher matcher = Pattern.compile("Schools\\['(\\S+)'\\] = \"(.+)\";").matcher(data);
        
        HashMap<String, School> schools = new HashMap<String, School>();
        
        while (matcher.find()) { 
        	schools.put(matcher.group(1), new School(matcher.group(1), matcher.group(2))); 
		}

		return schools;
	}
	
	private HashMap<String, SubjectArea> parseSubjectAreas()
	{
		String file = folder.toString() + File.separatorChar + BASIC;
		
		String data = readData(file);

		// Match subject areas
        Matcher matcher = Pattern.compile("SubjectAreas\\['(\\S+)'\\] = \"(.+)\";").matcher(data);
        
        HashMap<String, SubjectArea> subjectAreas = new HashMap<String, SubjectArea>();
        
        while (matcher.find()) { 
        	subjectAreas.put(matcher.group(1), new SubjectArea(matcher.group(1), matcher.group(2))); 
		}
        
        return subjectAreas;
	}
	
	private HashMap<String, Course> parseCourses()
	{
		String file = folder.toString() + File.separatorChar + COURSES;
		
		String data = readData(file);

		// Match courses
        Matcher matcher = Pattern.compile("Courses\\['(\\S+)'\\] = new Object\\(\\);").matcher(data);
        
        HashMap<String, Course> courses = new HashMap<String, Course>();
        
        while (matcher.find()) { 
        	courses.put(matcher.group(1), new Course(matcher.group(1))); 
        }
        
        // Add information pieces
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].College = \"(.+)\";").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setCollege(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].School = \"(.+)\";").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setSchool(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].SubjectArea = \"(.+)\";").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setSubjectArea(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].Title = \"(.+)\";").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setTitle(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].Level = \"(.+)\";").matcher(data); 
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setLevel(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].SCQFLevel = \"(.+)\";").matcher(data); 
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setSCQFLevel(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].Acronym = \"(.+)\";").matcher(data); 
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setAcronym(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].NormalYear = \"(.+)\";").matcher(data); 
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setNormalYear(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].VisitorsOnly = \"(.+)\";").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setVisitorsOnly(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].Blocks = \"(.+)\";").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setBlocks(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].CO = new Array\\((.+)\\);").matcher(data); 
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setCO(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].SY = new Array\\((.+)\\);").matcher(data); 
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setSY(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].SpecialArrangements = \"(.+)\";").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setSpecialArrangements(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].FirstMeet = \"(.+)\";").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setFirstMeet(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].Location = \"(.+)\";").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setLocation(matcher.group(2).replace("<b>Site:</b>", "").trim());
        }
        
        return courses;
	}
	
	private void parseEvents(HashMap<String, Course> courses)
	{
		String file = folder.toString() + File.separatorChar + EVENTS;
		
		String data = readData(file);

		// Match courses
        Matcher matcher = Pattern.compile("Courses\\['(\\S+)'\\].start = new Array \\((.+)\\);").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setStart(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].end = new Array\\((.+)\\);").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setEnd(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].alts = new Array\\((.+)\\);").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setAlts(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].sites = new Array\\((.+)\\);").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setSites(matcher.group(2));
        }
        matcher = Pattern.compile("Courses\\['(\\S+)'\\].options = new Array\\((.+)\\);").matcher(data);
        while (matcher.find()) { 
        	courses.get(matcher.group(1)).setOptions(matcher.group(2));
        }
	}
	
	private String readData(String filePath)
	{
		byte[] buffer = new byte[(int) new File(filePath).length()];
		
		try
		{
		    BufferedInputStream f = new BufferedInputStream(new FileInputStream(filePath));
		    f.read(buffer);
		}
		catch (Exception e)
		{
			return new String();
		}
		
	    return new String(buffer);
	}
}
