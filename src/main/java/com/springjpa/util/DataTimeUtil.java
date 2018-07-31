package com.springjpa.util;

import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DataTimeUtil {

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_WITHOUT_ZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	public static final DateTimeZone TIME_ZONE = DateTimeZone.forID("UTC");

	public static DateTime getCurrent() {
		return DateTime.now().withZone(TIME_ZONE);
	}

	public static int getMonth(DateTime dateTime) {
		dateTime.withZone(TIME_ZONE);
		return dateTime.getMonthOfYear();
	}

	public static int getQuarter(DateTime dateTime) {
		dateTime.withZone(TIME_ZONE);
		return (dateTime.getMonthOfYear() / 3) + 1;
	}

	public static int getYear(DateTime dateTime) {
		dateTime.withZone(TIME_ZONE);
		return dateTime.getYear();
	}
	
	
//	public static void main(String[] args) {
//
//		DateTime dt1 = DateTime.now();
//	    System.out.println(dt1 + "with zone is " + dt1.getZone());
//	 
//	    DateTimeZone dtz2 = DateTimeZone.forID("Europe/Berlin");
//	    DateTime dt2 = dt1.withZone(dtz2);
//	    System.out.println(dt2 + " with zone is" + dt2.getZone());
//	 
//	    DateTimeZone dtz3 = DateTimeZone.forID("UTC");
//	    DateTime dt3 = dt1.withZone(dtz3);
//	    System.out.println(dt3 + " with zone is " + dt3.getZone());
//	    
//	    String text = "2015-05-28 12:45:59";
//	    Timestamp timestamp = Timestamp.valueOf(text);
//	    System.out.println("Timestamp: " + timestamp);
//	 
//	    DateTime dateTime = new DateTime(timestamp);
//	    System.out.println("DateTime: " + dateTime);
//	    
//	    System.out.println(DataTimeUtil.getCurrent() + " with zone is " +DataTimeUtil.getCurrent().getZone());
//	    
//	}

}
