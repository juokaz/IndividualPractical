package com.juozas.studentapp.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import android.util.Log;

public class Event {

	private String Day;
	private String Start;
	private int Duration;
	private String Location;
	private String Alts;
	
	private int StartInt;
	private int EndInt;
	private int DayInt;
	
	private static String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
	private static String[] times = {"09.00", "10.00", "11.10", "12.10", "13.05", "14.00", "15.00", "16.10", "17.10"}; 
	private static int[] minutes = {  540,     600,     670,     730,     780,     840,     900,     970,     1030};
	private static String[] sites = {     "central", "external", "kb", "med+vet", "other"};
	private static String[] sitesNames = {"CA",      "EX",       "KB", "CA",      "OT"};
	
	public Event(String day, int dayint, String start, int startint, int endint, int duration, String location, String alts) {
		Day = day;
		Start = start;
		Duration = duration;
		Location = location;
		Alts = alts;
		StartInt = startint;
		EndInt = endint;
		DayInt = dayint;
	}

	public static ArrayList<Event> factory(String start, String end, String sites, String alts) {
		
		ArrayList<Event> events = new ArrayList<Event>();
		
		String[] starts = start.split(",");
		String[] ends = end.split(",");
		String[] alts2 = alts.split(",");
		String[] alts_ = new String[alts2.length];
		for (int i = 0; i < alts2.length; i++) {
			alts_[i] = alts2[i].replace("'", "").trim();
		}
		
		String[] sites2 = sites.split(",");
		String[] sites_ = new String[sites2.length];
		for (int i = 0; i < sites2.length; i++) {
			sites_[i] = sites2[i].replace("'", "").trim();
		}
		
		for (int i = 0; i < starts.length; i++) {
			
			int start_ = Integer.parseInt(starts[i].trim());
			int end_ = Integer.parseInt(ends[i].trim());
			
			int dayint = start_ / (24*60);
			String day = days[dayint];
			String start_time = times[Arrays.binarySearch(minutes, start_ % (24*60))];
			int duration = (end_ - start_) / 50;
			
			String site = sites_[i].toLowerCase();
			if (site.compareTo("") != 0)
				site = sitesNames[Arrays.binarySearch(Event.sites, site)];
			else 
				// default site
				site = sitesNames[Arrays.binarySearch(Event.sites, "other")];
			
			String alternative = alts_[i];

			Log.d("Event", "start " + Integer.toString(start_) + ", end " + Integer.toString(end_) + ", day: " + day + 
					", start time: " + start_time + ", duration: " + Integer.toString(duration) + ", site: " + site +
					", alts: " + alternative);
			
			events.add(new Event(day, dayint, start_time, start_, end_, duration, site, alternative));
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<Event> events2 = (ArrayList<Event>) events.clone();
		
		// remove bogus/dublicated events
		// nowhere in original javascript source code it mentions why are they there, but clearly they are not used anywhere
		for (Event event1 : events) {
			for (Event event2 : events) {
				// two events for one course happen at same time? Impossible, I assume
				if (event1.getStartInt() == event2.getStartInt() && !event1.equals(event2)) {
					if (event2.getLocation() == "OT")
						events2.remove(event2);
						
					break;
				}
			}
		}		
		
		// sort events by the time they happen
		Collections.sort(events2, new Comparator<Event>() {
			public int compare(Event event1, Event event2) {
				if (event1.getDay() != event1.getDay())
					return event1.getDayInt() - event2.getDayInt();
				
				return event1.getStartInt() - event2.getStartInt();
			}
		});
		
		return events2;
	}
	
	public String getStart() {
		return Start;
	}
	public void setStart(String start) {
		Start = start;
	}
	public int getDuration() {
		return Duration;
	}
	public void setDuration(int duration) {
		Duration = duration;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	
	public String getAlts() {
		return Alts;
	}

	public void setAlts(String alts) {
		Alts = alts;
	}

	public String getDay() {
		return Day;
	}

	public void setDay(String day) {
		Day = day;
	}

	public int getStartInt() {
		return StartInt;
	}

	public void setStartInt(int startInt) {
		StartInt = startInt;
	}

	public int getEndInt() {
		return EndInt;
	}

	public void setEndInt(int endInt) {
		EndInt = endInt;
	}

	public int getDayInt() {
		return DayInt;
	}

	public void setDayInt(int dayInt) {
		DayInt = dayInt;
	}
}
