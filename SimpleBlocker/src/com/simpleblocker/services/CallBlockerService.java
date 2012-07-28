package com.simpleblocker.services;

import com.simpleblocker.operations.IncomingCallOperations;
import com.simpleblocker.utils.AppConfig;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public final class CallBlockerService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		String incomingCallNumber = intent.getExtras().getString(AppConfig.PARAM_INCOMING_CALL_NUMBER);
		IncomingCallOperations incomingCallOperations = new IncomingCallOperations(incomingCallNumber);

		// If i use threads
		incomingCallOperations.start();

		// Stop the service by self way
		stopSelf();

		// before i used start_sticky
		return Service.START_NOT_STICKY;
	}
}
