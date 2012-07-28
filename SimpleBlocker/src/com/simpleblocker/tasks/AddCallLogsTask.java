package com.simpleblocker.tasks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.simpleblocker.SimpleBlockerApp;
import com.simpleblocker.data.DbHelper;
import com.simpleblocker.data.models.CallLog;
import com.simpleblocker.data.models.Contact;
import com.simpleblocker.ui.adapters.CallLogListAdapter;
import com.simpleblocker.ui.dialogs.CustomProgressDialog;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;

public final class AddCallLogsTask extends Thread {

	private ArrayAdapter<CallLog> arrayAdapter = null;
	private CustomProgressDialog addCallLogsDialog;
	private Handler handler;

	// ------------- Getters & Setters ------------------------------//

	public ArrayAdapter<CallLog> getArrayAdapter() {
		return arrayAdapter;
	}

	public void setArrayAdapter(ArrayAdapter<CallLog> arrayAdapter) {
		this.arrayAdapter = arrayAdapter;
	}

	// ------------------------------------------------------------------------//

	/**
	 * Constructor with the basic parameter
	 * 
	 * @param arrayAdapter
	 * @param addCallLogsDialog
	 * @param handler
	 */
	public AddCallLogsTask(final ArrayAdapter<CallLog> arrayAdapter, final CustomProgressDialog addCallLogsDialog, final Handler handler) {
		this.arrayAdapter = arrayAdapter;
		this.addCallLogsDialog = addCallLogsDialog;
		this.handler = handler;
	}

	public void run() {
		addCallLogs();
	}

	/**
	 * 
	 * Add the contacts to be blocked.
	 * 
	 */
	private void addCallLogs() {
		int numBanned = 0;
		DbHelper dbHelper = new DbHelper(SimpleBlockerApp.getContext());
		try {
			List<CallLog> selectedCallLogs = ((CallLogListAdapter<CallLog>) arrayAdapter).getSelectedCallLogs();
			removeDuplicate(dbHelper, selectedCallLogs);
			HashSet<String> phoneNumbers = new LinkedHashSet<String>();
			for (CallLog c : selectedCallLogs) {
				if (!phoneNumbers.contains(c.getPhoneNo())) {
					dbHelper.insertBlockedContact(c);
					numBanned++;
					phoneNumbers.add(c.getPhoneNo());
				}

			}
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		} finally {

			if (dbHelper != null) {
				dbHelper.cleanUp();
				dbHelper = null;
			}

			addCallLogsDialog.stopDialog(SimpleBlockerApp.getContext());

			// Create and send the numBanned message to the handler in gui main
			// thread
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putInt(AppConfig.PARAM_NUM_BANNED, numBanned);
			message.setData(bundle);
			handler.sendMessage(message);
		}
	}

	private void removeDuplicate(final DbHelper dbHelper, final List<CallLog> callLog) {
		// Check for duplicate
		Iterator<CallLog> iter = callLog.iterator();
		while (iter.hasNext()) {
			CallLog c = iter.next();
			Contact foundContact = dbHelper.isNumberBlocked(c.getPhoneNo());
			if (!foundContact.isEmpty()) {
				iter.remove();
				break;
			}

		}
	}
}
