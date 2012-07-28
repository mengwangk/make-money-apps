package com.simpleblocker.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.simpleblocker.R;
import com.simpleblocker.SimpleBlockerApp;
import com.simpleblocker.data.DbHelper;
import com.simpleblocker.data.models.CallLog;
import com.simpleblocker.data.models.Contact;
import com.simpleblocker.data.models.Phone;
import com.simpleblocker.ui.fragments.base.BaseListFragment;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.AppConfig.FragmentType;
import com.simpleblocker.utils.ExceptionUtils;
import com.simpleblocker.utils.TokenizerUtils;

public class LoadContactsDataTask<T> extends AsyncTask<Void, Void, List<T>> {

	private BaseListFragment<T> listener;
	private AppConfig.FragmentType actionType;

	/**
	 * Constructor
	 * 
	 * @param listener
	 * @param fragmentType
	 */
	public LoadContactsDataTask(final BaseListFragment<T> listener, final FragmentType fragmentType) {
		this.listener = listener;
		this.actionType = fragmentType;
	}

	@Override
	protected List<T> doInBackground(Void... params) {
		List<T> results = null;

		if (actionType == FragmentType.GET_CONTACTS)
			results = selectAllContacts();
		else if (actionType == FragmentType.GET_CALL_LOGS) {
			results = selectCallLogs();
		} else if (actionType == FragmentType.GET_BLOCKED_CONTACTS) {
			results = selectBlockedContacts();
		} else if (actionType == FragmentType.GET_BLOCKED_CALL_LOGS) {
			results = selectBlockedCallLogs();
		}
		return results;
	}

	@Override
	protected void onPostExecute(List<T> result) {
		listener.getDataInfo(result);
	}

	private List<T> selectAllContacts() {

		List<T> contactList = new ArrayList<T>(10);

		try {
			Context context = SimpleBlockerApp.getContext();

			// get all contacts from content provider
			Cursor contactCursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

			Log.d(AppConfig.LOG_TAG, "Num contacts: " + contactCursor.getCount());

			// While startCursor has content
			while (contactCursor.moveToNext()) {

				String contactName = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
				String contactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
				String hasPhone = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if (hasPhone.equals("1")) {

					// Create the db4o contact object.
					Contact contact = new Contact(contactId, contactName);

					// where clause
					String where = ContactsContract.Data.CONTACT_ID + " = " + contactId + " AND " + ContactsContract.Data.MIMETYPE + " = '"
							+ ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'";

					Cursor phonesCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, null, null);

					Log.d(AppConfig.LOG_TAG, "count: " + phonesCursor.getCount());

					// While the phonesCursor has content
					while (phonesCursor.moveToNext()) {
						String phoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						if (!TextUtils.isEmpty(phoneNumber)) {
							String newPhone = TokenizerUtils.tokenizerPhoneNumber(phoneNumber, null);
							int type = phonesCursor.getInt(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
							String customLabel = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
							CharSequence phoneType = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), type, customLabel);
							Phone phone = new Phone(contact, newPhone, phoneType.toString());
							contact.getPhoneList().add(phone);
						}
					}
					// close the phones cursor
					phonesCursor.close();

					if (contact.getPhoneList().size() > 0)
						contactList.add((T) contact);
				}
			}
			contactCursor.close();
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}

		// Remove those blocked contacts from the returned list
		List<T> blockedContacts = selectBlockedContacts();
		contactList.removeAll(blockedContacts);
		return contactList;
	}

	private List<T> selectCallLogs() {

		List<T> callList = new ArrayList<T>(10);

		try {
			Context context = SimpleBlockerApp.getContext();

			// Querying for a cursor is like querying for any SQL-Database
			Cursor c = context.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, null, null, null,
					android.provider.CallLog.Calls.DATE + " DESC");

			for (String colName : c.getColumnNames())
				Log.v(AppConfig.LOG_TAG, "Column Name: " + colName);

			// Retrieve the column-indexes of phoneNumber, date and call type
			int numberColumn = c.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
			int dateColumn = c.getColumnIndex(android.provider.CallLog.Calls.DATE);

			// type can be: Incoming, Outgoing or Missed
			int typeColumn = c.getColumnIndex(android.provider.CallLog.Calls.TYPE);

			// Cached name
			int cachedNameColumn = c.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME);

			// Loop through all entries the cursor provides to us.
			if (c.moveToFirst()) {
				do {
					String callerPhoneNumber = c.getString(numberColumn);
					Date callDate = new Date(c.getInt(dateColumn));
					int callType = c.getInt(typeColumn);
					String callCachedName = c.getString(cachedNameColumn);

					if (TextUtils.isEmpty(callCachedName)) {
						callCachedName = context.getString(R.string.label_no_available);
					}

					String newPhone = TokenizerUtils.tokenizerPhoneNumber(callerPhoneNumber, null);
					CallLog callLog = new CallLog(newPhone, callCachedName, newPhone, callType);
					if (!callList.contains(callLog)) {
						callList.add((T) callLog);
					}
				} while (c.moveToNext());
			}
			c.close();
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}

		// Remove those blocked contacts from the returned list
		List<Contact> blockedContacts = (List<Contact>) selectBlockedContacts();
		// Not so clean solution
		for (Contact contact : blockedContacts) {
			Iterator<T> iter = callList.iterator();
			while (iter.hasNext()) {
				CallLog callLog = (CallLog) iter.next();
				for (Phone phone : contact.getPhoneList()) {
					if (phone.getContactPhone().equals(callLog.getPhoneNo())) {
						iter.remove();
						break;
					}
				}
			}
		}
		return callList;
	}

	private List<T> selectBlockedContacts() {
		DbHelper dbHelper = new DbHelper(SimpleBlockerApp.getContext());
		try {
			return (List<T>) dbHelper.getBlockedContacts();
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		} finally {
			if (dbHelper != null) {
				dbHelper.cleanUp();
				dbHelper = null;
			}
		}
		return new ArrayList<T>();
	}

	private List<T> selectBlockedCallLogs() {
		DbHelper dbHelper = new DbHelper(SimpleBlockerApp.getContext());
		try {
			return (List<T>) dbHelper.getBlockedCallLogs();
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		} finally {
			if (dbHelper != null) {
				dbHelper.cleanUp();
				dbHelper = null;
			}
		}
		return new ArrayList<T>();
	}
}
