package com.juozas.studentapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

        TextView title = (TextView) findViewById(R.id.title);
        TextView description = (TextView) findViewById(R.id.description);

        title.setText(course.getTitle());
        description.setText(course.getLocation());
        
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
                	displayError("Course cannot be selected, clashes found!");
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
