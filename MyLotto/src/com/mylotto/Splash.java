package com.mylotto;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.mylotto.helper.Constants;
import com.mylotto.service.ILottoMessenger;

/**
 * Splash screen
 * 
 * @author MEKOH
 *
 */
public final class Splash extends BaseActivity {

	private static final String CLASS_TAG = Splash.class.getSimpleName();
	
	private ILottoMessenger lottoMessenger;
	private boolean bound = false;

	
	//private ProgressBar progressBar;

	/* 
	 * Called when the activity is created.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// show the screen (our splash image)
		setContentView(R.layout.splash);

		showOptionsMenu = false;

		//progressBar = (ProgressBar) findViewById(R.id.loadingprogress);

		// Start the service
		startService();

		// setup handler to close the splash screen
		Handler x = new Handler();
		x.postDelayed(new Splashhandler(), Constants.SPLASH_INTERVAL_MILLISECONDS);

		doBindService();
		
		// Start lengthy operation in a background thread
		/*
		new Thread(new Runnable() {
		    public void run() {
		    	int progressStatus = 0;
		        while (progressStatus < 100) {
		           progressBar.setProgress(progressStatus);
		        }
		    }
		}).start();
		*/

	}

	protected ServiceConnection serviceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			lottoMessenger = ILottoMessenger.Stub.asInterface(binder);
			bound = true;

		}

		public void onServiceDisconnected(ComponentName className) {
			lottoMessenger = null;
			bound = false;
		}
	};

	protected void doBindService() {
		if (!this.bound) {
			bindService(new Intent(ILottoMessenger.class.getName()),
					this.serviceConnection, Context.BIND_AUTO_CREATE);
		}
	}
	
	/**
	 * Check if service connection is available. If not, then wait for maximum 5 seconds
	 * @return
	 */
	protected boolean isServiceConnectionAvailable(){
		if (this.bound) return true;
		int waitCount = 0;
		while (!this.bound) {
			try {
				waitCount++;
				Thread.sleep(500);
				if (waitCount == 10) {
					return false;
				}
			} catch (Exception ex) {
				Log.d(CLASS_TAG, "Service handler thread is aborted");
			}
		}
		return true;
	}
	
	class Splashhandler implements Runnable {

		public void run() {

			TextView status = (TextView) findViewById(R.id.loading);

			// Check if the service is successfully started
			while (!isServiceRunning(Constants.SERVICE_CLASS_NAME)) {
				try {
					status.setText(Splash.this.getString(R.string.service_starting));
					Thread.sleep(500);
				} catch (Exception ex) {
					Log.e(CLASS_TAG, "Unable to start service - " + ex.getMessage());
				}
			}

			if (isServiceConnectionAvailable()) {
				MyLottoApplication application = (MyLottoApplication)getApplication();
				application.setLottoMessenger(lottoMessenger);
				status.setText(Splash.this.getString(R.string.service_started));
				// start new activity 
				startActivity(new Intent(getApplication(), Dashboard.class));
				// close out this activity
				finish();
			} else {
				status.setText(Splash.this.getString(R.string.service_start_failed));
			}
		}
	}

}