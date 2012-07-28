package com.simpleblocker.ui.dialogs.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.simpleblocker.R;
import com.simpleblocker.utils.AppConfig.DialogType;

public final class InfoFragmentDialog extends SherlockDialogFragment {

	private DialogType dialogType;

	private int alertDialogImage;
	private int title;
	private int message;

	// ------------------------------------------------------------------------//

	public static InfoFragmentDialog newInstance(final DialogType dialogType) {
		InfoFragmentDialog infoDialog = new InfoFragmentDialog(dialogType);
		return infoDialog;
	}

	/**
	 * Constructor.
	 * 
	 * @param dialogType
	 */
	public InfoFragmentDialog(final DialogType dialogType) {
		this.dialogType = dialogType;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setLabelsOperationType();
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity()).setIcon(alertDialogImage).setTitle(title).setMessage(message)
				.setCancelable(false).setPositiveButton(R.string.label_dialog_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		return builder.create();
	}

	/**
	 * Set title, message and dialog icon, for each type of operation
	 */
	private void setLabelsOperationType() {

		if (dialogType == DialogType.NO_CONTACT_SELECTED) {
			alertDialogImage = R.drawable.ic_stat_info;
			title = R.string.label_title_info;
			message = R.string.alert_no_contact_selected;
		} else if (dialogType == DialogType.NO_CALL_LOG_SELECTED){
			alertDialogImage = R.drawable.ic_stat_info;
			title = R.string.label_title_info;
			message = R.string.alert_no_call_log_selected;
		}
	}
}
