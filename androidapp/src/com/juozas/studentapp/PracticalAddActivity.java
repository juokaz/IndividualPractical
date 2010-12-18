package com.juozas.studentapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.juozas.studentapp.data.*;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class PracticalAddActivity extends Activity {

	private DataProvider data;
	private Practical practical;
	private List<Course> courses;
	
	private Spinner coursesSpinner;
	private EditText title;
	private EditText notes;
	private EditText due;
	private EditText time;
	private Button save;
	
	private Course course;

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
        
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.practical_add);

		data = ((App) getApplicationContext()).getDataProvider();
		
		courses = data.getCoursesTakingFull();

		if (getIntent().getData() != null) {
			practical = data.getPractical(getIntent().getData().toString());
		}
		
		coursesSpinner = (Spinner) findViewById(R.id.courses);
		title = (EditText) findViewById(R.id.title);
		notes = (EditText) findViewById(R.id.notes);
		due = (EditText) findViewById(R.id.due);
		time = (EditText) findViewById(R.id.time);
		save = (Button) findViewById(R.id.save);

		prepareForm();
		prepareData();
	}

	private void prepareForm() {

		CourseAdapter coursesAdapter = new CourseAdapter(this, courses);
		coursesSpinner.setAdapter(coursesAdapter);
		coursesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	            public void onItemSelected(AdapterView<?> parent, View view, int position, long i) {
	            	updateCourseSelection();
	            }

	            public void onNothingSelected(AdapterView<?> parent) {
	            }
	        });
		
		save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                savePractical();
            }
        });
		
		due.setFocusable(false);
		due.setFocusableInTouchMode(false);
		due.setClickable(true);
		due.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
		
		time.setFocusable(false);
		time.setFocusableInTouchMode(false);
		time.setClickable(true);
		time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
	}
	
	private void prepareData() {
		
		Calendar c = Calendar.getInstance();

		if (practical != null) {
			title.setText(practical.getTitle());
			notes.setText(practical.getNotes());
			course = practical.getCourse();
			
			int position = 0;
			
			for (Course course_ : courses) {
				if (course_.getKey().equals(course.getKey())) {
					position = courses.indexOf(course_); 
					break;
				}
			}
			
			coursesSpinner.setSelection(position);
			c.setTime(practical.getDue());
		}	
		
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);	
		
        updateDisplayDue();
        updateDisplayTime();
	}
	
	private void savePractical() {
		
		// default course
		if (course == null) {
			course = courses.get(0);
		}

		String title_ = title.getText().toString();
		
		if (title_.equals("")) {
			displayError("Title is needed");
			return;
		}
		
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		try {
			date = dfm.parse(Integer.toString(mYear) + "-" + pad(mMonth+1) + 
					"-" + pad(mDay) + " " + pad(mHour) + 
					":" + pad(mMinute));
		} catch (ParseException e) {
			Log.d("PracticalAdd", "Wrong date format: " + e.getMessage());
			return;
		}
		
		if (date.before(new Date())) {
			displayError("Due date must be in a future");
			return;
		}
	
		if (practical == null) {
			practical = new Practical(course, title_, date);
		} else {
			practical.setTitle(title_);
			practical.setDue(date);
			practical.setCourse(course);
		}
		
		practical.setNotes(notes.getText().toString());
		
		if (data.savePractical(practical)) {
			Log.d("Course", "Practical saved");
            
			Intent i = new Intent(getApplicationContext(), StudentApp.class);
     	    i.setData(Uri.parse(Integer.toString(3)));
            startActivity(i);
		} else {
			displayError("Practical cannot be saved");
			return;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DATE_DIALOG_ID:
	        return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

	            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	                mYear = year;
	                mMonth = monthOfYear;
	                mDay = dayOfMonth;
	                updateDisplayDue();
	            }
	        }, mYear, mMonth, mDay);
	    case TIME_DIALOG_ID:
	        return new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
	            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	                mHour = hourOfDay;
	                mMinute = minute;
	                updateDisplayTime();
	            }
	        }, mHour, mMinute, false);
	    }
	    return null;
	}
	
    private void updateDisplayDue() {
        due.setText(
            new StringBuilder()
            		.append(mYear).append("-")
                    // Month is 0 based so add 1
                    .append(pad(mMonth + 1)).append("-")
                    .append(pad(mDay)));
    }
    
    private void updateDisplayTime() {
        time.setText(
            new StringBuilder()
                    .append(pad(mHour)).append(":")
                    .append(pad(mMinute)));
    }
    
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
	
	private void displayError(String message) {
		Log.d("Course", "Displaying practical add error '" + message + "'");

		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	private void updateCourseSelection() {
		course = (Course) coursesSpinner.getSelectedItem();
		Log.d("PracticalAdd", "Selected course: " + course.getKey());
    }

	private class CourseAdapter extends ArrayAdapter<Course> {
		public CourseAdapter(Context context, List<Course> courses) {
			super(context, android.R.layout.simple_spinner_item, courses);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return createResource(position, convertView, parent);
		}

		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			return createResource(position, convertView, parent);
		}

		public View createResource(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater layoutInflater = getLayoutInflater();
				convertView = layoutInflater.inflate(R.layout.practical_entry,parent, false);
			}

			TextView text = (TextView) convertView.findViewById(R.id.title);

			// Populate template
			Course data = getItem(position);
			text.setText(data.getTitle());
			return convertView;
		}
	}
}