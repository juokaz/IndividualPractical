package com.juozas.studentapp;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;

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
