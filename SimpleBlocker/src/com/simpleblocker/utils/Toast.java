
package com.simpleblocker.utils;

import android.content.Context;

/**
 * <p>
 * Class for show or not Toast notifications
 * </p>
 * 
 * <p>
 * Two levels: 
 * - Debug mode (for develop process) 
 * - Release mode (for release app)
 * </p>
 * 
 */
public class Toast {

	public static final boolean DEBUG = true;
	public static final boolean RELEASE = true;

	/**
	 * Toast for debug (d) mode
	 * 
	 * @param context
	 * @param message
	 * @param length
	 */
	public static void d(Context context, String message, int length) {

		if (DEBUG)
			android.widget.Toast.makeText(context, message, length).show();
	}

	/**
	 * Toast for debug (d) mode
	 * 
	 * @param context
	 * @param message
	 * @param length
	 */
	public static void r(Context context, String message, int length) {

		if (RELEASE)
			android.widget.Toast.makeText(context, message, length).show();
	}
}
