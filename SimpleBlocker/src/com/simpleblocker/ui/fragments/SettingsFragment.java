package com.simpleblocker.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.simpleblocker.R;
import com.simpleblocker.SimpleBlockerApp;
import com.simpleblocker.data.DbHelper;
import com.simpleblocker.data.models.CallLog;
import com.simpleblocker.data.models.Contact;
import com.simpleblocker.listeners.ContactDialogListener;
import com.simpleblocker.notification.SimpleBlockerNotifier;
import com.simpleblocker.prefs.Prefs;
import com.simpleblocker.services.ServiceOperations;
import com.simpleblocker.tasks.LoadContactsDataTask;
import com.simpleblocker.ui.activities.PrefsActivity;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;
import com.simpleblocker.utils.AppConfig.FragmentType;

public final class SettingsFragment extends SherlockFragment implements OnClickListener, ContactDialogListener {

	private final int startstopButtonId = R.id.startstop;
	private final int callSettingsButtionId = R.id.callsettings;
	private final int blockRulesButtionId = R.id.blockrules;

	private ToggleButton startStopButton;
	private Button callSettingsButton;
	private Button blockRulesButton;

	private int checkedItemCallHandling;
	private int checkedItemBlockType;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(com.simpleblocker.R.layout.settings, container, false);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		startStopButton = (ToggleButton) getSherlockActivity().findViewById(startstopButtonId);
		callSettingsButton = (Button) getSherlockActivity().findViewById(callSettingsButtionId);
		blockRulesButton = (Button) getSherlockActivity().findViewById(blockRulesButtionId);

		startStopButton.setOnClickListener(this);
		callSettingsButton.setOnClickListener(this);
		blockRulesButton.setOnClickListener(this);

		final Prefs prefs = new Prefs(SimpleBlockerApp.getContext());
		startStopButton.setChecked(prefs.isAppEnabled());

	}

	/**
	 * onClick method for the view widgets
	 * 
	 * @param view
	 *            View of the used widget
	 */
	public void onClick(final View view) {
		int viewId = view.getId();
		switch (viewId) {
		case startstopButtonId:
			startStopServiceProcess();
			SimpleBlockerNotifier.showNotification(SimpleBlockerApp.getContext(), startStopButton.isChecked());
			break;
		case callSettingsButtionId:
			configureCallHandling();
			break;
		case blockRulesButtionId:
			configureBlockRules();
			break;
		default:
			break;
		}

	}

	@Override
	public void clickYes() {
	}

	private void startStopServiceProcess() {
		try {

			final Prefs prefs = new Prefs(SimpleBlockerApp.getContext());
			final boolean result = ServiceOperations.startStop(startStopButton.isChecked());
			prefs.setAppEnabled(startStopButton.isChecked());
			prefs.save();
			if (startStopButton.isChecked()) {

			} else {
				if (result)
					Toast.makeText(SimpleBlockerApp.getContext(), this.getString(R.string.msg_service_stopped), Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(SimpleBlockerApp.getContext(), this.getString(R.string.msg_service_failure), Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

	private void configureCallHandling() {
		final AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
		final Prefs prefs = new Prefs(SimpleBlockerApp.getContext());
		checkedItemCallHandling = prefs.getBlockBehavior();
		alert.setSingleChoiceItems(R.array.handle_call, checkedItemCallHandling, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				checkedItemCallHandling = item;
			}
		});

		alert.setIcon(R.drawable.ic_launcher);
		alert.setTitle(R.string.label_title_call_handling);

		alert.setPositiveButton(R.string.label_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				AppConfig.BlockBehavior blockBehavior = AppConfig.BlockBehavior.fromCode(checkedItemCallHandling);
				prefs.setBlockBehavior(blockBehavior);
				prefs.save();
			}
		});
		alert.setNegativeButton(R.string.label_dialog_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alert.show();

	}

	private void configureBlockRules() {
		final AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
		final Prefs prefs = new Prefs(SimpleBlockerApp.getContext());
		checkedItemBlockType = prefs.getBlockType();
		alert.setSingleChoiceItems(R.array.block_type, checkedItemBlockType, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				checkedItemBlockType = item;
			}
		});

		alert.setIcon(R.drawable.ic_launcher);
		alert.setTitle(R.string.label_title_block_type);

		alert.setPositiveButton(R.string.label_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				AppConfig.BlockType blockType = AppConfig.BlockType.fromCode(checkedItemBlockType);
				prefs.setBlockType(blockType);
				prefs.save();
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
