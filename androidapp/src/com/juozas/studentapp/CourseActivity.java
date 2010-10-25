package com.juozas.studentapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class CourseActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);

        DataProvider data = ((App)getApplicationContext()).getDataProvider();
        
        Course course = data.getCourse(getIntent().getData().toString());

        TextView title = (TextView) findViewById(R.id.title);
        TextView description = (TextView) findViewById(R.id.description);

        title.setText(course.getTitle());
        description.setText(course.getLocation());
    }
}
