package com.mylotto.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mylotto.MyLottoApplication;
import com.mylotto.data.DamacaiFetcher;
import com.mylotto.data.FetchResults.FetchStatus;
import com.mylotto.data.MagnumFetcher;
import com.mylotto.data.Prefs;
import com.mylotto.data.TotoFetcher;
import com.mylotto.helper.Constants;
import com.mylotto.helper.Constants.SyncStatus;
import com.mylotto.helper.Utils;

/**
 * Background service to check for lotto results.
 * 
 * Note that this is started at BOOT (in which case onCreate and onStart are
 * called), and is bound from within Dashboard Activity in MyLotto application.
 * This Service is started in the background for alert processing (standalone),
 * bound in Activities to call methods on Binder to register alert locations.
 * 
 * @author MEKOH
 * 
 */
public class LottoService extends Service {

	private static final String CLASS_TAG = LottoService.class.getSimpleName();

	// private static final long SERVICE_QUIET_PERIOD = 10000; // Default to 10
	// seconds
	// private static final long SERVICE_POLL_INTERVAL = 60000; // Default to 60
	// seconds

	// private static final String THREAD_GROUP_NAME = "tg";

	// private Timer timer;
	// private DbHelper dbHelper;

	// private NotificationManager nm;
	private Prefs prefs;

	// Broadcast intent to send to the activity
	// private Intent intent;

	protected static SyncStatus syncStatus;

	// Broadcast receiver to receive signal to start synchronization

	/*
	 * private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) { String
	 * serviceMessage = intent .getStringExtra(Constants.SERVICE_MESSAGE);
	 * 
	 * if (Constants.SERVICE_ACTION_START.equals(serviceMessage)) {
	 * 
	 * if (syncStatus != SyncStatus.IN_PROGRESS) { // Start synchronization
	 * handler.removeCallbacks(syncResults); handler.post(syncResults); } } else
	 * if (Constants.SERVICE_ACTION_STOP.equals(serviceMessage)) { // Stop
	 * synchronization
	 * 
	 * } } };
	 */
	/*
	 * private TimerTask task = new TimerTask() {
	 * 
	 * @Override public void run() { downloadData(); } };
	 */

	@Override
	public void onCreate() {
		// nm = (NotificationManager)
		// getSystemService(Context.NOTIFICATION_SERVICE);
		this.prefs = new Prefs(getApplicationContext());
		this.syncStatus = SyncStatus.NOT_STARTED;

		/*
		 * timer = new Timer(); timer.schedule(task, 1000,
		 * LottoService.SERVICE_POLL_INTERVAL);
		 */
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(Constants.LOG_TAG, " " + LottoService.CLASS_TAG + "   onStart");

		// Register the broadcast receiver
		// this.registerReceiver(broadcastReceiver, new IntentFilter(
		// Constants.BROADCAST_FROM_ACTIVITY_ACTION));

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// handler.removeCallbacks(syncResults);
		// unregisterReceiver(broadcastReceiver);
	}

	/**
	 * Download lotto results
	 */
	protected void downloadResults() {

		syncStatus = SyncStatus.IN_PROGRESS;

		if (!Utils.isOnline(this)) {
			// sendMessage(this.getString(R.string.message_network_not_available));
			syncStatus = SyncStatus.FAILED;
			return;
		}
		// dbHelper = new DbHelper(this);
		try {

			// sendMessage(this.getString(R.string.message_synchronizing));

			// Get the currently configured lottos
			// List<Lotto> lottos = new ArrayList<Lotto>(1);
			// lottos = dbHelper.getLottoByCountryId(prefs.getCountryId());

			// for (Lotto lotto : lottos) {
			// Log.d(CLASS_TAG, "lotto name: " + lotto.name);
			// }

			// Getting DAMACAI results
			DamacaiFetcher damacaiFetcher = new DamacaiFetcher("damacai", this, prefs);
			damacaiFetcher.run();

			// Getting MAGNUM results
			MagnumFetcher magnumFetcher = new MagnumFetcher("magnum", this, prefs);
			magnumFetcher.run();

			// Getting SportsToto results
			TotoFetcher totoFetcher = new TotoFetcher("toto", this, prefs);
			totoFetcher.run();

			// dbHelper.cleanUp();
			// dbHelper = null;

			if (damacaiFetcher.getFetchResults().status == FetchStatus.SUCCESSFUL && magnumFetcher.getFetchResults().status == FetchStatus.SUCCESSFUL
					&& totoFetcher.getFetchResults().status == FetchStatus.SUCCESSFUL) {
				syncStatus = SyncStatus.SUCCESS;
				
				MyLottoApplication application = (MyLottoApplication) getApplication();
				application.setSynchronizedFlag(true);
				
				// sendMessage(this.getString(R.string.message_sync_successful));

			} else {
				syncStatus = SyncStatus.FAILED;
				// sendMessage(this.getString(R.string.message_sync_failed));
			}

		} catch (Exception ex) {
			Log.e(CLASS_TAG, "Unable to fetch lotto results [" + ex.getMessage() + "]");
			syncStatus = SyncStatus.FAILED;
			// sendMessage(this.getString(R.string.message_sync_failed));

		}
		/*
		 * finally { if (dbHelper != null) { dbHelper.cleanUp(); dbHelper =
		 * null; } }
		 */
	}

	/**
	 * Send out broadcast message
	 * 
	 * @param message
	 */
	/*
	 * private void sendMessage(final String message) { if (intent == null) {
	 * intent = new Intent(Constants.BROADCAST_FROM_SERVICE_ACTION); }
	 * intent.putExtra(Constants.SERVICE_MESSAGE, message);
	 * sendBroadcast(intent); }
	 */

}
