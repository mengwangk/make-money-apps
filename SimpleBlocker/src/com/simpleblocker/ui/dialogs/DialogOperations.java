package com.simpleblocker.ui.dialogs;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.simpleblocker.R;
import com.simpleblocker.data.models.BlockedCallLog;
import com.simpleblocker.data.models.CallLog;
import com.simpleblocker.data.models.Contact;
import com.simpleblocker.tasks.AddCallLogsTask;
import com.simpleblocker.tasks.AddContactsTask;
import com.simpleblocker.tasks.RefreshCallLogsTask;
import com.simpleblocker.tasks.RemoveCallLogsTask;
import com.simpleblocker.tasks.RemoveContactsTask;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;

/**
 * 
 * Dialog operations
 * 
 */
public final class DialogOperations {

	/**
	 * Add contacts to database
	 * 
	 * @param context
	 * @param arrayAdapter
	 * @param handler
	 */
	public static void addContacts(Context context, ArrayAdapter<Contact> arrayAdapter, Handler handler) {

		try {

			// Create the progressDialog used while the system adds contacts
			CustomProgressDialog addContactsDialog = new CustomProgressDialog("", context.getString(R.string.msg_adding_contacts));

			// Create the object that this thread will do the process
			AddContactsTask addContactsTask = new AddContactsTask(arrayAdapter, addContactsDialog, handler);

			// Show the progress dialog and run the thread for adding contacts
			addContactsDialog.showDialog(context);
			addContactsTask.start();

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
			throw new Error();
		}
	}

	/**
	 * Add contacts to database
	 * 
	 * @param context
	 * @param arrayAdapter
	 * @param handler
	 */
	public static void addCallLogs(Context context, ArrayAdapter<CallLog> arrayAdapter, Handler handler) {
		try {
			// Create the progressDialog used while the system adds contacts
			CustomProgressDialog addCallLogsDialog = new CustomProgressDialog("", context.getString(R.string.msg_adding_call_logs));

			// Create the object that this thread will do the process
			AddCallLogsTask addCallLogsTask = new AddCallLogsTask(arrayAdapter, addCallLogsDialog, handler);

			// Show the progress dialog and run the thread for adding contacts
			addCallLogsDialog.showDialog(context);
			addCallLogsTask.start();

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
			throw new Error();
		}
	}

	/**
	 * Remove contacts from database.
	 * 
	 * @param context
	 * @param arrayAdapter
	 * @param handler
	 */
	public static void removeContacts(Context context, ArrayAdapter<Contact> arrayAdapter, Handler handler) {

		try {

			// Create the progressDialog used while the system removes contacts
			CustomProgressDialog removeContactsDialog = new CustomProgressDialog("", context.getString(R.string.msg_removing_contacts));

			RemoveContactsTask removeContactsTask = new RemoveContactsTask(arrayAdapter, removeContactsDialog, handler);

			// Show the progress dialog and run the thread for adding contacts
			removeContactsDialog.showDialog(context);
			removeContactsTask.start();

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
			throw new Error();
		}
	}

	/**
	 * Remove call logs from database.
	 * 
	 * @param context
	 * @param arrayAdapter
	 * @param handler
	 */
	public static void removeCallLogs(Context context, ArrayAdapter<BlockedCallLog> arrayAdapter, Handler handler) {

		try {

			// Create the progressDialog used while the system removes contacts
			CustomProgressDialog removeCallLogsDialog = new CustomProgressDialog("", context.getString(R.string.msg_removing_call_logs));

			RemoveCallLogsTask removeCallLogsTask = new RemoveCallLogsTask(arrayAdapter, removeCallLogsDialog, handler);

			// Show the progress dialog and run the thread for adding contacts
			removeCallLogsDialog.showDialog(context);
			removeCallLogsTask.start();

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
			throw new Error();
		}
	}

	public static void refreshAllCallLogs(Context context, ArrayAdapter<BlockedCallLog> arrayAdapter, Handler handler) {

		try {

			// Create the progressDialog used while the system removes contacts
			CustomProgressDialog refreshCallLogsDialog = new CustomProgressDialog("", context.getString(R.string.msg_refreshing_call_logs));

			// Create the object that this thread will do the process
			RefreshCallLogsTask refreshCallLogTask = new RefreshCallLogsTask(arrayAdapter, refreshCallLogsDialog, handler);

			// Show the progress dialog and run the thread for add all contacts
			refreshCallLogsDialog.showDialog(context);
			refreshCallLogTask.start();

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

}
