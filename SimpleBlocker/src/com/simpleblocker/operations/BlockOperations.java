package com.simpleblocker.operations;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.simpleblocker.SimpleBlockerApp;
import com.simpleblocker.data.DbHelper;
import com.simpleblocker.data.models.BlockedCallLog;
import com.simpleblocker.data.models.Contact;
import com.simpleblocker.data.models.EmptyBlockedCallLog;
import com.simpleblocker.data.models.Phone;
import com.simpleblocker.prefs.Prefs;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.AppConfig.BlockBehavior;
import com.simpleblocker.utils.ExceptionUtils;

public final class BlockOperations {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Date formatter
	 */
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

	private static ITelephony telephonyService = null;

	static {
		telephonyService = createITelephonyImp();
	}

	/**
	 * This function checks is must be re-initialize the telephnyService
	 */
	private static void checkTelephonyService() {
		if (telephonyService == null)
			telephonyService = createITelephonyImp();
	}

	/**
	 * Format a date using the default format.
	 * 
	 * @param dt
	 * @return
	 */
	public static String formatDate(final Date dt) {
		try {
			if (dt == null)
				return "";
			return DATE_FORMATTER.format(dt);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * @param prefs
	 */
	private static void muteOrSilenceAction(final Prefs prefs) {

		try {

			switch (BlockBehavior.fromCode(prefs.getBlockBehavior())) {
			case HANG_UP:
				//telephonyService.silenceRinger();
				telephonyService.endCall();

				if (telephonyService.isRinging()) {
					// Still ringing
					Log.i(AppConfig.LOG_TAG, "Problem hanging up call. Try to use airplane mode");
					toggleAirplaneMode();

					// Sleep 5 seconds before turning off
					try {
						Thread.sleep(5000);
					} catch (InterruptedException ie) {
					}
					toggleAirplaneMode();
				}
				break;

			case SILENCE:

				AudioManager audioManager = (AudioManager) SimpleBlockerApp.getContext().getSystemService(Context.AUDIO_SERVICE);

				// save original audio state
				int oldMode = audioManager.getRingerMode();

				// Put to silent mode
				audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

				TelephonyManager tm = (TelephonyManager) SimpleBlockerApp.getContext().getSystemService(Context.TELEPHONY_SERVICE);

				// if handling mode was silence, we have to wait the phone
				// to stop
				// ringing
				// if handling mode was block,
				// TelephonyManager.CALL_STATE_RINGING will
				// be false, ringer mode will be restored immediately
				while (tm.getCallState() == TelephonyManager.CALL_STATE_RINGING) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
					}
				}
				// restore saved audio state
				audioManager.setRingerMode(oldMode);

				break;

			}
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

	/**
	 * This function does use of reflection process to get a ITelephony object
	 * that is implemented automatically by de compiler using the
	 * com.android.internal.telephony.ITelephony.aidl interface. This aidl is
	 * the interface of the com.android.telephony.ITelephony whic is located by
	 * internal way inside the Android OS and can't use throw the standard and
	 * public SDK.
	 * 
	 * This code is based in the post written by Prasanta Paul on his blog and
	 * post: @link
	 * http://prasanta-paul.blogspot.com/2010/09/call-control-in-android.html
	 */
	private static ITelephony createITelephonyImp() {

		try {

			TelephonyManager telephonyManager = (TelephonyManager) SimpleBlockerApp.getContext().getSystemService(Context.TELEPHONY_SERVICE);

			// Java reflection to gain access to TelephonyManager

			Class c = Class.forName(telephonyManager.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);

			com.android.internal.telephony.ITelephony telephonyService = (ITelephony) m.invoke(telephonyManager);

			return telephonyService;

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
			return null;
		}
	}

	/**
	 * Put the mobile in silence mode (audio and vibrate)
	 * 
	 */
	private static void putRingerModeSilent() {

		// @Todo - save original mode, silent and after ringing, restore ring
		// mode
		try {
			AudioManager audioManager = (AudioManager) SimpleBlockerApp.getContext().getSystemService(Context.AUDIO_SERVICE);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

	/**
	 * Put the system into normal ring mode.
	 */
	private static void putRingerModeNormal() {

		try {
			AudioManager audioManager = (AudioManager) SimpleBlockerApp.getContext().getSystemService(Context.AUDIO_SERVICE);

			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

	/**
	 * Put the vibrator in mode off
	 */
	private static void vibrateOff() {

		try {

			String vibratorService = Context.VIBRATOR_SERVICE;
			Vibrator vibrator = (Vibrator) SimpleBlockerApp.getContext().getSystemService(vibratorService);
			vibrator.cancel();

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

	private static void toggleAirplaneMode() {
		try {
			boolean isEnabled = Settings.System.getInt(SimpleBlockerApp.getContext().getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
			Settings.System.putInt(SimpleBlockerApp.getContext().getContentResolver(), Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);
			Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			intent.putExtra("state", !isEnabled);
			SimpleBlockerApp.getContext().sendBroadcast(intent);
		} catch (Exception e) {
		}
	}

	private static String getCurrentTimestamp() {
		try {
			GregorianCalendar cal = new GregorianCalendar();
			return formatDate(cal.getTime());
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
			return null;
		}
	}

	// cut out special chars from saved numbers
	private static String normalizePhoneNum(String s) {
		s = s.replaceAll("[^0123456789]", "");
		return s;
	}

	private static boolean isMatchWithWildCard(final DbHelper dbHelper, final Prefs prefs, final String incomingNumber, final String contactName) {
		List<String> startsWithList = new ArrayList<String>(1);
		List<String> endsWithList = new ArrayList<String>(1);
		List<String> containsList = new ArrayList<String>(1);

		List<Contact> wildCardContacts = dbHelper.getWildCardContacts();
		for (Contact c : wildCardContacts) {
			for (Phone p : c.getPhoneList()) {
				String no = p.getContactPhone().trim();
				if (no.startsWith("*") && no.endsWith("*")) {
					// send to 'contains array'
					containsList.add(normalizePhoneNum(no.substring(1, no.length() - 1)));
				} else if (no.startsWith("*")) {
					// send to 'ends with array'
					endsWithList.add(normalizePhoneNum(no.substring(1, no.trim().length())));
				} else if (no.endsWith("*")) {
					// send to 'starts with array'
					startsWithList.add(normalizePhoneNum(no.substring(0, no.trim().length() - 1)));
				}
			}
		}

		if (isNumStartsWith(startsWithList, incomingNumber))
			return true;
		if (isNumContains(containsList, incomingNumber))
			return true;
		if (isNumEndsWith(endsWithList, incomingNumber))
			return true;

		return false;
	}

	private static boolean isNumStartsWith(final List<String> startsWithList, final String number) {
		boolean result = false;
		for (String s : startsWithList) {
			if (!result) {
				if (number.startsWith(s.trim())) {
					result = true;
				}
			}
		}
		return result;
	}

	private static boolean isNumContains(final List<String> containsList, final String number) {
		boolean result = false;
		for (String s : containsList) {
			if ((!result) && (number.contains(s.trim()))) {
				result = true;
			}
		}
		return result;
	}

	private static boolean isNumEndsWith(final List<String> endsWithList, final String number) {
		boolean result = false;
		for (String s : endsWithList) {
			if ((!result) && (number.endsWith(s.trim()))) {
				result = true;
			}
		}
		return result;
	}

	public static BlockedCallLog blockBlockedContacts(final DbHelper dbHelper, final Prefs prefs, final String incomingNumber,
			final String contactName) {

		try {
			Contact contact = dbHelper.isNumberBlocked(incomingNumber);
			if (contact.isEmpty()) {
				// check for wildcard matching
				if (isMatchWithWildCard(dbHelper, prefs, incomingNumber, contactName)) {
					checkTelephonyService();
					muteOrSilenceAction(prefs);
					return new BlockedCallLog(contactName, incomingNumber, getCurrentTimestamp());
				}
			} else {
				checkTelephonyService();
				muteOrSilenceAction(prefs);
				return new BlockedCallLog(contact.getContactName(), incomingNumber, getCurrentTimestamp());
			}

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}

		return new EmptyBlockedCallLog();
	}

	public static BlockedCallLog blockUnknownAndBlockedContacts(final DbHelper dbHelper, final Prefs prefs, final String incomingNumber,
			final String contactName) {

		try {
			// Check for unknown contact
			if (TextUtils.isEmpty(contactName)) {
				checkTelephonyService();
				muteOrSilenceAction(prefs);
				return new BlockedCallLog(contactName, incomingNumber, getCurrentTimestamp());
			}

			// Check for blocked contacts
			Contact contact = dbHelper.isNumberBlocked(incomingNumber);
			if (contact.isEmpty()) {
				// check for wildcard matching
				if (isMatchWithWildCard(dbHelper, prefs, incomingNumber, contactName)) {
					checkTelephonyService();
					muteOrSilenceAction(prefs);
					return new BlockedCallLog(contactName, incomingNumber, getCurrentTimestamp());
				}
			} else {
				checkTelephonyService();
				muteOrSilenceAction(prefs);
				return new BlockedCallLog(contact.getContactName(), incomingNumber, getCurrentTimestamp());
			}

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}

		return new EmptyBlockedCallLog();
	}

	public static BlockedCallLog blockUnknownCalls(final DbHelper dbHelper, final Prefs prefs, final String incomingNumber, final String contactName) {

		if (!TextUtils.isEmpty(contactName)) {
			return new EmptyBlockedCallLog();
		}
		try {
			checkTelephonyService();
			muteOrSilenceAction(prefs);
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
		return new BlockedCallLog(contactName, incomingNumber, getCurrentTimestamp());
	}

	public static BlockedCallLog blockedAllCalls(final DbHelper dbHelper, final Prefs prefs, final String incomingNumber, final String contactName) {
		try {
			checkTelephonyService();
			muteOrSilenceAction(prefs);
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
		return new BlockedCallLog(contactName, incomingNumber, getCurrentTimestamp());
	}

}
