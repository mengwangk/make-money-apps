package com.mylotto.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Common helper class.
 * 
 * @author MEKOH
 *
 */
public class Utils {
	
	/**
	 * Check if the device is connected to network
	 * 
	 * @return true if connected, otherwise return false
	 */
	public static boolean isOnline(final Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Padding left function
	 * 
	 * @param value
	 * @param length
	 * @param paddingChar
	 * @return
	 */
	public static String padLeft(final String value, final int length, final String paddingChar) {
		if (StringUtils.isNullorEmpty(value))
			return StringUtils.EMPTY;
		String result = value;
		int toPad = length - value.length();
		if (toPad > 0) {
			for (int i = 0; i < toPad; i++) {
				result = paddingChar + result;
			}
		}
		return result;
	}

}
