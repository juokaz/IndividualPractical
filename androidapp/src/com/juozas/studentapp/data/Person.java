package com.juozas.studentapp.data;

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
		
		String name = parts2[0] + " " + parts2[1] + " " + parts2[2];
		String email = parts2[4] + "@" + parts2[5];
		
		return new Person(name.replace("  ", " "), email);
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
