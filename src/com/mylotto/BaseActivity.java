package com.mylotto;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.mylotto.helper.StringUtils;
import com.mylotto.service.LottoService;

/**
 * Base class for all activities
 * 
 * @author MEKOH
 *
 */
public abstract class BaseActivity extends Activity {

	private static final String CLASS_TAG = BaseActivity.class.getSimpleName();

	private static final int MENU_BACK = Menu.FIRST;
	private static final int MENU_EXIT = Menu.FIRST + 1;

	protected boolean showOptionsMenu = false;

	// Broadcast intent to send to the activity
	//private Intent intent;

	// Broadcast receiver
	/*
	protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	String serviceMessage = intent.getStringExtra(Constants.SERVICE_MESSAGE);
	        	Log.d(CLASS_TAG, "Received message - " + serviceMessage);
	        	
	        	TextView statusText = (TextView)findViewById(R.id.syncstatus);
	        	if (statusText != null){
	        		statusText.setText(serviceMessage);
	        	}
	        	    
	        }
	    };   
	    
	*/

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case MENU_BACK:
			finish();
			return true;
		case MENU_EXIT:
			intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			stopService();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_BACK, 0, R.string.menu_back).setIcon(R.drawable.back);
		menu.add(0, MENU_EXIT, 0, R.string.menu_exit).setIcon(R.drawable.exit);

		// Keep the options menu visible
		/*
		new Handler().postDelayed(new Runnable() {
			public void run() {
				openOptionsMenu();
			}
		}, 500);
		*/
		return true;
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (showOptionsMenu)
			openOptionsMenu();
	}

	/**
	 * Check if a particular service is running
	 *
	 * @param serviceClassName Service class name
	 * @return true if running, otherwise false
	 */
	protected boolean isServiceRunning(final String serviceClassName) {
		if (StringUtils.isNullorEmpty(serviceClassName))
			return false;
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClassName.equalsIgnoreCase(service.service
					.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Start the lotto service
	 */
	protected void startService() {
		//if (!isServiceRunning(Constants.SERVICE_CLASS_NAME))
		this.startService(new Intent(this, LottoService.class));
	}

	/**
	 * Stop the lotto service
	 */
	protected void stopService() {
		//if (isServiceRunning(Constants.SERVICE_CLASS_NAME))
		this.stopService(new Intent(this, LottoService.class));
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		/*
		if (this.bound) {
			bound = false;
			unbindService(serviceConnection);
		}
		*/
		//unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_FROM_SERVICE_ACTION));
	}

	/**
	 * Send out broadcast message
	 * 
	 * @param message
	 */
	/*
	protected void sendMessage(final String message){
		
		new Thread() {
			@Override
			public void run() {
				try {
					if (intent == null) {
						intent = new Intent(Constants.BROADCAST_FROM_ACTIVITY_ACTION);
					}
					intent.putExtra(Constants.SERVICE_MESSAGE, message);
					sendBroadcast(intent);
				} catch (Exception ex) {
					Log.e(CLASS_TAG, "Failed to send broadcast message from activity: "
							+ ex.getMessage());
				}
			}
		}.start();
	}
	*/

}
