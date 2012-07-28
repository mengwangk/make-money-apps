package com.simpleblocker.utils;

import java.util.StringTokenizer;

import android.util.Log;

/**
 * 
 *
 */
public class TokenizerUtils {

	/**
	 * Tokenize the passed phoneNumber erasing the string separator and return
	 * the phone number whitout these.
	 * 
	 * @param phoneNumber
	 * @param delim
	 * @return the new String without the delim chars
	 * @see String
	 */
	public static String tokenizerPhoneNumber(String phoneNumber, String delim) {

		final String DEFAULT_DELIM = "-";

		try {

			// If not delim has been specified, put the default delim
			if (delim == null)
				delim = DEFAULT_DELIM;

			// If phone number is different from null we parse it.
			if (phoneNumber != null) {

				/*
				 * Separate the phone number into tokens which the delim string
				 * is the "separate word"
				 */
				StringTokenizer tokenizer = new StringTokenizer(phoneNumber, delim);

				String phoneNumberWithoutDelim = "";
				while (tokenizer.hasMoreTokens())
					phoneNumberWithoutDelim = phoneNumberWithoutDelim + tokenizer.nextToken();

				return phoneNumberWithoutDelim;
			}
			return null;

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
			return null;
		}
	}

	/**
	 * Add an increase of hours to the time passed, if the delim parameter is
	 * null we use the default parameter ":"
	 * 
	 * @param time
	 * @param increase
	 * @param delim
	 * @return the new time with the increase done
	 * @see String
	 */
	public static String addIncreaseDate(String time, int increase, String delim) {

		try {
			final String DEFAULT_DELIM = ":";

			if (delim == null)
				delim = DEFAULT_DELIM;

			if (time != null) {
				/*
				 * Separate the time into tokens which the delim string is the
				 * "separate word"
				 */
				StringTokenizer tokenizer = new StringTokenizer(time, delim);

				if (tokenizer.countTokens() == 2) {

					String hourString = tokenizer.nextToken();
					String minStrig = tokenizer.nextToken();

					int hour = Integer.valueOf(hourString);
					String completeNewTime = String.valueOf(hour + increase) + ":" + minStrig;

					return completeNewTime;
				}
			}

			return null;

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
			return null;
		}
	}

}
