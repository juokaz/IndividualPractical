package com.juozas.studentapp;

import android.os.Bundle;
import android.widget.TextView;

public class TakingActivity extends CoursesList {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.taking);
		
		DataProvider data = ((App)getApplicationContext()).getDataProvider();
        
        courses = data.getCoursesTaking();
        
        if (courses.size() > 0)
        {     
	        updateList(courses);
	        addClickHandler();
        } else {
        	TextView text = (TextView) findViewById(R.id.text);

            text.setText("You have not selected any courses!");
        }
	}
}
