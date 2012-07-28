package com.simpleblocker.listeners;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.simpleblocker.SimpleBlockerApp;
import com.simpleblocker.prefs.Prefs;
import com.simpleblocker.services.CallBlockerService;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;

public final class MyPhoneStateListener extends PhoneStateListener {

	/**
	 * Constructor with the context parameter for use if the application not run
	 * and is the BOOT_COMPLETED BroadcastReceiver that is launch this.
	 * 
	 * @param context
	 * @param ITelephony
	 */
	public MyPhoneStateListener(Context context) {

		super();
	}

	/**
	 * Function that receive the state id for the phone state type (idle,
	 * offhook and ringing), and the incoming number that is doing the call.
	 */
	public void onCallStateChanged(int state, String incomingNumber) {

		try {

			switch (state) {

			// ------------- CALL_STATE_IDLE ----------------------//
			// Device call state: No activity.
			case TelephonyManager.CALL_STATE_IDLE:
				AppConfig.processingRingingCall = false;
				break;

			// ----------------- CALL_STATE_OFFHOOK --------------//
			// Device call state: Off-hook. At least one call exists that is
			// dialing, active, or on hold, and no calls are ringing or waiting.
			case TelephonyManager.CALL_STATE_OFFHOOK:
				AppConfig.processingRingingCall = false;
				break;

			// ----------------- CALL_STATE_RINGING --------------//
			// Device call state: Ringing. A new call arrived and is ringing
			// or waiting. In the latter case, another call is already active.
			case TelephonyManager.CALL_STATE_RINGING:
				processCallStateRinging(incomingNumber);
				break;

			default:
				break;
			}

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

	/**
	 * Process the CALL_STATE_RINGING signal from PhoneReceiver
	 */
	private void processCallStateRinging(String incomingNumber) {

		try {

			Log.d(AppConfig.LOG_TAG, "RINGING");

			/*
			 * Put the device in silent mode if the incoming number is from
			 * contact banned and in schedule interval
			 */
			final Prefs prefs = new Prefs(SimpleBlockerApp.getContext());
			if (prefs.isAppEnabled() && !AppConfig.processingRingingCall) {
				
				AppConfig.processingRingingCall = true;
				// Process to control the incoming call and kill it if proceed.
				SimpleBlockerApp.getContext().startService(
						new Intent(SimpleBlockerApp.getContext(), CallBlockerService.class).putExtra(AppConfig.PARAM_INCOMING_CALL_NUMBER,
								incomingNumber));

			}

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

	/**
	 * Get what ringer mode is at the moment.
	 * 
	 * @return return RINGER_MODE_SILENT(0), RINGER_MODE_NORMAL(2),
	 *         RINGER_MODE_VIBRATE(1)
	 * @see int
	 * @throws Exception
	 */
	private int ringerMode() throws Exception {

		try {

			AudioManager audioManager = (AudioManager) SimpleBlockerApp.getContext().getSystemService(Context.AUDIO_SERVICE);

			// Only for traces
			switch (audioManager.getRingerMode()) {

			case AudioManager.RINGER_MODE_SILENT:
				Log.d(AppConfig.LOG_TAG, "Ringer_Mode_Silent");
				break;

			case AudioManager.RINGER_MODE_NORMAL:
				Log.d(AppConfig.LOG_TAG, "Ringer_Mode_Normal");
				break;

			case AudioManager.RINGER_MODE_VIBRATE:
				Log.d(AppConfig.LOG_TAG, "Ringer_Mode_Vibrate");
				break;
			default:
				break;
			}
			return audioManager.getRingerMode();
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
			throw new Exception();
		}

	}
}
