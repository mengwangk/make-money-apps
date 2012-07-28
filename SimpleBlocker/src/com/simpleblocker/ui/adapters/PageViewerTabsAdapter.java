package com.simpleblocker.ui.adapters;

import com.simpleblocker.R;
import com.simpleblocker.SimpleBlockerApp;
import com.simpleblocker.ui.fragments.BlockedListFragment;
import com.simpleblocker.ui.fragments.BlockedCallLogFragment;
import com.simpleblocker.ui.fragments.SettingsFragment;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public final class PageViewerTabsAdapter extends FragmentPagerAdapter {

	private Resources resources = SimpleBlockerApp.getContext().getResources();

	public final String[] TAB_TITLES = { resources.getString(R.string.label_tab_blocked_list), resources.getString(R.string.label_tab_call_log),
			resources.getString(R.string.label_tab_settings) };

	public PageViewerTabsAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {

		if (position == 0) {
			return new BlockedListFragment();
		} else if (position == 1) {
			return new BlockedCallLogFragment();
		} else if (position == 2) {
			return new SettingsFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		return TAB_TITLES.length;
	}

	@Override
	public String getPageTitle(int position) {
		return TAB_TITLES[position % TAB_TITLES.length].toUpperCase();
	}
}
