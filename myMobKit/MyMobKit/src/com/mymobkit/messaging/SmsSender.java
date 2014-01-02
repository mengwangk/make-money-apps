package com.mymobkit.messaging;

import java.util.ArrayList;
import java.util.Date;

import com.mymobkit.config.AppConfig;
import com.mymobkit.data.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

public final class SmsSender {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":SmsSender";

	
	private SmsSender() {}

	public static boolean sendSmsToAlias(Context context, String alias, String message) {
        return true;
	}
	
	public static boolean sendSms(final Context context, final String destinationAddress, final String body) {

		/*SmsManager smsManager = SmsManager.getDefault();

		ArrayList<String> parts = smsManager.divideMessage(body);
		smsManager.sendMultipartTextMessage(destinationAddress, null, parts, null, null);

		DbHelper database = new DbHelper(context);
		
		Uri phoneUri = AliasHelper.fromPhoneNumber(context.getContentResolver(), destinationAddress);

		if (phoneUri != null) {
			Log.i(TAG, "Sent message phone URI: " + phoneUri.toString());
	        database.createSms(phoneUri, SmsState.OUTGOING, new Date(), body);
		} else {
			Log.w(TAG, "Sent message to unknown contact: ignoring");
		}

		if (Settings.getCopySentMessages(context)) {
			try {
				ContentValues values = new ContentValues();
				values.put("address", destinationAddress);
				values.put("body", body);
				context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
			} catch (Exception ex) {
				Log.e(Constants.LOG_NAME, "Unable to copy sent message to outbox: " + ex.toString());
			}
		}*/
		
		return true;

	}
}
