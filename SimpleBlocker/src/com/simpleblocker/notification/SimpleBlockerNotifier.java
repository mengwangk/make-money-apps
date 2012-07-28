package com.simpleblocker.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.simpleblocker.R;
import com.simpleblocker.SimpleBlockerApp;
import com.simpleblocker.ui.activities.MainActivity;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;

public final class SimpleBlockerNotifier {

	// Notification
	private static NotificationManager notificationManager = null;
	private final static int notificationId = R.layout.notification;

	/**
	 * Create the intent that is used when the user touch the QuiteSleep
	 * notification, and then go to the Main class application.
	 * 
	 * @return the PendingIntent
	 * @see PendingIntent
	 */
	private static PendingIntent notificationIntent(final Context context) {
		// The PendingIntent to launch our activity if the user selects this
		// notification. Note the use of FLAG_UPDATE_CURRENT so that if there
		// is already an active matching pending intent, we will update its
		// extras to be the ones passed in here.
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), PendingIntent.FLAG_UPDATE_CURRENT);
		return contentIntent;
	}

	/**
	 * Show the QuiteSleep service running notification. If is the stop action,
	 * cancel the notification and hide.
	 */
	@SuppressWarnings("deprecation")
	public static void showNotification(final Context context, final boolean showNotification) {

		try {
			if (showNotification) {

				// Get the notification manager service
				if (notificationManager == null)
					notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

				CharSequence title = context.getText(R.string.app_name);
				CharSequence message;
				message = SimpleBlockerApp.getContext().getText(R.string.msg_notification_start_message);

				Notification notification = new Notification(R.drawable.ic_launcher, message, System.currentTimeMillis());
				notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
				notification.setLatestEventInfo(context, title, message, notificationIntent(context));

				/*
				 * Notification.Builder builder = new
				 * Notification.Builder(context); Notification notification =
				 * builder
				 * .setContentIntent(notificationIntent(context)).setSmallIcon
				 * (R.drawable.ic_launcher)
				 * .setTicker(message).setWhen(System.currentTimeMillis
				 * ()).setAutoCancel
				 * (true).setContentTitle(title).setContentText(message)
				 * .getNotification();
				 */

				notificationManager.notify(notificationId, notification);

			} else if (notificationManager != null) {
				Log.d(AppConfig.LOG_TAG, "notification canceled");
				notificationManager.cancel(notificationId);
			}

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}
}
