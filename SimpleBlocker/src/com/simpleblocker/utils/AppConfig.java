package com.simpleblocker.utils;

import java.util.HashMap;
import java.util.Map;

public final class AppConfig {

	/**
	 * Logging tag to easily differentiate the log entries for this application.
	 */
	public static final String LOG_TAG = "SimpleBlocker";

	/**
	 * Default theme used in this application.
	 */
	public static final Integer MY_THEME = com.actionbarsherlock.R.style.Theme_Sherlock;

	// Help file URIs
	public static final String HELP_ABOUT_URI = "file:///android_asset/about.html";
	public static final String HELP_HELP_URI = "file:///android_asset/help.html";

	public static final String FRAGMENT_TYPE = "TYPE_FRAGMENT";

	/**
	 * Enum to differentiate between different contact requests.
	 * 
	 */
	public static enum FragmentType {
		GET_CONTACTS, GET_CALL_LOGS, GET_BLOCKED_CALL_LOGS, ADD_CONTACTS, REMOVE_CONTACTS, GET_BLOCKED_CONTACTS, SERVICE_SETTINGS_CONFIG
	}

	public static enum DialogType {
		ADD_CONTACTS, NO_CONTACT_SELECTED, REMOVE_CONTACTS, ADD_CONTACT_FROM_CALL_LOGS, REMOVE_CALL_LOGS, NO_CALL_LOG_SELECTED, REFRESH_CALL_LOGS
	}

	public static enum BlockType {

		BLOCK_BLOCKED_CONTACTS(0), BLOCK_UNKNOWN_AND_BLOCKED_CONTACTS(1), BLOCK_UNKNOWN_CALLS(2), BLOCK_ALL_CALL(3);

		private int code;

		private BlockType(int code) {
			this.code = code;
		}

		public int getCode() {
			return this.code;
		}
		
		private static final Map<Integer, BlockType> codeToEnum = new HashMap<Integer, BlockType>();
		static { 
			for (BlockType bt : values())
				codeToEnum.put(bt.getCode(), bt);
		}

		// Returns Operation for string, or null if string is invalid
		public static BlockType fromCode(Integer code) {
			return codeToEnum.get(code);
		}

	}

	public static enum BlockBehavior {

		HANG_UP(0), SILENCE(1);

		private int code;

		private BlockBehavior(int code) {
			this.code = code;
		}

		public int getCode() {
			return this.code;
		}

		private static final Map<Integer, BlockBehavior> codeToEnum = new HashMap<Integer, BlockBehavior>();
		static { 
			for (BlockBehavior bb : values())
				codeToEnum.put(bb.getCode(), bb);
		}

		// Returns Operation for string, or null if string is invalid
		public static BlockBehavior fromCode(Integer code) {
			return codeToEnum.get(code);
		}

	}

	public static final String PARAM_CONTACT_NAME = "CONTACT_NAME";

	public static final String PARAM_NUM_BANNED = "NUM_BANNED";

	public static final String PARAM_NUM_REMOVED = "NUM_REMOVED";

	public static final String PARAM_INCOMING_CALL_NUMBER = "INCOMING_CALL_NO";

	public static final String PARAM_CALL_LOGS = "CALL_LOGS";

	// Request code for activities

	public static final int REQUEST_CODE_ADD_FROM_CONTACTS = 1;
	public static final int REQUEST_CODE_ADD_FROM_CALL_LOGS = 2;
	
	
	public static boolean processingRingingCall = false;

}
