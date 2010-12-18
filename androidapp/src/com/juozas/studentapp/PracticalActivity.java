package com.juozas.studentapp;

import com.juozas.studentapp.data.*;

import android.app.Activity;
import android.os.Bundle;

public class PracticalActivity extends Activity {

	private DataProvider data;
	private Practical practical;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        data = ((App)getApplicationContext()).getDataProvider();

        practical = data.getPractical(getIntent().getData().toString());
	}
}