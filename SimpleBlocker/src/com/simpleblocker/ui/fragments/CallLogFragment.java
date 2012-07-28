package com.simpleblocker.ui.fragments;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.simpleblocker.R;
import com.simpleblocker.SimpleBlockerApp;
import com.simpleblocker.data.DbHelper;
import com.simpleblocker.data.models.CallLog;
import com.simpleblocker.data.models.Contact;
import com.simpleblocker.data.models.Phone;
import com.simpleblocker.listeners.ContactDialogListener;
import com.simpleblocker.tasks.LoadContactsDataTask;
import com.simpleblocker.ui.adapters.CallLogListAdapter;
import com.simpleblocker.ui.adapters.ContactListAdapter;
import com.simpleblocker.ui.dialogs.DialogOperations;
import com.simpleblocker.ui.dialogs.fragments.ContactsFragmentDialog;
import com.simpleblocker.ui.dialogs.fragments.InfoFragmentDialog;
import com.simpleblocker.ui.fragments.base.BaseListFragment;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.AppConfig.DialogType;
import com.simpleblocker.utils.AppConfig.FragmentType;
import com.simpleblocker.utils.ExceptionUtils;

public final class CallLogFragment extends BaseListFragment<CallLog> implements ContactDialogListener {

	/**
	 * @param savedInstanceState
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
		new LoadContactsDataTask<CallLog>(this, FragmentType.GET_CALL_LOGS).execute();
	}

	@Override
	public void getDataInfo(List<CallLog> callLogList) {
		try {
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

			if (callLogList != null) {
				myOwnAdapter = new CallLogListAdapter<CallLog>(SimpleBlockerApp.getContext(), R.layout.call_log_list_item, callLogList, this);
				setListAdapter(myOwnAdapter);
				refreshList();
			}
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case AppConfig.REQUEST_CODE_ADD_FROM_CALL_LOGS:
			if (resultCode == Activity.RESULT_OK) {
				refreshList();
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.add_call_log_menu, menu);
	}

	/**
	 * @param item
	 * @return boolean
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_add_blocked_call_log:

			// Check and make sure at lease 1 contact must be selected
			List<CallLog> selectedCallLogs = ((CallLogListAdapter<CallLog>) myOwnAdapter).getSelectedCallLogs();
			Log.d(AppConfig.LOG_TAG, selectedCallLogs.size() + " call logs are selected");

			if (selectedCallLogs.size() == 0) {
				SherlockDialogFragment dialogFragment = InfoFragmentDialog.newInstance(DialogType.NO_CALL_LOG_SELECTED);
				dialogFragment.show(getSherlockActivity().getSupportFragmentManager(), getString(R.string.dialog_tag_no_call_log));
			} else {

				SherlockDialogFragment dialogFragment = ContactsFragmentDialog.newInstance(this, DialogType.ADD_CONTACT_FROM_CALL_LOGS);
				dialogFragment.show(getSherlockActivity().getSupportFragmentManager(), getString(R.string.dialog_tag_add_call_logs));
			}
			break;
		default:
			break;
		}
		return false;
	}

	

	/**
	 * Handler for clear the listView and the array adapter once we have added
	 * all contacts to the banned list
	 */
	public final Handler handler = new Handler() {

		public void handleMessage(final Message message) {

			int numBanned = message.getData().getInt(AppConfig.PARAM_NUM_BANNED);
			final List<CallLog> selectedCallLogs = ((CallLogListAdapter<CallLog>) myOwnAdapter).getSelectedCallLogs();
			for (CallLog c : selectedCallLogs) {
				myOwnAdapter.remove(c);
			}

			((CallLogListAdapter<CallLog>) myOwnAdapter).removeSelectedCallLogs();

			refreshList();

			// Show the toast message
			Toast.makeText(SimpleBlockerApp.getContext(), numBanned + " " + SimpleBlockerApp.getContext().getString(R.string.msg_add_call_logs),
					android.widget.Toast.LENGTH_SHORT).show();

		}
	};

	@Override
	public void clickYes() {
		DialogOperations.addCallLogs(getSherlockActivity(), myOwnAdapter, handler);
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		if (outState.isEmpty()) {
			outState.putBoolean("bug:fix", true);
		}
	}
}