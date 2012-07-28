package com.simpleblocker.ui.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

public final class CustomProgressDialog {

	private ProgressDialog progressDialog;
	private String title;
	private String message;

	// -------- Getters & Setters --------------------------------------//
	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	// --------------------------------------------------------------------//

	/**
	 * @param title
	 * @param message
	 */
	public CustomProgressDialog(final String title, final String message) {
		this.title = title;
		this.message = message;
	}

	/**
	 * Show the synchronization message
	 */
	public void showDialog(final Context context) {
		progressDialog = ProgressDialog.show(context, title, message, true);
	}

	/**
	 * Hide and dismiss the synchronization message
	 * 
	 * @param context
	 */
	public void stopDialog(Context context) {
		progressDialog.dismiss();
	}

}
