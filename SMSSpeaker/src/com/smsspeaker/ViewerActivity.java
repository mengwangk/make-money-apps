package com.smsspeaker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import com.smsspeaker.helper.DbHelper;

public class ViewerActivity extends Activity implements OnClickListener {

	private static final String CLASS_TAG = ViewerActivity.class.getSimpleName();

	private ArrayAdapter<InboundData> arrayAdapter;
	private List<InboundData> receivedMessages = new ArrayList<InboundData>(3);
	private List<InboundData> historyData;

	private static final int MENU_DELETE = Menu.FIRST;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		arrayAdapter = new ArrayAdapter<InboundData>(this, R.layout.viewer, receivedMessages) {
			private LayoutInflater inflater;
			{
				inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				InboundData m = getItem(position);
				TwoLineListItem view = (TwoLineListItem) inflater.inflate(R.layout.viewer, null);
				TextView text1 = view.getText1();
				if (m.isNew) {
					text1.setTextColor(Color.GREEN);
				}
				text1.setText(m.subject);

				TextView text2 = view.getText2();
				if (m.isNew) {
					text2.setTextColor(Color.GREEN);
				}
				text2.setText(m.details + "\r\n\r\n" + m.timestamp.toLocaleString());

				return view;
			}

		};

		setLayout();
		loadHistory();
	}
	
	private void setLayout(){
		ListView listView = new ListView(this);
		listView.setAdapter(arrayAdapter);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.addView(listView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		setContentView(linearLayout);
	}

	private BroadcastReceiver smsBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			InboundData m = (InboundData) intent.getSerializableExtra(Constants.PARAM_MSG);
			displayData(m);
		}
	};
	
	private BroadcastReceiver callBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			InboundData m = (InboundData) intent.getSerializableExtra(Constants.PARAM_CALL);
			displayData(m);
		}
	};
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(smsBroadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION_SMS_RECEIVED_DISPLAY));
		registerReceiver(callBroadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION_CALL_RECEIVED_DISPLAY));
	}
	
	@Override
	protected void onDestroy() {

		try {
			unregisterReceiver(smsBroadcastReceiver);
		} catch (Exception ex) {
		}
		
		try {
			unregisterReceiver(callBroadcastReceiver);
		} catch (Exception ex) {
		}

		super.onDestroy();
	}


	@Override
	public void onClick(View src) {

	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.msg_confirm_exit)).setCancelable(false)
				.setPositiveButton(getString(R.string.label_yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						ViewerActivity.this.finish();
					}
				}).setNegativeButton(getString(R.string.label_no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_DELETE, 0, R.string.menu_delete_all).setIcon(R.drawable.ic_menu_delete);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DELETE:
			deleteAllInboundData();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void deleteAllInboundData() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.msg_confirm_delete)).setCancelable(false).setPositiveButton(getString(R.string.label_yes), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				DbHelper dbHelper = new DbHelper(ViewerActivity.this);
				try {
					receivedMessages.clear();
					arrayAdapter.notifyDataSetChanged();
					
					dbHelper.deleteAllInboundData();
					dbHelper.cleanUp();
					dbHelper = null;
				} catch (Exception ex) {
					Log.e(CLASS_TAG, "Problem deleting data [" + ex.getMessage() + "]");
				} finally {
					if (dbHelper != null) {
						dbHelper.cleanUp();
						dbHelper = null;
					}
				}
			}
		}).setNegativeButton(getString(R.string.label_no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Display the message in a separate thread.
	 * 
	 * @param intent
	 *            Intent containing the message.
	 */
	private void displayData(InboundData m ) {
		receivedMessages.add(0, m);
		arrayAdapter.notifyDataSetChanged();
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			if (historyData == null || historyData.size() == 0) {
				return;
			} else {
				receivedMessages.clear();
				// Display all historical data
				for (InboundData data : historyData) {
					receivedMessages.add(0, data);
				}
				arrayAdapter.notifyDataSetChanged();
			}
		}
	};

	private void loadHistory() {
		new Thread() {
			@Override
			public void run() {
				DbHelper dbHelper = new DbHelper(ViewerActivity.this);
				try {
					historyData = dbHelper.getAllInboundData();
					dbHelper.cleanUp();
					dbHelper = null;
					handler.sendEmptyMessage(0);
				} catch (Exception ex) {
					Log.e(CLASS_TAG, "Problem inserting information into database [" + ex.getMessage() + "]");
				} finally {
					if (dbHelper != null) {
						dbHelper.cleanUp();
						dbHelper = null;
					}
				}
			}
		}.start();
	}
}
