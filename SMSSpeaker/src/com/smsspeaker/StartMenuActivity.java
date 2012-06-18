package com.smsspeaker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TabHost;

import com.android.internal.telephony.ITelephony;
import com.smsspeaker.helper.DbHelper;

/**
 * <p>
 * The main screen which displays all the tabs, and also initializes the TTS
 * engine.
 * </p>
 * <p>
 * It contains a broadcast receiver which receives the broadcast from SMS
 * receiver and uses the TTS engine to read out the message and display it in
 * the message viewer activity.
 * </p>
 */
public final class StartMenuActivity extends TabActivity implements TextToSpeech.OnInitListener {

	private static final String CLASS_TAG = StartMenuActivity.class.getSimpleName();
	private TextToSpeech tts = null;
	private SmsSpeakerApplication application;
	private AlertDialog.Builder adb;
	private boolean isTTSAvailable = false;

	// This code can be any value you want, its just a checksum.
	private static final int TTS_DATA_CHECK_CODE = 1234;

	private static ITelephony telephonyService = null;

	static {
		telephonyService = createITelephonyImp();
	}

	// private String receivedMsg = StringUtils.EMPTY;

	private static ITelephony createITelephonyImp() {
		try {

			TelephonyManager telephonyManager = (TelephonyManager) SmsSpeakerApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);

			// Java reflection to gain access to TelephonyManager
			Class c = Class.forName(telephonyManager.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			ITelephony telephonyService = (ITelephony) m.invoke(telephonyManager);
			return telephonyService;
		} catch (Exception ex) {
			Log.e(CLASS_TAG, "Problem getting telephony service [" + ex.getMessage() + "]");
			return null;
		}
	}

	/**
	 * This function checks is must be re-initialize the telephnyService
	 */
	private static void checkTelephonyService() {
		if (telephonyService == null)
			telephonyService = createITelephonyImp();
	}

	/**
	 * Put the mobile in silence mode (audio and vibrate)
	 * 
	 */
	private static void putRingerMode(String ringerMode) {
		try {
			if (Constants.RINGER_MODE_DEFAULT.equals(ringerMode))
				return;

			AudioManager audioManager = (AudioManager) SmsSpeakerApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
			if (Constants.RINGER_MODE_NORMAL.equals(ringerMode))
				audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			else if (Constants.RINGER_MODE_VIBRATE.equals(ringerMode))
				audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			else if (Constants.RINGER_MODE_SILENT.equals(ringerMode))
				audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		} catch (Exception ex) {
			Log.e(CLASS_TAG, "Problem putting ringer in desired mode [" + ex.getMessage() + "]");
		}
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startmenu);
		adb = new AlertDialog.Builder(this);
		application = (SmsSpeakerApplication) getApplication();

