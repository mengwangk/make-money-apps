package com.simpleblocker.receivers;

import com.simpleblocker.listeners.MyPhoneStateListener;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public final class PhoneStateReceiver extends BroadcastReceiver {

	private final String PHONE_STATE = "android.intent.action.PHONE_STATE";

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (intent.getAction().equals(PHONE_STATE)) {
			listenIncomingCalls(context);
		}
	}

	/**
	 * Listen for incoming calls and use the listener for.
	 * 
	 * @param context
	 */
	private synchronized void listenIncomingCalls(Context context) {

		try {
			MyPhoneStateListener phoneListener = new MyPhoneStateListener(context);
			TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

}
