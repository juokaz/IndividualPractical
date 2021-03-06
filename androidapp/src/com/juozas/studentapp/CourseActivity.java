package com.juozas.studentapp;

import com.juozas.studentapp.data.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.PorterDuff;

public class CourseActivity extends Activity {
	
	private Course course;
	private DataProvider data;
	private Button courseButton;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);

        data = ((App)getApplicationContext()).getDataProvider();
        
        course = data.getCourse(getIntent().getData().toString());
        
        populateView();
        
        courseButton = (Button) findViewById(R.id.addCourse);
        
        if (data.isUserTakingThis(course.getKey()))
        {
        	Log.d("Course", "User is taking this course");
        	
        	displayRemoveButton();
        }
        else
        {
        	Log.d("Course", "User is not taking this course");
        	
        	displayAddButton();
        }
    }
	
	protected void populateView()
	{
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(course.getTitle());
        
        TextView location = (TextView) findViewById(R.id.location);
        location.setText(course.getLocation());
        
        TextView college = (TextView) findViewById(R.id.college);
        college.setText(course.getCollege().getName());
        
        TextView school = (TextView) findViewById(R.id.school);
        school.setText(course.getSchool().getName());
        
        TextView subjectArea = (TextView) findViewById(R.id.subject_area);
        subjectArea.setText(course.getSubjectArea().getName());
        
        TextView firstMeeting = (TextView) findViewById(R.id.first_meeting);
        firstMeeting.setText(course.getFirstMeet());
        
        TextView level = (TextView) findViewById(R.id.level);
        level.setText(course.getSCQFLevel());
        
        TextView courseOrganizer = (TextView) findViewById(R.id.course_organizer);
        courseOrganizer.setText(Html.fromHtml("<u>" + course.getCO().getName() + "</u>"));
        courseOrganizer.setClickable(true);
        courseOrganizer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Course", "Course organizer clicked, send email to: " + course.getCO().getEmail());
                
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{course.getCO().getEmail()});

                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
        
        TextView courseSecretary = (TextView) findViewById(R.id.course_secretary);
        courseSecretary.setText(Html.fromHtml("<u>" + course.getSY().getName() + "</u>"));
        courseSecretary.setClickable(true);
        courseSecretary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Course", "Course secretary clicked, send email to: " + course.getSY().getEmail());
                
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{course.getSY().getEmail()});

                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
        
        TextView specialArrangements = (TextView) findViewById(R.id.special_arrangements);
        
        if (course.getSpecialArrangements() != null)
        	specialArrangements.setText(course.getSpecialArrangements());
        else {
        	specialArrangements.setVisibility(View.GONE);
        	TextView specialArrangementsLabel = (TextView) findViewById(R.id.special_arrangements_label);
        	specialArrangementsLabel.setVisibility(View.GONE);
        }
        
        TextView lecturesTitle = (TextView) findViewById(R.id.lectures_title);
        lecturesTitle.setText(lecturesTitle.getText() + "," + R.string.AllIn + " " + (course.isFirstSemester() ? R.string.first : R.string.second) + " " + R.string.semester);
        
        TableLayout lectures = (TableLayout) findViewById(R.id.lectures);
        
        for (Event event : course.getEvents()) {
        	TextView name = new TextView(this);
        	
        	name.setText(event.getStart() + " on " + event.getDay() + "'s in " + event.getLocation() + " area");
        	
        	TableRow row = new TableRow(this);
        	row.addView(name);
        	
        	lectures.addView(row);
        }
	}
	
	protected void displayAddButton()
	{
		courseButton.setText(R.string.IAmTakingThis);
    	
    	// make it green
    	courseButton.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
    	
    	courseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Course", "Add course clicked");
                
                if (data.saveCourse(course.getKey())) 
                {
            		Log.d("Course", "Displaying course added");
        		
                	displayDialog();
                	displayRemoveButton();
                }
                else
                	displayError(R.string.CourseCannotBeSelected);
            }
        });
	}
	
	protected void displayRemoveButton()
	{
		courseButton.setText(R.string.IAmNotTakingThis);
    	
    	// make it red
    	courseButton.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
    	
    	courseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Course", "Add course clicked");
                
                if (data.removeCourse(course.getKey()))
                {
                	Log.d("Course", "Course has been removed");
                	displayDialog();
                	displayAddButton();
                }
                else
                	displayError(R.string.CourseCannotBeRemoved);
            }
        });
	}
	
	protected void displayDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.WhatNext)
		       .setCancelable(false)
		       .setPositiveButton(R.string.GoTaking, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   Intent i = new Intent(getApplicationContext(), StudentApp.class);
		        	   i.setData(Uri.parse(Integer.toString(1)));
		               startActivity(i);
		           }
		       })
		       .setNegativeButton(R.string.StayHere, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	protected void displayError(int text)
	{
		Log.d("Course", "Displaying course error '" + getString(text) + "'");
		
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getApplicationContext(), text, duration);
		toast.show();
	}
}
