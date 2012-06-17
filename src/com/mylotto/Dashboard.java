package com.mylotto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mylotto.data.Prefs;
import com.mylotto.helper.Constants;
import com.mylotto.service.ILottoMessenger;

/**
 * Main dashboard screen.
 * 
 * @author MEKOH
 * 
 */
public final class Dashboard extends BaseActivity {

	private static final String CLASS_TAG = Dashboard.class.getSimpleName();

	private GridView gridView;

	/**
	private final Integer[] imageResources = { R.drawable.mybet, R.drawable.results, R.drawable.analysis, R.drawable.settings, R.drawable.live, R.drawable.exit, };
	private final Integer[] textResources = { R.string.mybet, R.string.lotto, R.string.analysis, R.string.settings, R.string.live_results, R.string.exit, };
	private final String[] intents = { "com.mylotto.VIEW_MYBET", "com.mylotto.VIEW_LOTTO", "com.mylotto.VIEW_ANALYSIS",  "com.mylotto.VIEW_SETTINGS", "com.mylotto.VIEW_LIVE_RESULTS" };
	***/
	
	private final Integer[] imageResources = { R.drawable.results, R.drawable.analysis, R.drawable.settings, R.drawable.live, R.drawable.exit, };
	private final Integer[] textResources = { R.string.lotto, R.string.analysis, R.string.settings, R.string.live_results, R.string.exit, };
	private final String[] intents = { "com.mylotto.VIEW_LOTTO", "com.mylotto.VIEW_ANALYSIS",  "com.mylotto.VIEW_SETTINGS", "com.mylotto.VIEW_LIVE_RESULTS" };

	
	private Prefs myprefs = null;

	private final Handler serviceHandler = new Handler();
	private final ServiceSyncher serviceSyncher = new ServiceSyncher();

	private final Handler progressHandler = new Handler();
	private final ProgressChecker progressChecker = new ProgressChecker();

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		// get our application prefs handle
		this.myprefs = new Prefs(this);

		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(new ImageAdapter(this));
		gridView.setOnItemClickListener(itemListener);
	}

	@Override
	protected void onStart() {
		super.onStart();

		serviceHandler.removeCallbacks(serviceSyncher);
		serviceHandler.post(serviceSyncher);

		progressHandler.removeCallbacks(progressChecker);
		progressHandler.postDelayed(progressChecker, 1000);

		// Send a broadcast message to start downloading the results
		// sendMessage(Constants.SERVICE_ACTION_START);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		serviceHandler.removeCallbacks(serviceSyncher);
		progressHandler.removeCallbacks(progressChecker);
	}

	public OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
			if (position == imageResources.length - 1) {
				finish();
			} else {
				Intent intent = new Intent(intents[position]);
				startActivity(intent);
			}
		}

	};

	@Override
	protected void onResume() {
		super.onResume();
		// Refresh user information
		RefreshUserInfo();
	}

	/**
	 * Refresh user information
	 */
	private void RefreshUserInfo() {
		try {
			final TextView userName = (TextView) findViewById(R.id.username);
			userName.setText(this.getString(R.string.user) + ": " + this.myprefs.getUserName());
		} catch (Exception ex) {
			Log.e(CLASS_TAG, "Unable to refresh user info [" + ex.getMessage() + "]");
		}
	}

	/**
	 * Image adapter for the grid view.
	 * 
	 * @author MEKOH
	 * 
	 */
	public class ImageAdapter extends BaseAdapter {

		private Context context;

		public ImageAdapter(final Context c) {
			context = c;
		}

		@Override
		public int getCount() {
			return imageResources.length;
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			View v;
			if (convertView == null) {
				LayoutInflater li = getLayoutInflater();
				v = li.inflate(R.layout.icon, null);
				TextView tv = (TextView) v.findViewById(R.id.icontext);
				tv.setText(textResources[position]);
				ImageView iv = (ImageView) v.findViewById(R.id.iconimage);
				iv.setImageResource(imageResources[position]);
			} else {
				v = convertView;
			}
			return v;
		}

		@Override
		public Object getItem(final int arg) {
			return null;
		}

		@Override
		public long getItemId(final int arg) {
			return 0;
		}
	}

	/**
	 * Runnable to start the synchronization process.
	 * 
	 * @author MEKOH
	 * 
	 */
	class ServiceSyncher implements Runnable {

		/**
		 * Invoke the service API to start synchronizing results.
		 */
		public void run() {

			// Call the service to start synchronization
			ILottoMessenger lottoMessenger = ((MyLottoApplication) getApplication()).getLottoMessenger();
			try {
				if (lottoMessenger.startSynch()) {

				} else {

				}
			} catch (Exception ex) {
				Log.e(CLASS_TAG, "Unable to synchronize results - " + ex.getMessage());
			}
		}
	}

	/**
	 * Runnable to check the synchronization progress.
	 * 
	 * @author MEKOH
	 * 
	 */
	class ProgressChecker implements Runnable {

		/**
		 * Invoke the service API to check synchronization status.
		 */
		public void run() {
			TextView syncStatus = (TextView) findViewById(R.id.syncstatus);

			// Call the service to start synchronization
			ILottoMessenger lottoMessenger = ((MyLottoApplication) getApplication()).getLottoMessenger();
			try {
				int statusCode = lottoMessenger.getStatus();
				if (Constants.SYNC_STATUS_DESCRIPTION.containsKey(statusCode)) {
					String statusDesc = getString(Constants.SYNC_STATUS_DESCRIPTION.get(statusCode));
					syncStatus.setText(statusDesc);
				}
			} catch (Exception ex) {
				Log.e(CLASS_TAG, "Unable to synchronize results - " + ex.getMessage());
			}

			progressHandler.postDelayed(this, 3000); // 3 seconds

		}
	}
}
