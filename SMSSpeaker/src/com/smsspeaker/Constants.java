package com.smsspeaker;

/**
 * Application constants.
 * 
 * @author MEKOH
 *
 */
public final class Constants {
	
	/**
	 * LogCat tag
	 */
	public final static String LOG_TAG = "SMSSpeaker";
	
	public static final String SPEED_NORMAL = "1.0";
	
	public static final String BROADCAST_ACTION_SMS_RECEIVED = "com.smsspeaker.SMS_RECEIVED_BROADCAST";
	public static final String BROADCAST_ACTION_SMS_RECEIVED_DISPLAY = "com.smsspeaker.SMS_RECEIVED_DISPLAY";
	public static final String BROADCAST_ACTION_CALL_RECEIVED = "com.smsspeaker.CALL_RECEIVED_BROADCAST";
	public static final String BROADCAST_ACTION_CALL_RECEIVED_DISPLAY = "com.smsspeaker.CALL_RECEIVED_DISPLAY";
	
	public static final String PARAM_MSG = "msg_payload";
	public static final String PARAM_CALL = "call_payload";
	
	public static final String DATA_TYPE_SMS = "SMS";
	public static final String DATA_TYPE_CALL = "CALL";

	public static final String RINGER_MODE_DEFAULT = "Default";
	public static final String RINGER_MODE_NORMAL = "Normal";
	public static final String RINGER_MODE_VIBRATE = "Vibrate";
	public static final String RINGER_MODE_SILENT = "Silent";
	

}