		// Fire off an intent to check if a TTS engine is installed
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, TTS_DATA_CHECK_CODE);
	}

	private BroadcastReceiver smsBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			speakOut(intent);
		}
	};

	private BroadcastReceiver callBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			processCall(intent);
		}
	};

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// receivedMsg = intent.getStringExtra(Constants.PARAM_MSG);
	}

	@Override
	public void onInit(final int status) {
		isTTSAvailable = (TextToSpeech.SUCCESS == status);

		if (isTTSAvailable) {

			// Get all available locales
			application.setAvailableLocales(EnumerateAvailableLanguages());

			// Set TTS settings
			Prefs prefs = new Prefs(this);
			int languageIndex = Integer.parseInt(prefs.getLanguage());
			if (languageIndex >= 0) {
				application.getTTS().setLanguage(application.getAvailableLocales().get(languageIndex));
			}
			application.getTTS().setSpeechRate(Float.parseFloat(prefs.getLanguageSpeed()));

			// Add the tabs
			addTabs();

			/*InboundData m = new InboundData("1234567890", "testing message", Constants.DATA_TYPE_SMS, new GregorianCalendar().getTime());
			m.isNew = true;
			Intent intent = new Intent(Constants.BROADCAST_ACTION_SMS_RECEIVED);
			intent.putExtra(Constants.PARAM_MSG, m);
			speakOut(intent);*/

		} else {
			// TTS not available, show message
			AlertDialog ad = this.adb.create();
			ad.setMessage(this.getString(R.string.msg_tts_not_available));
			ad.show();
			return;
		}
	}

	/**
	 * Add the menu tabs to the application
	 */
	private void addTabs() {
		final Resources res = getResources();
		final TabHost tabHost = getTabHost();

		final Intent intent1 = new Intent(this, ViewerActivity.class);
		tabHost.addTab(tabHost.newTabSpec(res.getString(R.string.tab_viewer))
				.setIndicator(res.getString(R.string.tab_viewer), res.getDrawable(R.drawable.ic_tab_message)).setContent(intent1));

		final Intent intent2 = new Intent(this, SetupActivity.class);
		tabHost.addTab(tabHost.newTabSpec(res.getString(R.string.tab_setup))
				.setIndicator(res.getString(R.string.tab_setup), res.getDrawable(R.drawable.ic_tab_setup)).setContent(intent2));

		final Intent intent3 = new Intent(this, HelpActivity.class);
		tabHost.addTab(tabHost.newTabSpec(res.getString(R.string.tab_help))
				.setIndicator(res.getString(R.string.tab_help), res.getDrawable(R.drawable.ic_tab_help)).setContent(intent3));

		tabHost.setCurrentTab(0);

		// Set tabs Colors
		tabHost.setBackgroundColor(Color.BLACK);
		tabHost.getTabWidget().setBackgroundColor(Color.BLACK);
	}

	/**
	 * This is the callback from the TTS engine check, if a TTS is installed we
	 * create a new TTS instance (which in turn calls onInit), if not then we
	 * will create an intent to go off and install a TTS engine
	 * 
	 * @param requestCode
	 *            int Request code returned from the check for TTS engine.
	 * @param resultCode
	 *            int Result code returned from the check for TTS engine.
	 * @param data
	 *            Intent Intent returned from the TTS check.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TTS_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

				// success, create the TTS instance
				tts = new TextToSpeech(this, this);

				application.setTTS(tts);

			} else {

				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	@Override
	protected void onPause() {
		try {
			super.onPause();
		} catch (Exception ex) {
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(smsBroadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION_SMS_RECEIVED));
		registerReceiver(callBroadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION_CALL_RECEIVED));
		// if (!StringUtils.isNullorEmpty(receivedMsg)){
		// tts.speak(receivedMsg, TextToSpeech.QUEUE_FLUSH, null);
		// }
	}

	@Override
	protected void onDestroy() {

		// Don't forget to shutdown TTS engine!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}

		try {
			unregisterReceiver(smsBroadcastReceiver);
		} catch (Exception ex) {
		}

		try {
			unregisterReceiver(callBroadcastReceiver);
		} catch (Exception ex) {
		}

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.msg_confirm_exit)).setCancelable(false)
				.setPositiveButton(getString(R.string.label_yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						StartMenuActivity.this.finish();
					}
				}).setNegativeButton(getString(R.string.label_no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Get all supported locale.
	 * 
	 * @return All supported locale.
	 */
	private final List<Locale> EnumerateAvailableLanguages() {
		final List<Locale> availableLocales = new ArrayList<Locale>();
		final Locale locales[] = Locale.getAvailableLocales();
		for (int index = 0; index < locales.length; ++index) {
			if (TextToSpeech.LANG_COUNTRY_AVAILABLE == tts.isLanguageAvailable(locales[index])) {
				Log.i(CLASS_TAG, locales[index].getDisplayLanguage() + " (" + locales[index].getDisplayCountry() + ")");
				availableLocales.add(locales[index]);
			}
		}
		return availableLocales;
	}

	/**
	 * Read out the message in a separate thread.
	 * 
	 * @param intent
	 *            Intent containing the message.
	 */
	private void speakOut(final Intent intent) {
		Thread t = new Thread(new Speaker(intent));
		t.start();
	}

	/**
	 * Process incoming call
	 * 
	 * @param intent
	 *            Intent containing the call details.
	 */
	private void processCall(final Intent intent) {
		Thread t = new Thread(new CallReceiver(intent));
		t.start();
	}

	private void sendSMS(final String phoneNumber, final String text) {
		try {
			String SENT = "SMS_SENT";
			String DELIVERED = "SMS_DELIVERED";

			PendingIntent sentPI = PendingIntent.getBroadcast(StartMenuActivity.this, 0, new Intent(SENT), 0);
			PendingIntent deliveredPI = PendingIntent.getBroadcast(StartMenuActivity.this, 0, new Intent(DELIVERED), 0);

			// when the SMS has been sent
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					switch (getResultCode()) {
					case Activity.RESULT_OK:
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					case SmsManager.RESULT_ERROR_NO_SERVICE:
					case SmsManager.RESULT_ERROR_NULL_PDU:
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						break;
					}
				}
			}, new IntentFilter(SENT));

			// when the SMS has been delivered
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					switch (getResultCode()) {
					case Activity.RESULT_OK:
						break;
					case Activity.RESULT_CANCELED:
						break;
					}
				}
			}, new IntentFilter(DELIVERED));

			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, text, sentPI, deliveredPI);
		} catch (Exception ex) {
			Log.e(CLASS_TAG, "Problem sending SMS [" + ex.getMessage() + "]");
		}
	}

	private void insertInboundData(final InboundData inboundData) {
		DbHelper dbHelper = new DbHelper(StartMenuActivity.this);
		try {
			dbHelper.insertInboundData(inboundData);
			dbHelper.cleanUp();
			dbHelper = null;
		} catch (Exception ex) {
			Log.e(CLASS_TAG, "Problem inserting information into database [" + ex.getMessage() + "]");
		} finally {
			if (dbHelper != null) {
				dbHelper.cleanUp();
				dbHelper = null;
			}
		}
	}

	/**
	 * Runnable to use the TTS to read out the received message.
	 * 
	 */
	class Speaker implements Runnable {

		private Intent intent;

		public Speaker(Intent intent) {
			this.intent = intent;
		}

		@Override
		public void run() {
			if (!isTTSAvailable)
				return;

			Prefs prefs = new Prefs(StartMenuActivity.this);
			if (!prefs.isEnabled())
				return;

			// Read the SMS details
			InboundData msg = (InboundData) intent.getSerializableExtra(Constants.PARAM_MSG);
			tts.speak(msg.subject + " " + msg.details, TextToSpeech.QUEUE_FLUSH, null);

			// Display the message in the viewer in ViewerActivity
			Intent intent = new Intent(Constants.BROADCAST_ACTION_SMS_RECEIVED_DISPLAY);
			intent.putExtra(Constants.PARAM_MSG, msg);
			sendBroadcast(intent);

			// Autoresponder is required?
			if (prefs.isEnableAutoresponder()) {
				String autoResponderText = prefs.getAutoresponderText();
				if (!TextUtils.isEmpty(autoResponderText)) {
					sendSMS(msg.subject, autoResponderText);
				}
			}

			insertInboundData(msg);
		}
	}

	/**
	 * Runnable to process the incoming call.
	 * 
	 */
	class CallReceiver implements Runnable {

		private Intent intent;

		public CallReceiver(Intent intent) {
			this.intent = intent;
		}

		@Override
		public void run() {
			if (!isTTSAvailable)
				return;

			Prefs prefs = new Prefs(StartMenuActivity.this);
			if (!prefs.isEnabled())
				return;

			if (prefs.isEnabledAutoDisconnectCall()) {
				hangUp();
			} else {
				putRingerMode(prefs.getRingerMode());
			}

			// Get the call details
			InboundData call = (InboundData) intent.getSerializableExtra(Constants.PARAM_CALL);
			String phoneNumber = call.subject;
			call.subject = String.format(StartMenuActivity.this.getString(R.string.msg_call_from), phoneNumber);
			tts.speak(call.subject, TextToSpeech.QUEUE_FLUSH, null);

			// Display the message in the viewer in ViewerActivity
			Intent intent = new Intent(Constants.BROADCAST_ACTION_CALL_RECEIVED_DISPLAY);
			intent.putExtra(Constants.PARAM_CALL, call);
			sendBroadcast(intent);

			// Autoresponder is required?
			if (prefs.isEnableAutoresponder()) {
				String autoResponderText = prefs.getAutoresponderText();
				if (!TextUtils.isEmpty(autoResponderText)) {
					sendSMS(phoneNumber, autoResponderText);
				}
			}
			insertInboundData(call);

			/*
			 * Context ctx = StartMenuActivity.this.getApplicationContext();
			 * Intent i = new Intent(ctx, StartMenuActivity.class);
			 * i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); startActivity(i);
			 */
		}

		/**
		 * Hang up the incoming call by accessing the telephony internal class
		 * using Java reflection.
		 * 
		 */
		private void hangUp() {
			try {
				checkTelephonyService();
				if (telephonyService != null) {
					telephonyService.silenceRinger();
					telephonyService.endCall();

					if (telephonyService.isRinging()) {
						// Still ringing
						Log.i(CLASS_TAG, "Problem hanging up call. Try to use airplane mode");
						toggleAirplaneMode();

						// Sleep 5 seconds before turning off
						try {
							Thread.sleep(5000);
						} catch (InterruptedException ie) {
						}
						toggleAirplaneMode();
					}
				}
			} catch (Exception ex) {
				Log.e(CLASS_TAG, "Problem hanging up call [" + ex.getMessage() + "]");
			}
		}

		public void toggleAirplaneMode() {
			try {
				boolean isEnabled = Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
				Settings.System.putInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);
				Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
				intent.putExtra("state", !isEnabled);
				sendBroadcast(intent);
			} catch (Exception e) {
			}
		}

	}
}