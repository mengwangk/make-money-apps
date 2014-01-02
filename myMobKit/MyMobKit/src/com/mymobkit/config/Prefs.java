package com.mymobkit.config;

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
	public Prefs(final Context context) {
		this.prefs = context.getSharedPreferences("PREFS_MYMOBKIT_PRIVATE", Context.MODE_PRIVATE);
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

	public boolean isAppEnabled() {
		if (this.prefs == null) {
			return false;
		}
		return this.prefs.getBoolean("isappenabled", false);
	}

	public void setAppEnabled(boolean enabled) {
		if (this.editor == null) {
			return;
		}
		this.editor.putBoolean("isappenabled", enabled);
	}

	public void save() {
		if (this.editor == null) {
			return;
		}
		this.editor.commit();
	}
}
