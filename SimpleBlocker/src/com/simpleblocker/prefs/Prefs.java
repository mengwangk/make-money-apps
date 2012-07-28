package com.simpleblocker.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.simpleblocker.utils.AppConfig.BlockBehavior;
import com.simpleblocker.utils.AppConfig.BlockType;

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
		this.prefs = context.getSharedPreferences("PREFS_SIMPLEBLOCKER_PRIVATE", Context.MODE_PRIVATE);
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
	
	public void setBlockType(BlockType type){
		if (this.editor == null) {
			return;
		}
		this.editor.putInt("blocktype", type.getCode());
	}
	
	public int getBlockType() {
		if (this.prefs == null) {
			return BlockType.BLOCK_BLOCKED_CONTACTS.getCode();
		}
		return this.prefs.getInt("blocktype", BlockType.BLOCK_BLOCKED_CONTACTS.getCode());
	}
	
	
	public void setBlockBehavior(BlockBehavior behavior){
		if (this.editor == null) {
			return;
		}
		this.editor.putInt("blockbehavior", behavior.getCode());
	}
	
	public int getBlockBehavior() {
		if (this.prefs == null) {
			return BlockBehavior.HANG_UP.getCode();
		}
		return this.prefs.getInt("blockbehavior", BlockBehavior.HANG_UP.getCode());
	}
	
	public void save() {
		if (this.editor == null) {
			return;
		}
		this.editor.commit();
	}
}
