package com.juozas.studentapp.data;

import android.util.Log;

public class Person {

	private String Name;
	private String Email;
	
	public Person(String name, String email) {
		this.Name = name;
		this.Email = email;
	}
	
	public static Person factory(String data) {
		
		String[] parts = data.split(",");
		String[] parts2 = new String[parts.length];
		
		for (int i = 0; i < parts.length; i++) {
			parts2[i] = parts[i].replace("\"", "");
		}
		
		String name = (parts2[0] + " " + parts2[1] + " " + parts2[2]).replace("  ", " ");
		String email = parts2[4] + "@" + parts2[5];
		
		Log.d("Person", "Person from '" + data + "', to name: '" + name + "', email: '" + email + "'");
		
		return new Person(name, email);
	}
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
}
