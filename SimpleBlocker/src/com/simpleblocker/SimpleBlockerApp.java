package com.simpleblocker;

import android.app.Application;
import android.content.Context;

/**
 * 
 * SimpleBlocker application class.
 *
 */
public final class SimpleBlockerApp extends Application {
	
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

}