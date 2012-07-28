package com.simpleblocker.tasks;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.simpleblocker.SimpleBlockerApp;
import com.simpleblocker.data.DbHelper;
import com.simpleblocker.data.models.Contact;
import com.simpleblocker.ui.adapters.ContactListAdapter;
import com.simpleblocker.ui.dialogs.CustomProgressDialog;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;

public final class RemoveContactsTask extends Thread {

	private ArrayAdapter<Contact> arrayAdapter = null;
	private CustomProgressDialog removeContactsDialog;
	private Handler handler;

	// ------------- Getters & Setters ------------------------------//

	public ArrayAdapter<Contact> getArrayAdapter() {
		return arrayAdapter;
	}

	public void setArrayAdapter(ArrayAdapter<Contact> arrayAdapter) {
		this.arrayAdapter = arrayAdapter;
	}

	// ------------------------------------------------------------------------//

	/**
	 * Constructor with the basic parameter
	 * 
	 * @param arrayAdapter
	 * @param removeContactsDialog
	 * @param handler
	 */
	public RemoveContactsTask(final ArrayAdapter<Contact> arrayAdapter, final CustomProgressDialog removeContactsDialog, final Handler handler) {
		this.arrayAdapter = arrayAdapter;
		this.removeContactsDialog = removeContactsDialog;
		this.handler = handler;
	}

	public void run() {
		removeContacts();
	}

	/**
	 * 
	 * Add the contacts to be blocked.
	 * 
	 */
	private void removeContacts() {
		int numRemoved = 0;
		DbHelper dbHelper = new DbHelper(SimpleBlockerApp.getContext());
		try {

			List<Contact> selectedContacts = ((ContactListAdapter<Contact>) arrayAdapter).getSelectedContacts();
			numRemoved = dbHelper.deleteBlockedContacts(selectedContacts);
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		} finally {

			if (dbHelper != null) {
				dbHelper.cleanUp();
				dbHelper = null;
			}
			
			removeContactsDialog.stopDialog(SimpleBlockerApp.getContext());

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
