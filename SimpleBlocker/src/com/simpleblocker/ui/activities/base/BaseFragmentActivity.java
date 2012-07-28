package com.simpleblocker.ui.activities.base;

import android.os.Bundle;
import android.view.Window;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class BaseFragmentActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// To use the progress loader in the action bar
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}

	/**
	 * This function update the action bar home icon to allow up navigation
	 * 
	 * @param homeToUp
	 */
	public void homeToUp(boolean homeToUp) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(homeToUp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Empty creator to build the initial action bar
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
	
			// To come back to the previous activity we finalize it
			case android.R.id.home:
				finish();
				break;
		}
		return false;
	}

}
