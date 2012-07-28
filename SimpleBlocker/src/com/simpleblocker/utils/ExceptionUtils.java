package com.simpleblocker.utils;

import android.util.Log;

/**
 * 
 * Exception logging utility class.
 * 
 */
public final class ExceptionUtils {

	/**
	 * 
	 * @param stackTrace
	 * @return
	 */
	public static String getString(Exception exception) {

		try {

			String exceptionString = exception.toString();
			StackTraceElement[] stackTrace = exception.getStackTrace();

			String completeTrace = "";
			for (int i = 0; i < stackTrace.length; i++) {
				if (completeTrace.equals(""))
					completeTrace = exceptionString + "\n\t" + stackTrace[i].toString() + "\n";
				else
					completeTrace = completeTrace + "\t" + stackTrace[i] + "\n";
			}

			return completeTrace;

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, e.toString());
			return "";
		}
	}

}
