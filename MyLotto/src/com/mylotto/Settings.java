package com.mylotto;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mylotto.data.Prefs;

/**
 * Preferences for the application.
 * 
 * @author MEKOH
 *
 */
public final class Settings extends BaseActivity {

	private static final String CLASS_TAG = Settings.class.getSimpleName();
	Prefs myprefs = null;
	private AlertDialog.Builder adb;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		this.myprefs = new Prefs(getApplicationContext());
		PopulateScreen();	// Load the screen
		this.adb = new AlertDialog.Builder(this);
		final Button savebutton = (Button) findViewById(R.id.savesettings);

		// create anonymous click listener to handle the "save"
		savebutton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				try {

					// get the string and do something with it.
					final EditText userName = (EditText) findViewById(R.id.username);
					final EditText noofDraw = (EditText) findViewById(R.id.noofdraw);
					
					if (userName.getText().length() == 0) {
						AlertDialog ad = Settings.this.adb.create();
						ad.setMessage(Settings.this.getString(R.string.alert_user_name));
						ad.show();
						return;
					}

					// save off values
					Settings.this.myprefs.setUserName(userName.getText().toString());
					Settings.this.myprefs.setNoOfDraw(noofDraw.getText().toString());
					Settings.this.myprefs.save();

					// we're done!
					finish();
				} catch (Exception e) {
					Log.i(Settings.CLASS_TAG, "Failed to Save Settings ["
							+ e.getMessage() + "]");
				}
			}
		});
	}

	/**
	 * Populate the screen
	 */
	private void PopulateScreen() {
		try {
			final EditText userNameField = (EditText) findViewById(R.id.username);
			final EditText noofDraw = (EditText) findViewById(R.id.noofdraw);
			userNameField.setText(this.myprefs.getUserName());
			noofDraw.setText(String.valueOf(this.myprefs.getNoOfDraw()));
		} catch (Exception e) {
			Log.e(CLASS_TAG, "Error populating screen: " + e.getMessage());
		}
	}

}
