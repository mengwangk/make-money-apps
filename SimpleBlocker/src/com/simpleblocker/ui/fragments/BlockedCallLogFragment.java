package com.simpleblocker.ui.fragments;

import java.util.ArrayList;
import java.util.List;

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
import com.simpleblocker.data.models.BlockedCallLog;
import com.simpleblocker.listeners.LogsDialogListener;
import com.simpleblocker.tasks.LoadContactsDataTask;
import com.simpleblocker.ui.adapters.BlockedCallLogListAdapter;
import com.simpleblocker.ui.dialogs.DialogOperations;
import com.simpleblocker.ui.dialogs.fragments.LogsFragmentDialog;
import com.simpleblocker.ui.fragments.base.BaseListFragment;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.AppConfig.DialogType;
import com.simpleblocker.utils.AppConfig.FragmentType;
import com.simpleblocker.utils.ExceptionUtils;

public final class BlockedCallLogFragment extends BaseListFragment<BlockedCallLog> implements LogsDialogListener {

	/**
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
		new LoadContactsDataTask<BlockedCallLog>(this, FragmentType.GET_BLOCKED_CALL_LOGS).execute();
	}

	@Override
	public void getDataInfo(final List<BlockedCallLog> contactList) {

		try {
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
			if (contactList != null) {
				myOwnAdapter = new BlockedCallLogListAdapter<BlockedCallLog>(SimpleBlockerApp.getContext(), R.layout.blocked_call_log_list_item,
						contactList, this);
				setListAdapter(myOwnAdapter);
				refreshList();
			}
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.call_log_menu, menu);
	}

	/**
	 * @param item
	 * @return boolean
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_clear_call_log:
			SherlockDialogFragment removeDialogFragment = LogsFragmentDialog.newInstance(this, DialogType.REMOVE_CALL_LOGS);
			removeDialogFragment.show(getSherlockActivity().getSupportFragmentManager(), getString(R.string.dialog_tag_remove_call_logs));
			break;
		case R.id.menu_refresh_call_log:
			SherlockDialogFragment refreshDialogFragment = LogsFragmentDialog.newInstance(this, DialogType.REFRESH_CALL_LOGS);
			refreshDialogFragment.show(getSherlockActivity().getSupportFragmentManager(), getString(R.string.dialog_tag_remove_call_logs));
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
			myOwnAdapter.clear();
			refreshList();

			// Show the toast message
			Toast.makeText(SimpleBlockerApp.getContext(), SimpleBlockerApp.getContext().getString(R.string.msg_cleared_call_logs),
					android.widget.Toast.LENGTH_SHORT).show();

		}
	};

	// @Todo: To refresh the log periodically
	public final Handler refreshHandler = new Handler() {
		public void handleMessage(final Message message) {
			ArrayList<BlockedCallLog> callLogs = null;
			callLogs = (ArrayList<BlockedCallLog>) message.getData().getSerializable(AppConfig.PARAM_CALL_LOGS);

			// set the array adapter
			if (callLogs != null) {

				// first delete the previous content list
				myOwnAdapter.clear();

				// Second, add all call logs to the list
				for (BlockedCallLog callLog : callLogs) {
					myOwnAdapter.add(callLog);
				}

				refreshList();
			}
			
			// Show the toast message
			Toast.makeText(SimpleBlockerApp.getContext(), SimpleBlockerApp.getContext().getString(R.string.msg_refreshed_call_logs),
					android.widget.Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		if (outState.isEmpty()) {
			outState.putBoolean("bug:fix", true);
		}
	}

	@Override
	public void clickYesClearLogs() {
		DialogOperations.removeCallLogs(getSherlockActivity(), myOwnAdapter, handler);

	}

	@Override
	public void clickYesRefreshLogs() {
		DialogOperations.refreshAllCallLogs(getSherlockActivity(), myOwnAdapter, refreshHandler);
	}

}
