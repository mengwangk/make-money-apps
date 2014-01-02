package com.mymobkit.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.mymobkit.data.DbHelper;
import com.mymobkit.model.ConfigParam;

/**
 * Application level instance.
 * 
 */
public final class MyMobKitApp extends Application {

	private static final String TAG = "MyMobKitApp";
	
	// Global application context
	private static Context context = null;

	@Override
	public void onCreate() {
		super.onCreate();
		if (context == null)
			context = this.getApplicationContext();
	}

	public static Context getContext() {
		return context;
	}

	public static void setSurveillanceMode(boolean mode) {
		DbHelper dbHelper = new DbHelper(context);
		try {
			dbHelper.updateConfigValue("surveillance_mode", "surveillance", String.valueOf(mode));
		} catch (Exception e) {
			Log.e(TAG, "[setSurveillanceMode] Error setting config param", e);
		} finally {
			if (dbHelper != null) {
				dbHelper.cleanUp();
				dbHelper = null;
			}
		}
	}

	public static boolean getSurveillanceMode() {
		DbHelper dbHelper = new DbHelper(context);
		try {
			ConfigParam config = dbHelper.getAppConfig("surveillance_mode", "surveillance");
			return DbHelper.getBoolean(config.getValue());
		} catch (Exception e) {
			Log.e(TAG, "[getSurveillanceMode] Error retrieving config param", e);
		} finally {
			if (dbHelper != null) {
				dbHelper.cleanUp();
				dbHelper = null;
			}
		}
		return false;
	}
}
