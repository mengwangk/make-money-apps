package com.simpleblocker.tasks;

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

public final class RemoveCallLogsTask extends Thread {

	private ArrayAdapter<BlockedCallLog> arrayAdapter = null;
	private CustomProgressDialog removeCallLogsDialog;
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
	 * @param removeCallLogsDialog
	 * @param handler
	 */
	public RemoveCallLogsTask(final ArrayAdapter<BlockedCallLog> arrayAdapter, final CustomProgressDialog removeCallLogsDialog, final Handler handler) {
		this.arrayAdapter = arrayAdapter;
		this.removeCallLogsDialog = removeCallLogsDialog;
		this.handler = handler;
	}

	public void run() {
		removeCallLogs();
	}

	/**
	 * 
	 * Add the contacts to be blocked.
	 * 
	 */
	private void removeCallLogs() {
		int numRemoved = 0;
		DbHelper dbHelper = new DbHelper(SimpleBlockerApp.getContext());
		try {

			numRemoved = dbHelper.deleteBlockedCallLogs();
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		} finally {

			if (dbHelper != null) {
				dbHelper.cleanUp();
				dbHelper = null;
			}

			removeCallLogsDialog.stopDialog(SimpleBlockerApp.getContext());

			// Create and send the numBanned message to the handler in gui main
			// thread
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putInt(AppConfig.PARAM_NUM_REMOVED, numRemoved);
			message.setData(bundle);
			handler.sendMessage(message);
		}
	}
}
