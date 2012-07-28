package com.simpleblocker.ui.fragments;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
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
import com.simpleblocker.listeners.ContactDialogListener;
import com.simpleblocker.tasks.LoadContactsDataTask;
import com.simpleblocker.ui.activities.ListActivityHolder;
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
 * Blocked list fragment.
 * 
 * @author MEKOH
 * 
 */
public final class BlockedListFragment extends BaseListFragment<Contact> implements ContactDialogListener {

	private static boolean isFirstLaunched = true;
	private int checkedItem;

	/**
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
		new LoadContactsDataTask<Contact>(this, FragmentType.GET_BLOCKED_CONTACTS).execute();
	}

	@Override
	public void getDataInfo(final List<Contact> contactList) {

		try {
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
			if (contactList != null) {
				myOwnAdapter = new ContactListAdapter<Contact>(SimpleBlockerApp.getContext(), R.layout.contact_list_item, contactList, this);
				setListAdapter(myOwnAdapter);
				refreshList();
				if (isFirstLaunched && contactList.size() == 0) {
					isFirstLaunched = false;
					Toast.makeText(SimpleBlockerApp.getContext(), SimpleBlockerApp.getContext().getString(R.string.msg_prompt_configure),
							android.widget.Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.blocked_list_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_from_contact:
			Intent addContactIntent = new Intent(SimpleBlockerApp.getContext(), ListActivityHolder.class);
			addContactIntent.putExtra(AppConfig.FRAGMENT_TYPE, AppConfig.FragmentType.GET_CONTACTS.ordinal());
			startActivityForResult(addContactIntent, AppConfig.REQUEST_CODE_ADD_FROM_CONTACTS);
			break;
		case R.id.menu_add_from_call_log:
			Intent addCallLogIntent = new Intent(SimpleBlockerApp.getContext(), ListActivityHolder.class);
			addCallLogIntent.putExtra(AppConfig.FRAGMENT_TYPE, AppConfig.FragmentType.GET_CALL_LOGS.ordinal());
			startActivityForResult(addCallLogIntent, AppConfig.REQUEST_CODE_ADD_FROM_CALL_LOGS);
			break;
		case R.id.menu_add_manual:
			addPhoneNumber();
			break;
		case R.id.menu_delete_blocked_contact:
			// Check and make sure at lease 1 contact must be selected
			List<Contact> selectedContacts = ((ContactListAdapter<Contact>) myOwnAdapter).getSelectedContacts();
			Log.d(AppConfig.LOG_TAG, selectedContacts.size() + " contacts are selected");
			if (selectedContacts.size() == 0) {
				SherlockDialogFragment dialogFragment = InfoFragmentDialog.newInstance(DialogType.NO_CONTACT_SELECTED);
				dialogFragment.show(getSherlockActivity().getSupportFragmentManager(), getString(R.string.dialog_tag_remove_contacts));
			} else {
				SherlockDialogFragment dialogFragment = ContactsFragmentDialog.newInstance(this, DialogType.REMOVE_CONTACTS);
				dialogFragment.show(getSherlockActivity().getSupportFragmentManager(), getString(R.string.dialog_tag_remove_contacts));
			}
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	/**
	 * Handler for clear the listView and the array adapter once we have delete
	 * contacts from the blocked list
	 */
	public final Handler handler = new Handler() {

		public void handleMessage(final Message message) {
			int numRemoved = message.getData().getInt(AppConfig.PARAM_NUM_REMOVED);
			final List<Contact> selectedContacts = ((ContactListAdapter<Contact>) myOwnAdapter).getSelectedContacts();
			for (Contact c : selectedContacts) {
				myOwnAdapter.remove(c);
			}
			((ContactListAdapter<Contact>) myOwnAdapter).removeSelectedContacts();
			refreshList();

			// Show the toast message
			Toast.makeText(SimpleBlockerApp.getContext(), numRemoved + " " + SimpleBlockerApp.getContext().getString(R.string.msg_remove_contacts),
					android.widget.Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case AppConfig.REQUEST_CODE_ADD_FROM_CONTACTS:
		case AppConfig.REQUEST_CODE_ADD_FROM_CALL_LOGS:
			// if (resultCode == Activity.RESULT_OK) {
			// Reload from database again
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
			new LoadContactsDataTask<Contact>(this, FragmentType.GET_BLOCKED_CONTACTS).execute();
			// }
			break;
		default:
			break;
		}
	}

	@Override
	public void clickYes() {
		DialogOperations.removeContacts(getSherlockActivity(), myOwnAdapter, handler);
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		if (outState.isEmpty()) {
			outState.putBoolean("bug:fix", true);
		}
	}

	public void addPhoneNumber() {
		final AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
		final EditText input = new EditText(getSherlockActivity());

		checkedItem = 0;
		input.setInputType(InputType.TYPE_CLASS_PHONE);
		input.setHint(R.string.msg_hint_type_here);
		alert.setSingleChoiceItems(R.array.manual_phonenumber_types, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				checkedItem = item;
			}
		});

		alert.setView(input);
		alert.setIcon(R.drawable.ic_launcher);
		alert.setTitle(R.string.label_title_add_contact_manual);
		alert.setPositiveButton(R.string.label_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString().trim();
				boolean isWildCard = true;
				if (!TextUtils.isEmpty(value)) {
					switch (checkedItem) {
					case 0:
						isWildCard = false;
						break;
					case 1:
						value = value.trim() + "*";
						break;
					case 2:
						value = "*" + value.trim() + "*";
						break;
					case 3:
						value = "*" + value.trim();
						break;

					default:
						break;
					}

					DbHelper dbHelper = new DbHelper(SimpleBlockerApp.getContext());
					try {

						// Check in Phone table to see if the number exists
						Contact foundContact = dbHelper.isNumberBlocked(value);
						if (!foundContact.isEmpty()) {
							// This is already a blocked contact
							Toast.makeText(SimpleBlockerApp.getContext(),
									SimpleBlockerApp.getContext().getString(R.string.msg_number_is_already_blocked), Toast.LENGTH_SHORT).show();
							return;
						}

						// If not exists then proceed to insert into database
						CallLog callLog = new CallLog(value, SimpleBlockerApp.getContext().getString(R.string.label_no_available), value, 0);
						callLog.setWildCard(isWildCard);
						dbHelper.insertBlockedContact(callLog);
					
						// inform user
						Toast.makeText(SimpleBlockerApp.getContext(), SimpleBlockerApp.getContext().getString(R.string.msg_number_is_blocked), Toast.LENGTH_SHORT).show();
						
						getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
						new LoadContactsDataTask<Contact>(BlockedListFragment.this, FragmentType.GET_BLOCKED_CONTACTS).execute();

					} catch (Exception e) {
						Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
					} finally {

						if (dbHelper != null) {
							dbHelper.cleanUp();
							dbHelper = null;
						}
					}
				}
			}
		});
		alert.setNegativeButton(R.string.label_dialog_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alert.show();
	}

}