package com.juozas.studentapp.data;

import java.util.ArrayList;
import java.util.Arrays;

import android.util.Log;

public class Course {
	
	private String key;
	private College College;
	private School School;
	private SubjectArea SubjectArea;
	private String Title;
	private String Level;
	private String SCQFLevel;
	private String Acronym;
	private String NormalYear;
	private String VisitorsOnly;
	private String Blocks;
	private Person CO;
	private Person SY;
	private String SpecialArrangements;
	private String FirstMeet;
	private String Location;
	private int[][] Options;
	
	private ArrayList<Event> Events;
	
	public Course(String key) {
		this.key = key;
		
		// init empty value
		this.College = new College("", "");
		this.School = new School("", "");
		this.SubjectArea = new SubjectArea("", "");
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public College getCollege() {
		return College;
	}
	public void setCollege(College college) {
		College = college;
	}
	public School getSchool() {
		return School;
	}
	public void setSchool(School school) {
		School = school;
	}
	public SubjectArea getSubjectArea() {
		return SubjectArea;
	}
	public void setSubjectArea(SubjectArea subjectArea) {
		SubjectArea = subjectArea;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getLevel() {
		return Level;
	}
	public void setLevel(String level) {
		Level = level;
	}
	public String getSCQFLevel() {
		return SCQFLevel;
	}
	public void setSCQFLevel(String sCQFLevel) {
		SCQFLevel = sCQFLevel;
	}
	public String getAcronym() {
		return Acronym;
	}
	public void setAcronym(String acronym) {
		Acronym = acronym;
	}
	public String getNormalYear() {
		return NormalYear;
	}
	public void setNormalYear(String normalYear) {
		NormalYear = normalYear;
	}
	public String getVisitorsOnly() {
		return VisitorsOnly;
	}
	public void setVisitorsOnly(String visitorsOnly) {
		VisitorsOnly = visitorsOnly;
	}
	public String getBlocks() {
		return Blocks;
	}
	public void setBlocks(String blocks) {
		Blocks = blocks;
	}
	public Person getCO() {
		return CO;
	}
	public void setCO(String cO) {
		CO = Person.factory(cO);
	}
	public Person getSY() {
		return SY;
	}
	public void setSY(String sY) {
		SY = Person.factory(sY);
	}
	public String getSpecialArrangements() {
		return SpecialArrangements;
	}
	public void setSpecialArrangements(String specialArrangements) {
		SpecialArrangements = specialArrangements;
	}
	public String getFirstMeet() {
		return FirstMeet;
	}
	public void setFirstMeet(String firstMeet) {
		FirstMeet = firstMeet;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public ArrayList<Event> getEvents() {
		return Events;
	}
	public void setEvents(String start, String end, String alts, String sites) {
		Events = Event.factory(start, end, sites, alts);
	}
	public int[][] getOptions() {
		return Options;
	}
	public void setOptions(String options_) {
		
		String[] parts = options_.split(",\\[");
		
		int[][] options = new int[parts.length][];
		
		for (int i = 0; i < parts.length; i++) {
			String[] parts2 = parts[i].replace("[", "").replace("]", "").split(",");
			
			int[] options2 = new int[parts2.length];
			
			for (int j = 0; j < parts2.length; j++) {
				options2[j] = Integer.parseInt(parts2[j]);
			}
			
			options[i] = options2;
			
			Log.d("Course", "options[" + Integer.toString(i) + "] = " + Arrays.toString(options2));
		}
		
		Options = options;
	}
}
