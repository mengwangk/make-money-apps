package com.mymobkit.upnp;

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.model.ValidationException;
import org.teleal.cling.registry.RegistrationException;

import com.mymobkit.config.AppConfig;

import android.content.ComponentName;
import android.os.IBinder;
import android.util.Log;

public class UpnpServiceConnection implements android.content.ServiceConnection {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":UpnpServiceConnection";

	
	private String webService = null;
	
	public UpnpServiceConnection(String webService) {
		this.webService = webService;
	}
	
	public void onServiceConnected(ComponentName name, IBinder service) {
		try {
			((AndroidUpnpService)service).getRegistry().addDevice(ProxyDevice.createDevice(webService));
		} catch (RegistrationException e) {
			Log.e(TAG, "[onServiceConnected] Error adding UPnP device: ", e);
		} catch (ValidationException e) {
			Log.e(TAG, "[onServiceConnected] Error adding UPnP device: ", e);
		}
	}

	public void onServiceDisconnected(ComponentName name) {
	}
}
