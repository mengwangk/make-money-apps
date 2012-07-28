
package com.simpleblocker.ui.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

import com.simpleblocker.R;

/**
 * 
 */
public class SyncContactsDialog {
	
	final int FIRST_TIME_SYNC = R.string.msg_synccontactsdialog_first_time;
	final int ANY_TIME_SYNC = R.string.msg_synccontactsdialog_anytime;
	final int REFRESH_LIST = R.string.msg_synccontactsdialog_refresh;
	
	private ProgressDialog progressDialog;
	private String messageDialog = "";	
	
	
	//--------	Getters & Setters	--------------------------------------//
	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}
	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}
	
	public String getMessageDialog () {
		return messageDialog;
	}
	public void setMessageDialog (String messageDialog) {
		this.messageDialog = messageDialog;
	}
	
	//--------------------------------------------------------------------//
	
	/**
	 * Constructor without parameters. 
	 */
	public SyncContactsDialog () {
					
	}
	
	
	/**
	 * Show message regarding first synchronization		
	 * @param context
	 */
	public void showDialogFirstTime (Context context) {
	
		messageDialog = context.getString(FIRST_TIME_SYNC);		
		showDialog(context);
	}
		
	/**
	 * Show message regarding any synchronization.
	 * At the moment (24/02/2010) is not used.
	 * @param context
	 */
	public void showDialogAnyTime (Context context) {
		
		messageDialog = context.getString(ANY_TIME_SYNC);
		showDialog(context);
	}
		
	/**
	 * Show message regarding refreshing synchronization.
	 * 
	 * @param context
	 */
	public void showDialogRefreshList (Context context) {
		messageDialog = context.getString(REFRESH_LIST);
		showDialog(context);
	}
	
	
	/**
	 * Show the dialog with other synchronization message that we like
	 * @param context
	 * @param messageDialog
	 */
	public void showDialogOtherMessage (Context context, String messageDialog) {
		this.messageDialog = messageDialog;
		showDialog(context);
	}
	

	/**
	 * Show the synchronization message
	 * @param context
	 */
	private void showDialog (Context context) {		
		
		progressDialog = ProgressDialog.show(context, "", messageDialog, true);
		
	}
	
	/**
	 * Hide and dismiss the synchronization message
	 * @param context
	 */
	public void stopDialog (Context context) {
		//progressDialog.cancel();
		progressDialog.dismiss();
	}

}
