package com.smsspeaker;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Store user preferences and settings.
 * 
 */
public class Prefs {

	private SharedPreferences prefs = null;
	private Editor editor = null;
	private Context context = null;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public Prefs(Context context) {
		this.prefs = context.getSharedPreferences("PREFS_SMSSPEAKER_PRIVATE", Context.MODE_PRIVATE);
		this.editor = this.prefs.edit();
		this.context = context;
	}

	public String getValue(String key, String defaultvalue) {
		if (this.prefs == null) {
			return "?";
		}

		return this.prefs.getString(key, defaultvalue);
	}

	public void setValue(String key, String value) {
		if (this.editor == null) {
			return;
		}
		this.editor.putString(key, value);
	}

	public String getLanguage() {
		if (this.prefs == null) {
			return "-1";
		}
		return this.prefs.getString("language", "-1");
	}

	public void setLanguage(String language) {
		if (this.editor == null) {
			return;
		}
		this.editor.putString("language", language);
	}

	public boolean isEnabled() {
		if (this.prefs == null) {
			return true;
		}
		return this.prefs.getBoolean("enabled", true);
	}

	public void setEnabled(boolean enabled) {
		if (this.editor == null) {
			return;
		}
		this.editor.putBoolean("enabled", enabled);
	}
	
	public boolean isEnabledAutoDisconnectCall() {
		if (this.prefs == null) {
			return false;
		}
		return this.prefs.getBoolean("enabledautodisconnectcall", false);
	}

	public void setEnabledAutoDisconnectCall(boolean enabled) {
		if (this.editor == null) {
			return;
		}
		this.editor.putBoolean("enabledautodisconnectcall", enabled);
	}
	
	public String getRingerMode(){
		if (this.prefs == null) {
			return Constants.RINGER_MODE_DEFAULT;
		}
		return this.prefs.getString("ringermode", Constants.RINGER_MODE_DEFAULT);
	}

	public void setRingerMode(String ringerMode) {
		if (this.editor == null) {
			return;
		}
		this.editor.putString("ringermode", ringerMode);
	}
	
	public String getLanguageSpeed() {
		if (this.prefs == null) {
			return Constants.SPEED_NORMAL;
		}
		return this.prefs.getString("languagespeed", Constants.SPEED_NORMAL);
	}

	public void setLanguageSpeed(String languageSpeed) {
		if (this.editor == null) {
			return;
		}
		this.editor.putString("languagespeed", languageSpeed);
	}

	public boolean isEnableAutoresponder() {
		if (this.prefs == null) {
			return false;
		}
		return this.prefs.getBoolean("enableautoresponder", false);
	}

	public void setEnableAutoresponder(boolean enableAutoresponder) {
		if (this.editor == null) {
			return;
		}
		this.editor.putBoolean("enableautoresponder", enableAutoresponder);
	}

	public String getAutoresponderText() {
		if (this.prefs == null) {
			return context.getString(R.string.default_autoresponder_text);
		}
		return this.prefs.getString("autorespondertext", context.getString(R.string.default_autoresponder_text));
	}

	public void setAutoresponderText(String autoresponderText) {
		if (this.editor == null) {
			return;
		}
		this.editor.putString("autorespondertext", autoresponderText);
	}

	public void save() {
		if (this.editor == null) {
			return;
		}
		this.editor.commit();
	}
}
