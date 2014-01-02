package com.mymobkit.ui.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.mymobkit.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mymobkit.app.MyMobKitApp;
import com.mymobkit.config.AppConfig;
import com.mymobkit.config.AppConfig.MenuListSetup;
import com.mymobkit.ui.base.BaseFragment;
import com.mymobkit.ui.base.BaseSlidingFragmentActivity;
import com.mymobkit.ui.base.PreferenceListFragment.OnPreferenceAttachedListener;
import com.mymobkit.ui.fragment.FunctionViewFragment;
import com.mymobkit.ui.fragment.MenuListFragment;

/**
 * Sliding activity holder.
 * 
 */
public final class SlidingActivityHolder extends BaseSlidingFragmentActivity implements OnPreferenceAttachedListener, OnSharedPreferenceChangeListener {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":SlidingActivityHolder";

	/**
	 * Constructor.
	 */
	public SlidingActivityHolder() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);
		List<MenuListSetup> menuList = MenuListSetup.getRootMenu();
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, FunctionViewFragment.newInstance(menuList)).commit();
		setSlidingActionBarEnabled(false);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		surveillanceMode();
	}

	
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		surveillanceMode();
	}
	
	private void surveillanceMode(){
		MyMobKitApp.setSurveillanceMode(false);
		int funcId = getIntent().getIntExtra(AppConfig.FUNCTION_PARAM, -1);
		if (funcId != -1) {
			getIntent().putExtra(AppConfig.FUNCTION_PARAM, -1);
			// Get the function to display
			MenuListSetup setup = MenuListSetup.fromCode(funcId);
			((MenuListFragment)this.getMenuList()).highlightItem(setup);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		int funcId = intent.getIntExtra(AppConfig.FUNCTION_PARAM, -1);
		if (funcId != -1) {
			getIntent().putExtra(AppConfig.FUNCTION_PARAM, funcId);
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.msg_confirm_exit)).setCancelable(false)
				.setPositiveButton(getString(R.string.label_dialog_yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				}).setNegativeButton(getString(R.string.label_dialog_no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Change the content.
	 * 
	 * @param function Function to display.
	 */
	public void changeDisplay(final MenuListSetup function) {
		Log.d(TAG, "[changeDisplay] Change display - " + function.getTitle());
		BaseFragment fragment = MenuListSetup.createFragment(function);
		fragment.setSlidingMenu(getSlidingMenu());
		getSupportActionBar().setTitle(getString(R.string.app_name) + " - " + function.getTitle());
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
		SlidingMenu sm = getSlidingMenu();
		if (sm.isMenuShowing())
			getSlidingMenu().toggle();
	}

	@Override
	public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
		Log.d(TAG, "[onPreferenceAttached] is called");
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.i(TAG, "[onSharedPreferenceChanged] Key - " + key);
	}

}
