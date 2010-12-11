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
import android.view.View;

public class CoursesActivity extends ListActivity {
	
	private EditText ed;
	
	private List<Map<String, Course>> courses = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses);
        
        DataProvider data = ((App)getApplicationContext()).getDataProvider();
        
        courses = data.getCourses();
        
        updateList(courses);

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent courseIntent = new Intent(getApplicationContext(), CourseActivity.class);
                
                ListView lv = (ListView) parent;
                
                Course course = (Course) ((Map<String, Course>) parent.getItemAtPosition(position)).get(DataProvider.DATA);
                
                courseIntent.setData(Uri.parse(course.getKey()));
                startActivity(courseIntent);
			}
        });
        
        ed = (EditText) findViewById(R.id.search);

        ed.addTextChangedListener(new TextWatcher() {

        	 public void afterTextChanged(Editable s) {
        	 }

        	 public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        	 }

        	 public void onTextChanged(CharSequence s, int start, int before, int count) {

	        	 List<Map<String, Course>> searched = new ArrayList<Map<String, Course>>();
	        	 
	        	 String search = ed.getText().toString().toLowerCase();
	        	 
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
    
    private void updateList(List<Map<String, Course>> data)
    {
    	 SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.list_item,
                 new String[] {
    			 	DataProvider.DATA
                 }, new int[] {
                     R.id.title
                 });
         simpleAdapter.setViewBinder(new ViewBinder() {
        	@Override
    	    public boolean setViewValue(View view, Object data, String stringRepresetation) {
        		Course listItem = (Course)data;

    	        TextView menuItemView = (TextView)view;
    	        menuItemView.setText(listItem.getTitle());

    	        return true;
    	    }
         });
         setListAdapter(simpleAdapter);
    }
}