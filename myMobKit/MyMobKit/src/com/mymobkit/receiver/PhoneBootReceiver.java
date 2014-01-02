package com.mymobkit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.mymobkit.R;
import com.mymobkit.config.AppConfig;
import com.mymobkit.service.HttpdService;
import com.mymobkit.ui.fragment.ControlPanelFragment;

public final class PhoneBootReceiver extends BroadcastReceiver {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":PhoneBootReceiver";

	private final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(BOOT_COMPLETED)) {
			listenBootCompleted(context);
		}
	}

	/**
	 * Listen for BOOT_COMPLETED event and start myMobKit control panel service
	 * if it is configured to start upon reboot
	 * 
	 * @param context
	 */
	public void listenBootCompleted(final Context context) {
		try {
			Log.d(TAG, "[listenBootCompleted] Boot completed!!");

			SharedPreferences sharedPreferences = context.getSharedPreferences(ControlPanelFragment.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
			boolean isServiceStarted = sharedPreferences.getBoolean(ControlPanelFragment.KEY_CONTROL_PANEL_STATUS,
					Boolean.parseBoolean(context.getString(R.string.default_start_stop_service)));
			boolean isAutoStart = sharedPreferences.getBoolean(ControlPanelFragment.KEY_CONTROL_PANEL_AUTO_START,
					Boolean.parseBoolean(context.getString(R.string.default_auto_start_service)));
			if (isServiceStarted && isAutoStart) {

				//String portString = sharedPreferences.getString(ControlPanelFragment.KEY_CONTROL_PANEL_PORT, "-1");
				//Log.d(TAG, "[listenBootCompleted] Port string [" + portString + "]");
				// Create the service intent
				int port = Integer.valueOf(sharedPreferences.getString(ControlPanelFragment.KEY_CONTROL_PANEL_PORT, context.getString(R.string.default_http_port)));
				Intent intent = new Intent(context, HttpdService.class);
				intent.putExtra(AppConfig.HTTP_LISTENING_PORT_PARAM, port);

				// Start the service
				context.startService(intent);
				
				Log.i(TAG, "[listenBootCompleted] myMobKit HTTPD service is started!!");
			}
		} catch (Exception e) {
			Log.e(TAG, "[listenBootCompleted] Error starting HTTPD service", e);
		} finally {
		}
	}
}
