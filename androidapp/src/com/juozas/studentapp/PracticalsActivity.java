package com.juozas.studentapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.juozas.studentapp.data.*;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;

public class PracticalsActivity extends ListActivity {

	private DataProvider data;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.practicals);

        data = ((App)getApplicationContext()).getDataProvider();
        
        pupulatePracticals();
        
        ListView lv = getListView();

        lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d("Courses", "View practical clicked '" + Integer.toString(position) + "'");
				
                Intent courseIntent = new Intent(getApplicationContext(), PracticalActivity.class);
                
                @SuppressWarnings("unchecked")
				Practical practical = (Practical) ((Map<String, Practical>) parent.getItemAtPosition(position)).get("data");
                
                courseIntent.setData(Uri.parse(Integer.toString(practical.getId())));
                startActivity(courseIntent);
			}
        });
        
        Button practicalButton = (Button) findViewById(R.id.addPractical);
        
        practicalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Course", "Add practical clicked");
                
                if (data.getCoursesTaking().size() == 0) {
                	displayError(R.string.CantCreatePractical);
                } else {
	                Intent intent = new Intent(getApplicationContext(), PracticalAddActivity.class);
	                startActivity(intent);
                }
            }
        });
	}
	
	private void pupulatePracticals()
	{
		List<Practical> practicals = data.getPracticals();
		
		updateList(practicals);

		if (practicals.size() != 0) {
			TextView text = (TextView) findViewById(R.id.practicals_text);
			text.setVisibility(TextView.GONE);
		}
 	}
	
	protected void updateList(List<Practical> data)
    {
		List<Map<String, Practical>> data_ = new ArrayList<Map<String,Practical>>();
		
		for (Practical practical : data) {
			HashMap<String, Practical> map = new HashMap<String, Practical>();
			map.put("data", practical);
			data_.add(map);
		}
		
		 SimpleAdapter simpleAdapter = new SimpleAdapter(this, data_, R.layout.list_item,
		         new String[] {
				 	"data"
		         }, new int[] {
		             R.id.title
		         });
		 simpleAdapter.setViewBinder(new ViewBinder() {
			@Override
		    public boolean setViewValue(View view, Object data, String stringRepresetation) {
				Practical listItem = (Practical)data;
		
		        TextView menuItemView = (TextView)view;
		        menuItemView.setText(listItem.getTitle() + " for " + listItem.getCourse().getTitle());
		
		        return true;
		    }
		 });
		 setListAdapter(simpleAdapter);
    }
	
	private void displayError(int message) {
		Log.d("Practicals", "Displaying practicals error '" + getString(message) + "'");

		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}
}