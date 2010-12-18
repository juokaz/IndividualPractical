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
        lecturesTitle.setText(lecturesTitle.getText() + ", all in " + (course.isFirstSemester() ? "first" : "second") + " semester");
        
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
		courseButton.setText("I'm taking this!");
    	
    	// make it green
    	courseButton.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
    	
    	courseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Course", "Add course clicked");
                
                data = ((App)getApplicationContext()).getDataProvider();
                
                if (data.saveCourse(course.getKey())) 
                {
            		Log.d("Course", "Displaying course added");
        		
                	displayDialog();
                	displayRemoveButton();
                }
                else
                	displayError("Course cannot be selected, clashes found! Are you sure you are taking this? Check Taking tab to be sure or contact your DoS.");
            }
        });
	}
	
	protected void displayRemoveButton()
	{
		courseButton.setText("I'm not taking this!");
    	
    	// make it red
    	courseButton.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
    	
    	courseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Course", "Add course clicked");
                
                data = ((App)getApplicationContext()).getDataProvider();
                
                if (data.removeCourse(course.getKey()))
                {
                	Log.d("Course", "Course has been removed");
                	displayDialog();
                	displayAddButton();
                }
                else
                	displayError("Course cannot be removed!");
            }
        });
	}
	
	protected void displayDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("What do you want to do next?")
		       .setCancelable(false)
		       .setPositiveButton("Go to Taking", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   Intent i = new Intent(getApplicationContext(), StudentApp.class);
		        	   i.setData(Uri.parse(Integer.toString(1)));
		               startActivity(i);
		           }
		       })
		       .setNegativeButton("Stay here", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	protected void displayError(String text)
	{
		Log.d("Course", "Displaying course error");
		
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getApplicationContext(), text, duration);
		toast.show();
	}
}
