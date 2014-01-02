package com.mymobkit.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.mymobkit.R;
import com.mymobkit.service.IHttpdService;
import com.mymobkit.app.MyMobKitApp;
import com.mymobkit.common.NetworkHelper;
import com.mymobkit.common.Notifier;
import com.mymobkit.config.AppConfig;
import com.mymobkit.config.AppConfig.MenuListSetup;
import com.mymobkit.model.ActionStatus;
import com.mymobkit.model.Surveillance;
import com.mymobkit.net.ControlPanelService;
import com.mymobkit.net.provider.Processor;
import com.mymobkit.ui.activity.SlidingActivityHolder;
import com.mymobkit.ui.fragment.CameraSettingsFragment;
import com.mymobkit.ui.fragment.ControlPanelFragment;

/**
 * Android HTTPD service.
 * 
 */
public final class HttpdService extends Service {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":HttpdService";

	// Scheduler interval in seconds
	private static final int DEFAULT_MESSAGE_SERVICE_SCHEDULER_INTERVAL = 30;

	private static int ONGOING_NOTIFICATION_ID = 1688;

	protected ControlPanelService httpServer;
	protected boolean hasError;
	protected String status;
	protected String uri;
	protected ScheduledExecutorService messageScheduler;
	protected ScheduledFuture<?> messageSchedulerFuture;

	/**
	 * Remote methods.
	 * 
	 */
	private final class HttpdServiceProvider extends IHttpdService.Stub {

