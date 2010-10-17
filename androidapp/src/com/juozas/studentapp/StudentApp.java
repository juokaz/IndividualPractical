package com.juozas.studentapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class StudentApp extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        DBAdapter db = new DBAdapter(this);
        
        ArrayList<String> courses = new ArrayList<String>();
        
        try
        {
	        db.open();
	        
	        Cursor c = db.getAllCourses();
	        if (c.moveToFirst())
	        {
	            do {          
	            	courses.add(c.getString(0));
	            } while (c.moveToNext());
	        }
	        db.close();
        }
        catch (Exception e)
        {
        	Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, courses));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), ((TextView) view).getText(),Toast.LENGTH_SHORT).show();
			}
        });
    }
}