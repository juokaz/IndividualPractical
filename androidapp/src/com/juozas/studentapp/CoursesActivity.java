package com.juozas.studentapp;

import com.juozas.studentapp.data.*;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public class CoursesActivity extends CoursesList {
	
	private EditText ed;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses);
        
        DataProvider data = ((App)getApplicationContext()).getDataProvider();
        
        courses = data.getCourses();
        
        updateList(courses);

        addClickHandler();
        
        ed = (EditText) findViewById(R.id.search);
        ed.requestFocus();

        ed.addTextChangedListener(new TextWatcher() {

        	 public void afterTextChanged(Editable s) {
        	 }

        	 public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        	 }

        	 public void onTextChanged(CharSequence s, int start, int before, int count) {

	        	 List<Course> searched = new ArrayList<Course>();
	        	 
	        	 String search = ed.getText().toString().toLowerCase();
	        	 
        		 Log.d("Courses", "Searching for '" + search + "'");
	        	 
	        	 for (int i = 0; i < courses.size(); i++) {
	        		 Course course = courses.get(i);
	        		 
	        		 if (course.getTitle().toLowerCase().contains(search)) {
						searched.add(course);
					 }
        		 }
	        	 
	        	 updateList(searched);
        	 }
    	 });
    }
}