		@Override
		public String getUri() throws RemoteException {
			if (TextUtils.isEmpty(uri)) {
				SharedPreferences sharedPreferences = getSharedPreferences(ControlPanelFragment.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
				int port = Integer.valueOf(sharedPreferences.getString(ControlPanelFragment.KEY_CONTROL_PANEL_PORT, getString(R.string.default_http_port)));
				uri = "http://" + NetworkHelper.getLocalIPAddress() + ":" + port;
			}
			return uri;
		}

		@Override
		public boolean isAlive() throws RemoteException {
			if (httpServer != null && httpServer.isAlive()) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isError() throws RemoteException {
			return hasError;
		}

		@Override
		public String getErrorMsg() throws RemoteException {
			return status;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new HttpdServiceProvider();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "[onDestroy] Stopping HTTP service");
		if (httpServer != null && httpServer.isAlive()) {
			httpServer.stop();
			httpServer = null;
		}

		// Stop messaging service
		stopMessagingService();

		// Stop service and remove notification
		stopForeground(true);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.i(TAG, "[onStartCommand] Starting myMobKit server");

		if (httpServer != null && httpServer.isAlive())
			return Service.START_NOT_STICKY;

		// Get HTTP listening port from the passed in intent
		int port = intent.getIntExtra(AppConfig.HTTP_LISTENING_PORT_PARAM, Integer.parseInt(MyMobKitApp.getContext().getString(com.mymobkit.R.string.default_http_port)));

		// Instantiate the HTTP server
		httpServer = new ControlPanelService(null, port, MyMobKitApp.getContext().getAssets(), true);
		try {

			// Register the services
			httpServer.registerService("/services", serviceHelp);
			httpServer.registerService("/services/", serviceHelp);
			httpServer.registerService("/services/surveillance/url", surveillanceUrl);
			httpServer.registerService("/services/surveillance/start", startSurveillance);
			httpServer.registerService("/services/message/send", messageSender);
			httpServer.registerService("/services/message/receive", messageReceiver);
			httpServer.registerService("/services/message/status", messageStatusChecker);

			httpServer.start();
			this.uri = "http://" + NetworkHelper.getLocalIPAddress() + ":" + port;
			Log.i(TAG, "[onStartCommand] HTTP server is started - " + uri);
			status = String.format(MyMobKitApp.getContext().getString(R.string.notif_http_service), uri);

			// start messaging service
			startMessagingService();

			hasError = false;
		} catch (IOException ex) {
			Log.e(TAG, "[onStartCommand] Error starting HTTPD service", ex);
			status = ex.getMessage();
			hasError = true;
		}
		Notification notification = Notifier.buildNotification(MyMobKitApp.getContext(), status);
		startForeground(ONGOING_NOTIFICATION_ID, notification);
		return Service.START_NOT_STICKY;
	}

	private Processor<Map<String, String>, Map<String, String>, String> startSurveillance = new Processor<Map<String, String>, Map<String, String>, String>() {

		@Override
		public String process(Map<String, String> header, Map<String, String> params) {
			boolean mode = MyMobKitApp.getSurveillanceMode();
			if (mode) {
				Log.i(TAG, "[process] Already in surveillance mode");
				return ActionStatus.OK;
			}

			try {
				Log.i(TAG, "[process] Starting surveillance mode");
				Intent dialogIntent = new Intent(getBaseContext(), SlidingActivityHolder.class);
				dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				dialogIntent.putExtra(AppConfig.FUNCTION_PARAM, MenuListSetup.VIDEO_STREAMING.getCode());
				getApplication().startActivity(dialogIntent);
				
				// Check is in surveillance mode
				int counter = 0;
				while (true) {
					Thread.sleep(1000);
					mode = MyMobKitApp.getSurveillanceMode();
					if (mode || counter == 13) 
						break;
					else 
						counter++;
				}
			} catch (Exception e) {
				Log.e(TAG, "[process] Failed to start surveillance mode", e);
				return ActionStatus.ERROR;
			}
			return ActionStatus.OK;
		}

	};

	private Processor<Map<String, String>, Map<String, String>, String> surveillanceUrl = new Processor<Map<String, String>, Map<String, String>, String>() {

		@Override
		public String process(Map<String, String> header, Map<String, String> params) {
			SharedPreferences prefs = MyMobKitApp.getContext().getSharedPreferences(CameraSettingsFragment.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
			String port = prefs.getString(CameraSettingsFragment.KEY_VIDEO_STREAMING_PORT, MyMobKitApp.getContext().getString(R.string.default_video_streaming_port));
			String url = "http://" + NetworkHelper.getLocalIPAddress() + ":" + port;
			Surveillance s = new Surveillance();
			s.setUrl(url);
			return new Gson().toJson(s);
		}

	};

	private Processor<Map<String, String>, Map<String, String>, String> serviceHelp = new Processor<Map<String, String>, Map<String, String>, String>() {

		@Override
		public String process(Map<String, String> header, Map<String, String> params) {
			return "service help";
		}

	};

	private Processor<Map<String, String>, Map<String, String>, String> messageSender = new Processor<Map<String, String>, Map<String, String>, String>() {

		@Override
		public String process(Map<String, String> header, Map<String, String> params) {
			return "message sender";
		}

	};

	private Processor<Map<String, String>, Map<String, String>, String> messageReceiver = new Processor<Map<String, String>, Map<String, String>, String>() {

		@Override
		public String process(Map<String, String> header, Map<String, String> params) {
			return "";
		}

	};

	private Processor<Map<String, String>, Map<String, String>, String> messageStatusChecker = new Processor<Map<String, String>, Map<String, String>, String>() {

		@Override
		public String process(Map<String, String> header, Map<String, String> params) {
			return "";
		}

	};

	private void startMessagingService() {
		messageScheduler = Executors.newSingleThreadScheduledExecutor();
		messageSchedulerFuture = messageScheduler.scheduleWithFixedDelay(
				new Runnable() {
					public void run() {
						// Log.w(TAG, "Message scheduler runs");
					}
				}
				, 0, DEFAULT_MESSAGE_SERVICE_SCHEDULER_INTERVAL, TimeUnit.SECONDS);
	}

	private void stopMessagingService() {
		if (messageScheduler != null) {
			messageScheduler.shutdown();
		}
	}
}
