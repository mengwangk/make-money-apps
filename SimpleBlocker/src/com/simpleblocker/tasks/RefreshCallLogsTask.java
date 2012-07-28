package com.simpleblocker.tasks;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.simpleblocker.SimpleBlockerApp;
import com.simpleblocker.data.DbHelper;
import com.simpleblocker.data.models.BlockedCallLog;
import com.simpleblocker.ui.dialogs.CustomProgressDialog;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;

public final class RefreshCallLogsTask extends Thread {

	private ArrayAdapter<BlockedCallLog> arrayAdapter = null;
	private CustomProgressDialog customDialog;
	private Handler handler;

	// ------------- Getters & Setters ------------------------------//
	public ArrayAdapter<BlockedCallLog> getArrayAdapter() {
		return arrayAdapter;
	}

	public void setArrayAdapter(ArrayAdapter<BlockedCallLog> arrayAdapter) {
		this.arrayAdapter = arrayAdapter;
	}

	// ------------------------------------------------------------------------//

	/**
	 * Constructor with the basic parameter
	 * 
	 * @param arrayAdapter
	 * @param customDialog
	 * @param handler
	 */
	public RefreshCallLogsTask(ArrayAdapter<BlockedCallLog> arrayAdapter, CustomProgressDialog callLogDialog, Handler handler) {

		this.arrayAdapter = arrayAdapter;
		this.customDialog = callLogDialog;
		this.handler = handler;
	}

	public void run() {

		refreshAll();
	}

	/**
	 * Remove all callLog objects from the ddbb and the list
	 */
	private void refreshAll() {

		ArrayList<BlockedCallLog> callLogs = new ArrayList<BlockedCallLog>();
		
		DbHelper dbHelper = new DbHelper(SimpleBlockerApp.getContext());
		try {
			callLogs = (ArrayList<BlockedCallLog>)dbHelper.getBlockedCallLogs();
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		} finally {
			
			if (dbHelper != null) {
				dbHelper.cleanUp();
				dbHelper = null;
			}
			
			// Hide and dismiss the synchronization dialog
			customDialog.stopDialog(SimpleBlockerApp.getContext());

			// Create and send the numBanned message to the handler in gui main
			// thread
			Message message = handler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putSerializable(AppConfig.PARAM_CALL_LOGS, callLogs);
			message.setData(bundle);
			handler.sendMessage(message);
		}

	}

}
