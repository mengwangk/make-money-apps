package com.mymobkit.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mymobkit.R;
import com.mymobkit.app.MyMobKitApp;
import com.mymobkit.config.AppConfig;
import com.mymobkit.ui.activity.SlidingActivityHolder;

public final class Notifier {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":Notifier";

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
				new Intent(context, SlidingActivityHolder.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP), 
					PendingIntent.FLAG_UPDATE_CURRENT);
		return contentIntent;
	}

	/**
	 * Show myMobKit service running notification. If is the stop action,
	 * cancel the notification and hide.
	 */
	public static void showNotification(final Context context, final boolean showNotification, final int iconId, final int msgId) {

		try {
			if (showNotification) {

				// Get the notification manager service
				if (notificationManager == null)
					notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

				CharSequence title = context.getText(R.string.app_name);
				CharSequence message;
				message = MyMobKitApp.getContext().getText(msgId);


				NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
				Notification notification = builder.setContentIntent(notificationIntent(context))
						.setSmallIcon(iconId)
						.setTicker(message)
						.setWhen(System.currentTimeMillis())
						//.setAutoCancel(false)
						.setOngoing(true)
						.setContentTitle(title).setContentText(message)
						.build();

				notificationManager.notify(notificationId, notification);
			} else {
				if (notificationManager == null) {
					notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				}
				Log.d(TAG, "[showNotification] Notification is canceled");
				notificationManager.cancel(notificationId);
			}

		} catch (Exception e) {
			Log.e(TAG, "[showNotification] Error showing notification", e);
		}
	}
	

	/**
	 * Build an ongoing notification.
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Notification buildNotification(final Context context, final String msg) {
		CharSequence title = context.getText(R.string.app_name);
		//CharSequence message;
		//message = MyMobKitApp.getContext().getText(msgId);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		Notification notification = builder
				.setContentIntent(notificationIntent(context))
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker(msg)
				.setWhen(System.currentTimeMillis())
				.setOngoing(true)
				.setContentTitle(title)
				.setContentText(msg)
				.build();
		return notification;
	}
	
	
	
}
