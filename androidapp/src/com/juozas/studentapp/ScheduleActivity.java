package com.juozas.studentapp;

import java.util.Calendar;
import java.util.List;
import com.juozas.studentapp.data.*;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
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
}
