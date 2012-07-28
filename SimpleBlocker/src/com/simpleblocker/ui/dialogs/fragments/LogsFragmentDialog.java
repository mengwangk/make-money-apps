package com.simpleblocker.ui.dialogs.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.simpleblocker.R;
import com.simpleblocker.listeners.LogsDialogListener;
import com.simpleblocker.utils.AppConfig;

/**
 * Dialog screen
 */
public class LogsFragmentDialog extends SherlockDialogFragment {

	private AppConfig.DialogType dialogType;
	private LogsDialogListener listener;

	private int alertDialogImage;
	private int title;
	private int message;

	// ------------------------------------------------------------------------//

	/**
	 * Gets a new {@link LogsFragmentDialog}
	 * 
	 * @param listener
	 * @param dialogType
	 * @return
	 */
	public static LogsFragmentDialog newInstance(LogsDialogListener listener, AppConfig.DialogType dialogType) {
		LogsFragmentDialog warningDialog = new LogsFragmentDialog(dialogType, listener);
		return warningDialog;
	}

	/**
	 * Constructor
	 * 
	 * @param operationType
	 * @param listener
	 */
	public LogsFragmentDialog(AppConfig.DialogType operationType, LogsDialogListener listener) {

		this.dialogType = operationType;
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		setLabelsOperationType();

		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity()).setIcon(alertDialogImage).setTitle(title).setMessage(message)
				.setCancelable(false).setPositiveButton(R.string.label_dialog_yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (dialogType == AppConfig.DialogType.REFRESH_CALL_LOGS)
							listener.clickYesRefreshLogs();
						else if (dialogType == AppConfig.DialogType.REMOVE_CALL_LOGS)
							listener.clickYesClearLogs();
					}
				}).setNegativeButton(R.string.label_dialog_no, new DialogInterface.OnClickListener() {
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

		if (dialogType == AppConfig.DialogType.REMOVE_CALL_LOGS) {
			alertDialogImage = R.drawable.ic_stat_question;
			title = R.string.label_title_remove_call_logs;
			message = R.string.msg_remove_call_logs;
		} else if (dialogType == AppConfig.DialogType.REFRESH_CALL_LOGS) {
			alertDialogImage = R.drawable.ic_stat_question;
			title = R.string.label_title_refresh_call_logs;
			message = R.string.msg_refresh_call_logs;
		}
	}

}
