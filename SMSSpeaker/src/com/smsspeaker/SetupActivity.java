package com.smsspeaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.speech.tts.TextToSpeech;

/**
 * Preference screen for setup activity.
 * 
 */
public final class SetupActivity extends PreferenceActivity {

	private static final String CLASS_TAG = SetupActivity.class.getSimpleName();

	private static final int ENABLE_SPEAK_BUTTON = 1;
	private static final String END_OF_SPEECH = "END";

	private SmsSpeakerApplication application;
	private TextToSpeech tts;
	private Prefs prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * ((Button)
		 * findViewById(R.id.button_clear)).setOnClickListener(onButtonClik);
		 * ((Button)
		 * findViewById(R.id.button_speak)).setOnClickListener(onButtonClik);
		 * 
		 * ((EditText)
		 * findViewById(R.id.text)).addTextChangedListener(textWather);
		 * ((EditText) findViewById(R.id.text)).setText(demoText);
		 */

		addPreferencesFromResource(R.xml.preferences);
		application = (SmsSpeakerApplication) getApplication();
		tts = application.getTTS();
		tts.setOnUtteranceCompletedListener(onUtteranceCompleted);
		prefs = new Prefs(this);

		// SMSSpeaker is enabled
		final CheckBoxPreference checkPref = (CheckBoxPreference) findPreference("enableSMSSpeakerPref");
		checkPref.setChecked(prefs.isEnabled());
		checkPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean enabled = (Boolean) newValue;
				prefs.setEnabled(enabled);
				prefs.save();
				return true;
			}
		});

		// Auto disconnect incoming call
		final CheckBoxPreference disconnectCheckPref = (CheckBoxPreference) findPreference("enableAutoDisconnectPref");
		disconnectCheckPref.setChecked(prefs.isEnabledAutoDisconnectCall());
		disconnectCheckPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean enabled = (Boolean) newValue;
				prefs.setEnabledAutoDisconnectCall(enabled);
				prefs.save();
				return true;
			}
		});

		// Set language options and default value
		final ListPreference languagePref = (ListPreference) findPreference("languagePref");
		final List<Locale> availableLocales = application.getAvailableLocales();
		final List<String> listEntryValues = new ArrayList<String>(availableLocales.size());
		final List<String> listEntries = new ArrayList<String>(availableLocales.size());
		int index = 0;
		for (Locale locale : availableLocales) {
			listEntries.add(locale.getDisplayName());
			listEntryValues.add(String.valueOf(index++));
		}
		CharSequence[] languageEntryValues = listEntryValues.toArray(new CharSequence[listEntryValues.size()]);
		languagePref.setEntryValues(languageEntryValues);
		CharSequence[] languageEntries = listEntries.toArray(new CharSequence[listEntries.size()]);
		languagePref.setEntries(languageEntries);

		String selectedLanguage = prefs.getLanguage();
		if (listEntryValues.contains(selectedLanguage)) {
			languagePref.setValue(selectedLanguage);
		}
		languagePref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				int selectedIndex = Integer.parseInt(newValue.toString());
				if (tts != null) {
					tts.setLanguage(availableLocales.get(selectedIndex));
				}
				prefs.setLanguage(newValue.toString());
				prefs.save();
				return true;
			}
		});

		// Language speed preference
		final ListPreference languageSpeedPref = (ListPreference) findPreference("languageSpeedPref");
		languageSpeedPref.setValue(prefs.getLanguageSpeed());
		languageSpeedPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				String speedRate = newValue.toString();
				if (tts != null) {
					tts.setSpeechRate(Float.parseFloat(speedRate));
				}
				prefs.setLanguageSpeed(speedRate);
				prefs.save();
				return true;
			}
		});

		
		// Ringer mode preference
		final ListPreference ringerModePref = (ListPreference) findPreference("ringerModePref");
		ringerModePref.setValue(prefs.getRingerMode());
		ringerModePref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				String ringerMode = newValue.toString();
				prefs.setRingerMode(ringerMode);
				prefs.save();
				return true;
			}
		});
		
		
		// Enable autoresponder
		final CheckBoxPreference autoResponderCheckPref = (CheckBoxPreference) findPreference("enableAutoresponderPref");
		autoResponderCheckPref.setChecked(prefs.isEnableAutoresponder());
		autoResponderCheckPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean enabled = (Boolean) newValue;
				prefs.setEnableAutoresponder(enabled);
				prefs.save();
				return true;
			}
		});

		// Autoresponder text
		final EditTextPreference autoResponderEditPref = (EditTextPreference) findPreference("autoresponderTextPref");
		autoResponderEditPref.setText(prefs.getAutoresponderText());
		autoResponderEditPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				String text = (String) newValue;
				prefs.setAutoresponderText(text);
				prefs.save();
				return true;
			}
		});

	}

	private final TextToSpeech.OnUtteranceCompletedListener onUtteranceCompleted = new TextToSpeech.OnUtteranceCompletedListener() {
		@Override
		public void onUtteranceCompleted(String utteranceId) {
			if (0 == utteranceId.compareToIgnoreCase(END_OF_SPEECH)) {
				handler.sendEmptyMessage(ENABLE_SPEAK_BUTTON);
			}
		}
	};

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ENABLE_SPEAK_BUTTON: {
				// ((Button) findViewById(R.id.button_speak)).setEnabled(true);
				break;
			}
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.msg_confirm_exit)).setCancelable(false)
				.setPositiveButton(getString(R.string.label_yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						SetupActivity.this.finish();
					}
				}).setNegativeButton(getString(R.string.label_no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/*
	 * 
	 * private static final int LANGUAGE_MENU = 0; private static final int
	 * SPEED_MENU = 1000; private static final String demoText = "Sample text";
	 * 
	 * @Override public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); setContentView(R.layout.setup);
	 * 
	 * availableLocales = new ArrayList<Locale>(); tts = new TextToSpeech(this,
	 * this);
	 * 
	 * ((Button)
	 * findViewById(R.id.button_clear)).setOnClickListener(onButtonClik);
	 * ((Button)
	 * findViewById(R.id.button_speak)).setOnClickListener(onButtonClik);
	 * 
	 * ((EditText) findViewById(R.id.text)).addTextChangedListener(textWather);
	 * ((EditText) findViewById(R.id.text)).setText(demoText); }
	 * 
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { boolean result
	 * = super.onCreateOptionsMenu(menu); String menutitle = "";
	 * 
	 * SubMenu lanugageMenu = menu.addSubMenu(0, LANGUAGE_MENU, Menu.NONE,
	 * "Language");
	 * 
	 * lanugageMenu.setHeaderTitle("Select Language");
	 * 
	 * for (int index = 0; index < availableLocales.size(); ++index) { menutitle
	 * = availableLocales.get(index).getDisplayLanguage() + " (" +
	 * availableLocales.get(index).getDisplayCountry() + ")";
	 * 
	 * lanugageMenu.add(0, LANGUAGE_MENU + index + 1, Menu.NONE, menutitle); }
	 * 
	 * SubMenu speedMenu = menu.addSubMenu(0, SPEED_MENU, Menu.NONE, "Speed");
	 * 
	 * speedMenu.add(0, SPEED_MENU + 1, Menu.NONE, "Very Slow");
	 * speedMenu.add(0, SPEED_MENU + 2, Menu.NONE, "Slow"); speedMenu.add(0,
	 * SPEED_MENU + 3, Menu.NONE, "Normal"); speedMenu.add(0, SPEED_MENU + 4,
	 * Menu.NONE, "Fast"); speedMenu.add(0, SPEED_MENU + 5, Menu.NONE,
	 * "Very Fast");
	 * 
	 * return result; }
	 * 
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { boolean
	 * result = true; int itemId = item.getItemId();
	 * 
	 * if (itemId > SPEED_MENU) { setTextToSpeechRate(itemId - SPEED_MENU - 1);
	 * } else if (itemId > LANGUAGE_MENU && itemId < SPEED_MENU) {
	 * setTextToSpeechLocale(itemId - LANGUAGE_MENU - 1); }
	 * 
	 * return result; }
	 * 
	 * public boolean onContextItemSelected(MenuItem item) {
	 * AdapterContextMenuInfo info = (AdapterContextMenuInfo)
	 * item.getMenuInfo(); boolean result = true;
	 * 
	 * long id = info.id - Menu.FIRST;
	 * 
	 * Log.i("TTSDemo", "MenuItem Selectd: " + id);
	 * 
	 * return result; }
	 * 
	 * 
	 * 
	 * private void setTextToSpeechRate(int index) { float rate = (float) 1.0;
	 * 
	 * switch (index) { case 0: rate = (float) 0.1; break; case 1: rate =
	 * (float) 0.5; break; case 2: rate = (float) 1.0; break; case 3: rate =
	 * (float) 1.5; break; case 4: rate = (float) 2.0; break; }
	 * 
	 * Log.i("TTSDemo", "SpeechRate: " + rate + "(" + index + ")");
	 * 
	 * tts.setSpeechRate(rate); }
	 * 
	 * private void setTextToSpeechLocale(int index) {
	 * tts.setLanguage(availableLocales.get(index));
	 * 
	 * Log.i("TTSDemo", "Language: " +
	 * availableLocales.get(index).getDisplayLanguage() + " (" +
	 * availableLocales.get(index).getDisplayCountry() + ")"); }
	 * 
	 * 
	 * 
	 * private final View.OnClickListener onButtonClik = new
	 * View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { switch (v.getId()) { case
	 * R.id.button_clear: { ((EditText) findViewById(R.id.text)).setText("");
	 * 
	 * break; } case R.id.button_speak: { String text = ((EditText)
	 * findViewById(R.id.text)).getText().toString(); HashMap<String, String>
	 * hash = new HashMap<String, String>();
	 * 
	 * hash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, END_OF_SPEECH);
	 * tts.speak(text, TextToSpeech.QUEUE_FLUSH, hash);
	 * 
	 * ((Button) findViewById(R.id.button_speak)).setEnabled(false);
	 * 
	 * break; } } } };
	 * 
	 * private final TextWatcher textWather = new TextWatcher() {
	 * 
	 * @Override public void afterTextChanged(Editable editable) { ((Button)
	 * findViewById(R.id.button_clear)).setEnabled(editable.toString().length()
	 * > 0); }
	 * 
	 * @Override public void beforeTextChanged(CharSequence arg0, int arg1, int
	 * arg2, int arg3) { }
	 * 
	 * @Override public void onTextChanged(CharSequence arg0, int arg1, int
	 * arg2, int arg3) { } };
	 */
}