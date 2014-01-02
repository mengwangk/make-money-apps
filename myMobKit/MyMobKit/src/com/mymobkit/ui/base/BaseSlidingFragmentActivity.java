package com.mymobkit.ui.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mymobkit.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.mymobkit.config.AppConfig;
import com.mymobkit.ui.fragment.MenuListFragment;

/**
 * Base class for sliding menu.
 * 
 */
public class BaseSlidingFragmentActivity extends SlidingFragmentActivity {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":BaseSlidingFragmentActivity";

	private int titleRes;
	protected ListFragment slidingMenuList;

	public BaseSlidingFragmentActivity(int titleRes) {
		this.titleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "[onCreate] Creating sliding menu");
		super.onCreate(savedInstanceState);

		setTitle(titleRes);

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);

		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			slidingMenuList = new MenuListFragment();
			t.replace(R.id.menu_frame, slidingMenuList);
			t.commit();
		} else {
			slidingMenuList = (ListFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		case R.id.menu_home:
			Uri uriUrl = Uri.parse(getString(R.string.mymobkit_url));
			Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
			launchBrowser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplicationContext().startActivity(launchBrowser);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	
	public ListFragment getMenuList() {
		return this.slidingMenuList;
	}

}
