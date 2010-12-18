package com.juozas.studentapp;

import com.juozas.studentapp.data.*;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.util.Log;
import android.view.View;

public class CoursesList extends ListActivity {
	
	protected List<Map<String, Course>> courses = null;
    
    protected void addClickHandler()
    {
    	ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d("Courses", "View course clicked '" + Integer.toString(position) + "'");
				
                Intent courseIntent = new Intent(getApplicationContext(), CourseActivity.class);
                
                @SuppressWarnings("unchecked")
				Course course = (Course) ((Map<String, Course>) parent.getItemAtPosition(position)).get(DataProvider.DATA);
                
                courseIntent.setData(Uri.parse(course.getKey()));
                startActivity(courseIntent);
			}
        });
    }
    
    protected void updateList(List<Map<String, Course>> data)
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