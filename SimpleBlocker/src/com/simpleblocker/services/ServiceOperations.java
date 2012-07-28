package com.simpleblocker.services;

import android.util.Log;

import com.simpleblocker.SimpleBlockerApp;
import com.simpleblocker.prefs.Prefs;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;

public final class ServiceOperations {

	/**
	 * Store service start/stop preferences.
	 * 
	 * @param status
	 * @return
	 */
	public static boolean startStop(boolean status) {

		try {
			Prefs prefs = new Prefs(SimpleBlockerApp.getContext());
			prefs.setAppEnabled(status);
			return true;
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
			return false;
		}
	}

}
