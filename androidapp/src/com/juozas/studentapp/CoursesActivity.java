package com.juozas.studentapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

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

        ed.addTextChangedListener(new TextWatcher() {

        	 public void afterTextChanged(Editable s) {
        	 }

        	 public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        	 }

        	 public void onTextChanged(CharSequence s, int start, int before, int count) {

	        	 List<Map<String, Course>> searched = new ArrayList<Map<String, Course>>();
	        	 
	        	 String search = ed.getText().toString().toLowerCase();
	        	 
        		 Log.d("Courses", "Searching for '" + search + "'");
	        	 
	        	 for (int i = 0; i < courses.size(); i++) {
	        		 Map<String, Course> course = courses.get(i);
	        		 
	        		 if (course.get(DataProvider.DATA).getTitle().toLowerCase().contains(search)) {
						searched.add(course);
					 }
        		 }
	        	 
	        	 updateList(searched);
        	 }
    	 });
    }
}