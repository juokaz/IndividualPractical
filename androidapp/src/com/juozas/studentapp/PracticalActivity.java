package com.juozas.studentapp;

import com.juozas.studentapp.data.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PracticalActivity extends Activity {

	private DataProvider data;
	private Practical practical;

	private Button completedButton;
	private Button editButton;
	private Button deleteButton;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.practical);

        data = ((App)getApplicationContext()).getDataProvider();

        practical = data.getPractical(getIntent().getData().toString());
        
        completedButton = (Button) findViewById(R.id.completed);
        editButton = (Button) findViewById(R.id.edit);
        deleteButton = (Button) findViewById(R.id.delete);
        
        if (practical.isCompleted())
        {
        	Log.d("Practical", "Practical completed");
        	
        	displayDisCompleteButton();
        }
        else
        {
        	Log.d("Practical", "Practical not completed");
        	
        	displayCompleteButton();
        }        

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(practical.getTitle());

        TextView course = (TextView) findViewById(R.id.course);
        course.setText(practical.getCourse().getTitle());

        TextView due = (TextView) findViewById(R.id.due);
        due.setText(practical.getDue().toLocaleString());
        
    	TextView notes = (TextView) findViewById(R.id.notes);
        
        if (!practical.getNotes().equals(""))
        	notes.setText(practical.getNotes());
        else {
        	notes.setVisibility(View.GONE);
        	TextView notesLabel = (TextView) findViewById(R.id.notes_label);
        	notesLabel.setVisibility(View.GONE);
        }
        
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Practical", "Edit clicked");
                
            	Intent courseIntent = new Intent(getApplicationContext(), PracticalAddActivity.class);
                
                courseIntent.setData(Uri.parse(Integer.toString(practical.getId())));
                startActivity(courseIntent);
            }
        });
        
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Practical", "Delete clicked");
                
                data.deletePractical(practical);
                
                Intent i = new Intent(getApplicationContext(), StudentApp.class);
         	    i.setData(Uri.parse(Integer.toString(3)));
                startActivity(i);
            }
        });
	}
	
	protected void displayCompleteButton()
	{
		completedButton.setText(R.string.HaveCompleted);
    	
    	// make it green
		completedButton.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
    	
		completedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Practical", "Completed clicked");
                
                practical.setCompleted(true);
                data.savePractical(practical);

            	Log.d("Practical", "Practical updated to: completed");
            	displayDisCompleteButton();
            }
        });
	}
	
	protected void displayDisCompleteButton()
	{
		completedButton.setText(R.string.HaventCompleted);
    	
    	// make it red
		completedButton.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
    	
		completedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Log.d("Practical", "Not completed clicked");
                
                practical.setCompleted(false);
                data.savePractical(practical);

            	Log.d("Practical", "Practical updated to: not completed");
            	displayCompleteButton();
            }
        });
	}
}