package com.simpleblocker.tasks;

import java.util.Iterator;
import java.util.List;

import com.simpleblocker.SimpleBlockerApp;
import com.simpleblocker.data.DbHelper;
import com.simpleblocker.data.models.Contact;
import com.simpleblocker.data.models.Phone;
import com.simpleblocker.ui.adapters.ContactListAdapter;
import com.simpleblocker.ui.dialogs.CustomProgressDialog;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

public final class AddContactsTask extends Thread {

	private ArrayAdapter<Contact> arrayAdapter = null;
	private CustomProgressDialog addContactsDialog;
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
	 * @param addContactsDialog
	 * @param handler
	 */
	public AddContactsTask(final ArrayAdapter<Contact> arrayAdapter, final CustomProgressDialog addContactsDialog, final Handler handler) {
		this.arrayAdapter = arrayAdapter;
		this.addContactsDialog = addContactsDialog;
		this.handler = handler;
	}

	public void run() {
		addContacts();
	}

	/**
	 * 
	 * Add the contacts to be blocked.
	 * 
	 */
	private void addContacts() {
		int numBanned = 0;
		DbHelper dbHelper = new DbHelper(SimpleBlockerApp.getContext());
		try {
			List<Contact> selectedContacts = ((ContactListAdapter<Contact>) arrayAdapter).getSelectedContacts();
			removeDuplicate(dbHelper, selectedContacts);
			for (Contact c : selectedContacts) {
				dbHelper.insertBlockedContact(c);
				numBanned++;
			}
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		} finally {

			if (dbHelper != null) {
				dbHelper.cleanUp();
				dbHelper = null;
			}

			addContactsDialog.stopDialog(SimpleBlockerApp.getContext());

			// Create and send the numBanned message to the handler in gui main
			// thread
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putInt(AppConfig.PARAM_NUM_BANNED, numBanned);
			message.setData(bundle);
			handler.sendMessage(message);
		}
	}

	private void removeDuplicate(final DbHelper dbHelper, final List<Contact> contacts) {
		// Check for duplicate
		Iterator<Contact> iter = contacts.iterator();
		while (iter.hasNext()) {
			Contact c = iter.next();
			for (Phone phone : c.getPhoneList()) {
				Contact foundContact = dbHelper.isNumberBlocked(phone.getContactPhone());
				if (!foundContact.isEmpty()) {
					iter.remove();
					break;
				}
			}
		}
	}

}
