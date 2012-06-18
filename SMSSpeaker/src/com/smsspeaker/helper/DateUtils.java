package com.smsspeaker.helper;

import static java.util.Calendar.DAY_OF_WEEK;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date helper class.
 * 
 * @author MEKOH
 *
 */
public final class DateUtils {

	/**
	 * Date format used in the application
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * Date formatter
	 */
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

	

	/**
	 * Format a date using the default format.
	 * 
	 * @param dt
	 * @return
	 */
	public static String formatDate(final Date dt) {
		try {
			if (dt == null)
				return StringUtils.EMPTY;
			return DATE_FORMATTER.format(dt);
		} catch (Exception ex) {
			return StringUtils.EMPTY;
		}
	}

	/**
	 * Format a date using the passed in format
	 * 
	 * @param dt
	 * @param format
	 * @return
	 */
	public static String formatDate(final Date dt, final String format) {
		try {
			if (dt == null)
				return StringUtils.EMPTY;
			SimpleDateFormat fmt = new SimpleDateFormat(format);
			return fmt.format(dt);
		} catch (Exception ex) {
			return StringUtils.EMPTY;
		}
	}

	/**
	 * Parse a date using the passed in format
	 * 
	 * @param dt
	 * @param format
	 * @return
	 */
	public static Date parseDate(final String dt, final String format) {
		try {
			if (StringUtils.isNullorEmpty(dt))
				return new Date();
			SimpleDateFormat fmt = new SimpleDateFormat(format);
			return fmt.parse(dt);
		} catch (Exception ex) {
			return new Date();
		}
	}

	/**
	 * Parse a date using the default format
	 * 
	 * @param dt
	 * @return
	 */
	public static Date parseDate(final String dt) {
		try {
			if (StringUtils.isNullorEmpty(dt))
				return new Date();
			SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
			return fmt.parse(dt);
		} catch (Exception ex) {
			return new Date();
		}
	}

	/**
	 * Get a date from a string based on a date pattern
	 * @param val
	 * @param datePattern
	 * @return
	 */
	public static Date getDate(final String val, final String datePattern) {
		if (StringUtils.isNullorEmpty(val))
			return null;
		Pattern pattern = Pattern.compile(datePattern);
		Matcher matcher = pattern.matcher(val.trim());
		Date dt = null;
		//if (matcher.matches()) {
		//	matcher.reset();
			if (matcher.find()) {
				String day = matcher.group(1);
				String month = matcher.group(2);
				int year = Integer.parseInt(matcher.group(3));

				if (day.equals("31")
						&& (month.equals("4") || month.equals("6")
								|| month.equals("9") || month.equals("11")
								|| month.equals("04") || month.equals("06") || month
									.equals("09"))) {
					return null; // only 1,3,5,7,8,10,12 has 31 days
				} else if (month.equals("2") || month.equals("02")) {
					//leap year
					if (year % 4 == 0) {
						if (day.equals("30") || day.equals("31")) {
							return null;
						} else {
							dt = buildDate(year, month, day);
						}
					} else {
						if (day.equals("29") || day.equals("30")
								|| day.equals("31")) {
							return null;
						} else {
							dt = buildDate(year, month, day);
						}
					}
				} else {
					dt = buildDate(year, month, day);
				}
			}
		//}
		return dt;
	}

	private static Date buildDate(final int year, final String month,
			final String day) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(year, Integer.parseInt(month) - 1, Integer.parseInt(day));
		return cal.getTime();
	}

	/**
	 * Get the week day name of a date
	 * 
	 * @param dt
	 * @return
	 */
	public static String getWeekdayName(final Date dt) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dt);
		String[] weekdays = new DateFormatSymbols().getWeekdays(); // Get day names
		return weekdays[cal.get(DAY_OF_WEEK)];
	}

	/**
	 * Difference in days between 2 dates
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static long dateDiffDays(final Date begin, final Date end) {
		Calendar beginCal = Calendar.getInstance();
		beginCal.setTime(begin);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(end);

		long milis1 = beginCal.getTimeInMillis();
		long milis2 = endCal.getTimeInMillis();

		long diff = milis2 - milis1;

		// Calculate difference in days
		long diffDays = diff / (24 * 60 * 60 * 1000);
		return diffDays;
	}

	/**
	 * Get the previous month 
	 * 
	 * @param currentMonth
	 * @return
	 */
	public static Calendar getPreviousMonth(final Calendar cal) {
		cal.add(Calendar.MONTH, -1);
		return cal;
	}

	/**
	 * Check if the date is today.
	 * 
	 * @param dt
	 * @return
	 */
	public static boolean isToday(final Date dt) {
		if (dt == null) return false;
		
		Calendar today = Calendar.getInstance();
		Calendar cal = new GregorianCalendar();
		cal.setTime(dt);
		return (
				today.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH) &&
				today.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
				today.get(Calendar.YEAR) == cal.get(Calendar.YEAR) 
				);
	}

}
