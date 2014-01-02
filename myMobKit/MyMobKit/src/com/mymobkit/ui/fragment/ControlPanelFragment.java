package com.mymobkit.ui.fragment;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.util.Log;

import com.mymobkit.R;
import com.mymobkit.service.IHttpdService;
import com.mymobkit.app.MyMobKitApp;
import com.mymobkit.config.AppConfig;
import com.mymobkit.service.HttpdService;
import com.mymobkit.ui.base.PreferenceListFragment;
import com.mymobkit.ui.base.PreferenceListFragment.OnPreferenceAttachedListener;

/**
 * Web control panel fragment.
 * 
 */
public final class ControlPanelFragment extends PreferenceListFragment implements OnSharedPreferenceChangeListener, OnPreferenceAttachedListener {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":ControlPanelFragment";

	public static final String SHARED_PREFS_NAME = "control_panel_settings";

	public static final String KEY_CONTROL_PANEL_LOGIN_REQUIRED = "preferences_control_panel_login_required";
	public static final String KEY_CONTROL_PANEL_USER_NAME = "preferences_control_panel_user_name";
	public static final String KEY_CONTROL_PANEL_USER_PASSWORD = "preferences_control_panel_user_password";
	public static final String KEY_CONTROL_PANEL_PORT = "preferences_control_panel_port";
	public static final String KEY_CONTROL_PANEL_STATUS = "preferences_control_panel_status";
	public static final String KEY_CONTROL_PANEL_URL = "preferences_control_panel_url";
	public static final String KEY_CONTROL_PANEL_AUTO_START = "preferences_control_panel_auto_start";
	public static final String KEY_CONTROL_PANEL_IN_SURVEILLANCE_MODE = "preferences_control_panel_in_surveillance_mode";

	// Interface to the service
	private IHttpdService httpdServiceProvider;

	private CheckBoxPreference loginRequired;
	private EditTextPreference userName;
	private EditTextPreference userPassword;
	private EditTextPreference port;
	private CheckBoxPreference status;
	private EditTextPreference url;
	private CheckBoxPreference autoStart;

	protected static final int HTTPD_SERVICE_CONNECTED = 100;
	protected static final int HTTPD_SERVICE_DISCONNECTED = 200;

