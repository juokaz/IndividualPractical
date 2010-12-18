package com.juozas.studentapp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.juozas.studentapp.data.*;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScheduleActivity extends Activity {

	private DataProvider data;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        data = ((App)getApplicationContext()).getDataProvider();
        
        populateLectures();
        populatePracticals();
	}
	
	private void populateLectures() {
		List<Course> courses = data.getCoursesTakingFull();
		
		int day = getCurrentDay();
		
		TableLayout lectures = (TableLayout) findViewById(R.id.lectures);
		
		boolean lecturesToday = false;
		
		for (Course course : courses) {
			
			if ((course.isFirstSemester() && isFirstSemesterNow()) || course.isSecondSemester() && !isFirstSemesterNow())
				continue;
			
			for (Event event : course.getEvents()) {
				if (event.getDayInt() == day) {
					TextView name = new TextView(this);
		        	name.setText(course.getTitle());
		        	
					TextView time = new TextView(this);
		        	time.setText(Html.fromHtml("<b>" + event.getStart()) + "</b>");
		        	time.setPadding(0, 0, 10, 0);
		        	
		        	TableRow row = new TableRow(this);
		        	row.addView(time);
		        	row.addView(name);
		        	
		        	lectures.addView(row);
		        	
		        	lecturesToday = true;
				}
			}
		}
		
		TextView text = (TextView) findViewById(R.id.lectures_text);
		
		if (!lecturesToday) {
            text.setText("You have no lectures today!");
		} else {
			text.setVisibility(TextView.GONE);
		}
	}
	
	private void populatePracticals() {
		List<Practical> practicals = data.getPracticals();

		boolean practicalsSoon = false;
		
		TableLayout practicalsList = (TableLayout) findViewById(R.id.practicals);
		
		for (Practical practical : practicals) {

			// if we have more than a month time due for practical
			if (getDaysDifference(practical.getDue()) > 30 || getDaysDifference(practical.getDue()) < 0)
				continue;
			
			TextView name = new TextView(this);
        	name.setText(Html.fromHtml("<u>" + practical.getTitle() + " for " + practical.getCourse().getTitle() + "</u>"));
        	name.setTag(practical.getId());
        	name.setClickable(true);
        	name.setOnClickListener(new OnClickListener() {
        		
        		public void onClick(View v) {
        			String id = Integer.toString((Integer) v.getTag());
    				Log.d("Courses", "View practical clicked '" + id + "'");
    				
                    Intent practicalIntent = new Intent(getApplicationContext(), PracticalActivity.class);
                    
                    practicalIntent.setData(Uri.parse(id));
                    startActivity(practicalIntent);
    			}
            });
        	
			TextView time = new TextView(this);
        	time.setText(getDifference(practical.getDue()) + " left");
        	time.setPadding(0, 0, 10, 0);
        	
        	TableRow row = new TableRow(this);
        	row.addView(time);
        	row.addView(name);
        	
        	practicalsList.addView(row);
        	
        	practicalsSoon = true;
		}
		
		TextView text = (TextView) findViewById(R.id.practicals_text);
		
		if (!practicalsSoon) {
            text.setText("You have no tasks soon!");
		} else {
			text.setVisibility(TextView.GONE);
		}
	}
	
	private boolean isFirstSemesterNow() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		
		return month > Calendar.DECEMBER && month < Calendar.SEPTEMBER;
	}
	
	private int getCurrentDay()
	{
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		
		// convert to normal day offset from 0..6 for Monday..Sunday
		if (day == 1)
			day = 6;
		else
			day = day - 2;
		
		return day;
	}
	
	private double getDaysDifference(Date date)
	{
		Date current = new Date();
		
		return ((double) date.getTime() - current.getTime()) / (1000 * 60 * 60 * 24);
	}
	
	private String getDifference(Date date)
	{
		Date current = new Date();
		
		long difference = (date.getTime() - current.getTime()) / 1000;

		if (difference < 60) {
			return Long.toString(difference) + " s.";
		}
		
		difference = difference / 60;

		if (difference < 60) {
			return Long.toString(difference) + " m.";
		}
		
		difference = difference / 60;

		if (difference < 24) {
			return Long.toString(difference) + " h.";
		}
		
		difference = difference / 24;
		
		return Long.toString(difference) + " d.";
	}
}
