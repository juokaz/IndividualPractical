package com.juozas.studentapp.data;

import java.util.Date;

public class Practical {

	private int Id;
	private Course Course;
	private String Title;
	private Date Due;
	private String Notes = null;
	private boolean Completed = false;
	
	public Practical(Course course, String title, Date due) {
		Course = course;
		Title = title;
		Due = due;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public Course getCourse() {
		return Course;
	}

	public void setCourse(Course course) {
		Course = course;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public Date getDue() {
		return Due;
	}

	public void setDue(Date due) {
		Due = due;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}

	public boolean isCompleted() {
		return Completed;
	}

	public void setCompleted(boolean completed) {
		Completed = completed;
	}
}
