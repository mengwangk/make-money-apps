package com.mylotto.helper;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Keep track of application constants.
 * 
 * @author MEKOH
 * 
 */
public final class Constants {

	/**
	 * Splash screen show up interval
	 */
	public final static int SPLASH_INTERVAL_MILLISECONDS = 1000;

	/**
	 * LogCat tag
	 */
	public final static String LOG_TAG = "MyLotto";

	/**
	 * Default to Malaysia/Singapore
	 */
	public final static String DEFAULT_COUNTRY_ID = "1";

	/**
	 * Default number of draws to store
	 */
	public final static String DEFAULT_NO_OF_DRAW = "100";

	/**
	 * Image resources
	 */
	public static final Map<String, Integer> IMAGE_RESOURCES;

	/**
	 * Date format used in the application
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * Service class name
	 */
	public static final String SERVICE_CLASS_NAME = "com.mylotto.service.LottoService";

	/**
	 * Date formatter
	 */
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

	/**
	 * Results screen
	 */
	public static final String INTENT_ACTION_VIEW_RESULTS = "com.mylotto.VIEW_RESULTS";

	
	/**
	 * Number analysis results screen
	 */
	public static final String INTENT_ACTION_VIEW_NUMBER_ANALYSIS_RESULTS = "com.mylotto.VIEW_NUMBER_ANALYSIS_RESULTS";
	
	
	/**
	 * Frequency prediction results screen
	 */
	public static final String INTENT_ACTION_VIEW_FREQUENCY_PREDICTION_RESULTS = "com.mylotto.VIEW_FREQUENCY_PREDICTION_RESULTS";
	
	

	/**
	 * Broadcast status from background service
	 */
	//public static final String BROADCAST_FROM_SERVICE_ACTION = "com.mylotto.service.progress";

	/**
	 * Broadcast status from activity
	 */
	public static final String BROADCAST_FROM_ACTIVITY_ACTION = "com.mylotto.activity.progress";

	/**
	 * Progress of the background service
	 */
	//public static final String SERVICE_MESSAGE = "service_message";

	/**
	 * Message from activity to start the Lotto service
	 */
	//public static final String SERVICE_ACTION_START = "start service";

	/**
	 * Message from activity to stop the Lotto service
	 */
	//public static final String SERVICE_ACTION_STOP = "stop service";

	/**
	 * Synchronization status
	 *
	 */
	public enum SyncStatus {
		NOT_STARTED, IN_PROGRESS, FAILED, SUCCESS
	}

	

	/**
	 * Label for synchronization status code
	 */
	public static final Map<Integer, Integer> SYNC_STATUS_DESCRIPTION;
	
	
	// Static initializer
	static {
		
		// Image resources lookup
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		aMap.put("toto", com.mylotto.R.drawable.toto);
		aMap.put("magnum", com.mylotto.R.drawable.magnum);
		aMap.put("damacai", com.mylotto.R.drawable.damacai);
		IMAGE_RESOURCES = Collections.unmodifiableMap(aMap);
		
		// Sync status lookup
		Map<Integer, Integer> syncStatuses = new HashMap<Integer, Integer>();
		syncStatuses.put(SyncStatus.IN_PROGRESS.ordinal(), com.mylotto.R.string.status_sync_in_progress);
		syncStatuses.put(SyncStatus.NOT_STARTED.ordinal(), com.mylotto.R.string.status_sync_not_start);
		syncStatuses.put(SyncStatus.FAILED.ordinal(), com.mylotto.R.string.status_sync_failure);
		syncStatuses.put(SyncStatus.SUCCESS.ordinal(), com.mylotto.R.string.status_sync_successful);
		SYNC_STATUS_DESCRIPTION = Collections.unmodifiableMap(syncStatuses);
	}

}
