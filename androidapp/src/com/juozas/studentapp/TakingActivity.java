package com.juozas.studentapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class TakingActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView textview = new TextView(this);
        textview.setText("This is the Taking tab");
        setContentView(textview);
	}
}
