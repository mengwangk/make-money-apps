package com.simpleblocker.ui.dialogs.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.simpleblocker.R;
import com.simpleblocker.listeners.ContactDialogListener;
import com.simpleblocker.utils.AppConfig;

/**
 * Dialog screen
 */
public class ContactsFragmentDialog extends SherlockDialogFragment {

	private AppConfig.DialogType dialogType;
	private ContactDialogListener listener;

	private int alertDialogImage;
	private int title;
	private int message;

	// ------------------------------------------------------------------------//

	/**
	 * Gets a new {@link ContactsFragmentDialog}
	 * 
	 * @param listener
	 * @param dialogType
	 * @return
	 */
	public static ContactsFragmentDialog newInstance(ContactDialogListener listener, AppConfig.DialogType dialogType) {
		ContactsFragmentDialog warningDialog = new ContactsFragmentDialog(dialogType, listener);
		return warningDialog;
	}

	/**
	 * Constructor
	 * 
	 * @param operationType
	 * @oaram listener
	 */
	public ContactsFragmentDialog(AppConfig.DialogType operationType, ContactDialogListener listener) {

		this.dialogType = operationType;
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		setLabelsOperationType();

		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity()).setIcon(alertDialogImage).setTitle(title).setMessage(message)
				.setCancelable(false).setPositiveButton(R.string.label_dialog_yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						listener.clickYes();
					}
				}).setNegativeButton(R.string.label_dialog_no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						// if (dialogType ==
						// AppConfig.DialogType.RETRIEVE_CONTACT)
						// getSherlockActivity().finish();
					}
				});
		return builder.create();
	}

	/**
	 * Set title, message and dialog icon, for each type of operation
	 */
	private void setLabelsOperationType() {

		if (dialogType == AppConfig.DialogType.ADD_CONTACTS) {
			alertDialogImage = R.drawable.ic_stat_question;
			title = R.string.label_title_add_contacts;
			message = R.string.msg_block_selected_contacts;
		} else if (dialogType == AppConfig.DialogType.REMOVE_CONTACTS) {
			alertDialogImage = R.drawable.ic_stat_question;
			title = R.string.label_title_remove_contacts;
			message = R.string.msg_remove_selected_contacts;
		} else if (dialogType == AppConfig.DialogType.ADD_CONTACT_FROM_CALL_LOGS) {
			alertDialogImage = R.drawable.ic_stat_question;
			title = R.string.label_title_add_call_logs;
			message = R.string.msg_block_selected_call_logs;
		} 
	}

}
