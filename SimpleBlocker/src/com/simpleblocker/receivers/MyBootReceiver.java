package com.simpleblocker.receivers;

import com.simpleblocker.notification.SimpleBlockerNotifier;
import com.simpleblocker.prefs.Prefs;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class MyBootReceiver extends BroadcastReceiver {

	private final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(BOOT_COMPLETED)) {
			listenBootCompleted(context);
		}
	}

	/**
	 * Listen for BOOT_COMPLETED event and show the SimpleBlocker status bar
	 * notification if the SimpleBlocker service is previously configured to
	 * start
	 * 
	 * @param context
	 */
	private void listenBootCompleted(final Context context) {

		try {
			
			Log.d(AppConfig.LOG_TAG, "Boot_Completed!!");
			Prefs prefs = new Prefs(context);
			if (prefs.isAppEnabled()){
				SimpleBlockerNotifier.showNotification(context, true);
			}
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

}
