package com.simpleblocker.ui.fragments;

import java.util.Iterator;
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
import com.simpleblocker.data.DbHelper;
import com.simpleblocker.data.models.Contact;
import com.simpleblocker.data.models.Phone;
import com.simpleblocker.listeners.ContactDialogListener;
import com.simpleblocker.tasks.LoadContactsDataTask;
import com.simpleblocker.ui.adapters.ContactListAdapter;
import com.simpleblocker.ui.dialogs.DialogOperations;
import com.simpleblocker.ui.dialogs.fragments.ContactsFragmentDialog;
import com.simpleblocker.ui.dialogs.fragments.InfoFragmentDialog;
import com.simpleblocker.ui.fragments.base.BaseListFragment;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.AppConfig.DialogType;
import com.simpleblocker.utils.AppConfig.FragmentType;
import com.simpleblocker.utils.ExceptionUtils;

/**
 * Contacts fragment
 * 
 * @author MEKOH
 * 
 */
public final class ContactsFragment extends BaseListFragment<Contact> implements ContactDialogListener {

	/**
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
		new LoadContactsDataTask<Contact>(this, FragmentType.GET_CONTACTS).execute();
	}

	@Override
	public void getDataInfo(final List<Contact> contactList) {

		try {
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
			if (contactList != null) {
				myOwnAdapter = new ContactListAdapter<Contact>(SimpleBlockerApp.getContext(), R.layout.contact_list_item, contactList, this);
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
		inflater.inflate(R.menu.add_contact_menu, menu);
	}

	/**
	 * @param item
	 * @return boolean
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_add_blocked_contact:

			// Check and make sure at lease 1 contact must be selected
			List<Contact> selectedContacts = ((ContactListAdapter<Contact>) myOwnAdapter).getSelectedContacts();
			Log.d(AppConfig.LOG_TAG, selectedContacts.size() + " contacts are selected");
			if (selectedContacts.size() == 0) {
				SherlockDialogFragment dialogFragment = InfoFragmentDialog.newInstance(DialogType.NO_CONTACT_SELECTED);
				dialogFragment.show(getSherlockActivity().getSupportFragmentManager(), getString(R.string.dialog_tag_no_contact));
			} else {
				SherlockDialogFragment dialogFragment = ContactsFragmentDialog.newInstance(this, DialogType.ADD_CONTACTS);
				dialogFragment.show(getSherlockActivity().getSupportFragmentManager(), getString(R.string.dialog_tag_add_contacts));
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
			final List<Contact> selectedContacts = ((ContactListAdapter<Contact>) myOwnAdapter).getSelectedContacts();
			for (Contact c : selectedContacts) {
				myOwnAdapter.remove(c);
			}
			((ContactListAdapter<Contact>) myOwnAdapter).removeSelectedContacts();
			refreshList();

			// Show the toast message
			Toast.makeText(SimpleBlockerApp.getContext(), numBanned + " " + SimpleBlockerApp.getContext().getString(R.string.msg_add_contacts),
					android.widget.Toast.LENGTH_SHORT).show();

		}
	};

	@Override
	public void clickYes() {
		DialogOperations.addContacts(getSherlockActivity(), myOwnAdapter, handler);
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		if (outState.isEmpty()) {
			outState.putBoolean("bug:fix", true);
		}
	}

}