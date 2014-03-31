package main.java.edu.gatech;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
	
	public static String getDate(int month, int year, int day){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		Date date = (Date) c.getTime();
		return dateFormat.format(date);
	}
}
