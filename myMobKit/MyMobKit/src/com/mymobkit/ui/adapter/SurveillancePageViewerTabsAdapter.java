package com.mymobkit.ui.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mymobkit.R;
import com.mymobkit.app.MyMobKitApp;
import com.mymobkit.ui.fragment.CameraSettingsFragment;
import com.mymobkit.ui.fragment.ControlPanelFragment;

public final class SurveillancePageViewerTabsAdapter extends FragmentPagerAdapter {

	private Resources resources = MyMobKitApp.getContext().getResources();

	public final String[] TAB_TITLES = resources.getStringArray(R.array.surveillance_settings_string_array);

	public SurveillancePageViewerTabsAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {

		if (position == 0) {
			return new CameraSettingsFragment();
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
