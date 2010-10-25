package com.juozas.studentapp;

import android.app.Application;

public class App extends Application {

	  private DataProvider provider;
	  
	  public DataProvider getDataProvider()
	  {
		  if (provider == null)
		  {
			  provider = new DataProvider(new DBAdapter(this.getApplicationContext()));
		  }
		  
		  return provider;
	  }
}
