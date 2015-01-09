package com.wdil.whendidilast;

import java.util.ArrayList;
import java.util.Collections;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Counter {

	private boolean pub = true;
	private ArrayList<DateTime> dates;
	private String name;
	private long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Counter(String name) {
		this.dates = new ArrayList<DateTime>();
		this.name = name;
		
	}
	
	public Counter(ArrayList<DateTime> dates, String name) {
		this.dates = dates;
		this.name = name;
		sortCalendarDates();
	}
	
	public Counter(boolean pub, ArrayList<DateTime> dates, String name) {
		this.pub = pub;
		this.dates = dates;
		this.name = name;
		sortCalendarDates();
	}
	
	public boolean isPub() {
		return pub;
	}

	public void setPub(boolean pub) {
		this.pub = pub;
	}

	public ArrayList<DateTime> getDates() {
		return dates;
	}

	public void setDates(ArrayList<DateTime> dates) {
		this.dates = dates;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean addDate(DateTime date) {
		boolean result = this.dates.add(date);
		sortCalendarDates();
		return result;
	}
	
	public boolean deleteCalendar(DateTime date) {
		return this.dates.remove(date);
	}
	
	public void sortCalendarDates() {
		Collections.sort(this.dates);
	}
	
	public long calculateAverageTimeInMillis() {//Possibly causes divide by zero //TODO
		if(this.dates.size() == 0 || this.dates.size() == 1) {
			return 0;
		}
		sortCalendarDates();
		long sum = 0;
		for(int i = 0; i < this.dates.size() - 1; i++) {//this.dates.size() - 1 because we don't want to do this calculation on the last date in dates
			DateTime date = this.dates.get(i);
			DateTime nextDate = this.dates.get(i + 1);
			Duration difference = new Duration(date, nextDate);
			
			sum += difference.getMillis();
		}
		return sum/(this.dates.size() - 1);
	}
	
	public DateTime getExpectedNextDate() {
		long averageTimeInMillis = calculateAverageTimeInMillis();
		DateTime mostRecentDate = dates.get(dates.size() - 1);
		DateTime expectedDate = mostRecentDate.plus(averageTimeInMillis);
		return expectedDate;
	}
	
	public String toString() {
		return this.name;
	}
	
	public String getCounterInfo() {
		if(this.dates.size() == 0) {
			return "We don't have enough information about " + this.name +". Please enter more dates.";
		}
		
		DateTimeFormatter fmt = DateTimeFormat.longDateTime();
		if(this.dates.size() == 1) {
			return "1 time since " + fmt.print(this.dates.get(0));
		}
		
		
		StringBuilder sb = new StringBuilder();
		
		long avgInMillis = this.calculateAverageTimeInMillis();
		MutablePeriod period = new Period(avgInMillis).normalizedStandard().toMutablePeriod();
		int years = period.getWeeks() / 52;
		period.setYears(years);
		period.setWeeks(period.getWeeks() - (years * 52));
		int totalDays = period.getWeeks() * 7 + period.getDays(); 
		int months = totalDays / 30;
		period.setMonths(months);
		totalDays = totalDays - (months * 30);
		int weeks = totalDays / 7;
		period.setWeeks(weeks);
		int days = period.getDays() - weeks;
		period.setDays(days);
		
			
		years = period.getYears();//Here be split TODO
		if(years > 0) {
			if(years == 1) {
				sb.append("1 year");
			}
			else{
				sb.append(years + "years");
			}
			
		}
		
		months = period.getMonths();
		if(months > 0) {
			if(years > 0) {
				sb.append(", ");
			}
			if(months == 1) {
				sb.append("1 Month");
			}
			else {
				sb.append(months + " months");
			}
		}
		
		weeks = period.getWeeks();
		if(weeks > 0) {
			if(months > 0 || years > 0) {
				sb.append(", ");
			}
			if(weeks == 1) {
				sb.append("1 week");
			}
			else{
				sb.append(weeks + " weeks");
			}
		}
		period.getDays();
		if(days > 0) {
			if(weeks > 0 || months > 0 || years > 0) {
				sb.append(", ");
			}
			if(days == 1) {
				sb.append("1 day");
			}
			else {
				sb.append(days + " days");
			}
		}
		int hours = period.getHours();
		if(hours > 0) {
			if(days > 0 || weeks > 0 || months > 0 || years > 0){
				sb.append(", ");
			}
			if(hours == 1) {
				sb.append("1 hour");
			}
			else {
				sb.append(hours + " hours");
			}
		}
		
		if(sb.toString().equals("")){
			int minutes = period.getMinutes();
			if(minutes > 0){
				if(minutes == 1){
					sb.append("1 minute");
				}
				else {
					sb.append(minutes + " minutes");
				}
			}
			int seconds = period.getSeconds();
			if(seconds > 0){
				if(minutes > 0) {
					sb.append(", ");
				}
				if(seconds == 1) {
					sb.append("1 second");
				}
				else{
					sb.append(seconds + " seconds");
				}
			}
			int millis = period.getMillis();
			if(millis > 0) {
				if(minutes > 0 || seconds > 0){
					sb.append(", ");
				}
				if(millis == 1){
					sb.append("1 millisecond");
				}
				else {
					sb.append(millis + " milliseconds");
				}
			}
		}
		
		return "You " + name + " every " + sb.toString() + " on average. \nYou are next expected to " + 
				this.name + " on " + fmt.print(getExpectedNextDate()) + "\nCount: " + this.dates.size() + 
				" since " + fmt.print(this.dates.get(0));
	}
}


