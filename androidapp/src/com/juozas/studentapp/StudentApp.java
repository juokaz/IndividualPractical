package com.juozas.studentapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TabHost;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

public class StudentApp extends TabActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Reusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ScheduleActivity.class);

        // Initialise a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("schedule").setIndicator("Schedule",
                          res.getDrawable(R.drawable.ic_tab_schedule))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, TakingActivity.class);
        spec = tabHost.newTabSpec("taking").setIndicator("Taking",
                          res.getDrawable(R.drawable.ic_tab_taking))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, CoursesActivity.class);
        spec = tabHost.newTabSpec("courses").setIndicator("Courses",
                          res.getDrawable(R.drawable.ic_tab_courses))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        int tab = 0;
        if (getIntent().getData() != null) {
        	String param = getIntent().getData().toString();
        	tab = param != "" ? Integer.parseInt(param) : 0;
        }
        tabHost.setCurrentTab(tab);
    }
}