	protected Handler statusHandler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			Log.d(TAG, "[handleMessage] Received service status");
			switch (msg.what) {
			case HTTPD_SERVICE_CONNECTED:
				try {
					if (httpdServiceProvider != null) {
						if (!httpdServiceProvider.isError()) { // && httpdServiceProvider.isAlive()) {
							String uri = httpdServiceProvider.getUri();
							url.setText(uri);
						} else {
							url.setText(httpdServiceProvider.getErrorMsg());
						}
					}
				} catch (Exception ex) {
					Log.e(TAG, "[handleMessage] Error retrieving service status", ex);
				}
				break;
			case HTTPD_SERVICE_DISCONNECTED:
				url.setText(" ");
				break;
			default:
				break;
			}
		}
	};

	public static ControlPanelFragment newInstance() {
		return new ControlPanelFragment();
	}

	public ControlPanelFragment() {
		super(R.xml.pref_control_panel);
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		PreferenceManager preferenceManager = getPreferenceManager();
		preferenceManager.setSharedPreferencesName(SHARED_PREFS_NAME);

		PreferenceScreen preferences = getPreferenceScreen();
		loginRequired = (CheckBoxPreference) preferences.findPreference(KEY_CONTROL_PANEL_LOGIN_REQUIRED);
		userName = (EditTextPreference) preferences.findPreference(KEY_CONTROL_PANEL_USER_NAME);
		userPassword = (EditTextPreference) preferences.findPreference(KEY_CONTROL_PANEL_USER_PASSWORD);
		port = (EditTextPreference) preferences.findPreference(KEY_CONTROL_PANEL_PORT);
		status = (CheckBoxPreference) preferences.findPreference(KEY_CONTROL_PANEL_STATUS);
		url = (EditTextPreference) preferences.findPreference(KEY_CONTROL_PANEL_URL);
		autoStart = (CheckBoxPreference) preferences.findPreference(KEY_CONTROL_PANEL_AUTO_START);

		loginRequired.setChecked(preferenceManager.getSharedPreferences().getBoolean(KEY_CONTROL_PANEL_LOGIN_REQUIRED, Boolean.valueOf(this.getString(R.string.default_login_required))));
		userName.setText(preferenceManager.getSharedPreferences().getString(KEY_CONTROL_PANEL_USER_NAME, this.getString(R.string.default_http_user_name)));
		userPassword.setText(preferenceManager.getSharedPreferences().getString(KEY_CONTROL_PANEL_USER_PASSWORD, this.getString(R.string.default_http_user_password)));
		port.setText(preferenceManager.getSharedPreferences().getString(KEY_CONTROL_PANEL_PORT, this.getString(R.string.default_http_port)));

		boolean isServiceRunning = isServiceRunning();
		status.setChecked(preferenceManager.getSharedPreferences().getBoolean(KEY_CONTROL_PANEL_STATUS, isServiceRunning));
		if (isServiceRunning) {
			bindService();
		}

		url.setText(preferenceManager.getSharedPreferences().getString(KEY_CONTROL_PANEL_URL, ""));
		autoStart.setChecked(preferenceManager.getSharedPreferences().getBoolean(KEY_CONTROL_PANEL_AUTO_START, Boolean.valueOf(this.getString(R.string.default_auto_start_service))));

		PreferenceManager.setDefaultValues(MyMobKitApp.getContext(), R.xml.pref_control_panel, false);
		for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
			initSummary(getPreferenceScreen().getPreference(i));
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(TAG, "[onSharedPreferenceChanged] Key - " + key);
		if (KEY_CONTROL_PANEL_STATUS.equals(key)) {
			if (status.isChecked()) {

				// Create the service intent
				Intent intent = new Intent(MyMobKitApp.getContext(), HttpdService.class);
				intent.putExtra(AppConfig.HTTP_LISTENING_PORT_PARAM, Integer.parseInt(port.getText()));

				// Start the service
				ComponentName cn = getActivity().startService(intent);
				if (cn != null) {

					// Bind to the service
					bindService();

					// Show the service status
					// showStatus(); // Changed to use handler
				} else {
					Log.w(TAG, "[onSharedPreferenceChanged] Unable to start service");
				}
			} else {
				// Unbind the service
				unBindService();

				// Stop the service
				boolean isStopped = getActivity().stopService(new Intent(MyMobKitApp.getContext(), HttpdService.class));
				if (!isStopped) {
					Log.w(TAG, "[onSharedPreferenceChanged] Unable to stop service");
				}

				// Reset the URL
				url.setText(" ");
			}
		}
		updatePrefSummary(findPreference(key));
	}

	@Override
	public void onPause() {
		super.onPause();

		// Unregister listener
		getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		// Register listener
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unBindService();
	}

	@Override
	public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
		if (root == null)
			return;
	}

	private void initSummary(Preference p) {
		if (p instanceof PreferenceCategory) {
			PreferenceCategory pCat = (PreferenceCategory) p;
			for (int i = 0; i < pCat.getPreferenceCount(); i++) {
				initSummary(pCat.getPreference(i));
			}
		} else {
			updatePrefSummary(p);
		}
	}

	private void updatePrefSummary(Preference p) {
		if (p instanceof ListPreference) {
			ListPreference listPref = (ListPreference) p;
			p.setSummary(listPref.getEntry());
		}
		if (p instanceof EditTextPreference) {
			EditTextPreference editTextPref = (EditTextPreference) p;
			p.setSummary(editTextPref.getText());
		}
	}

	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (HttpdService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/*private void showStatus() {

		Log.v(TAG, "[showStatus] Create a new thread to show the status");

		new Thread() {
			@Override
			public void run() {
				try {
					// Wait for the connection to be available
					if (httpdServiceProvider == null)
						sleep(3000);

					if (httpdServiceProvider != null) {
						if (!httpdServiceProvider.isError() && httpdServiceProvider.isAlive()) {
							url.setText(httpdServiceProvider.getUri());
						} else {
							url.setText(httpdServiceProvider.getErrorMsg());
						}
					}
				} catch (Exception ex) {
					Log.e(TAG, "[showStatus] Error retrieving service status", ex);
				}
			}
		}.start();

	}*/

	protected void bindService() {
		if (httpdServiceProvider == null) {
			getActivity().bindService(new Intent(IHttpdService.class.getName()), this.serviceConnection, Context.BIND_AUTO_CREATE);
		}
	}

	protected void unBindService() {
		if (httpdServiceProvider != null) {
			getActivity().unbindService(serviceConnection);
			httpdServiceProvider = null;
		}
	}

	protected ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			httpdServiceProvider = IHttpdService.Stub.asInterface(binder);
			statusHandler.sendEmptyMessage(HTTPD_SERVICE_CONNECTED);
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			httpdServiceProvider = null;
			statusHandler.sendEmptyMessage(HTTPD_SERVICE_DISCONNECTED);
		}

	};
}
