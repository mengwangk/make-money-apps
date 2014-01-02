package com.mymobkit.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mymobkit.R;
import com.mymobkit.ui.adapter.SurveillancePageViewerTabsAdapter;
import com.mymobkit.ui.base.BaseFragment;
import com.viewpagerindicator.TabPageIndicator;

public final class SurveillanceSettingsFragment extends BaseFragment {

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (getSlidingMenu() != null)
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	public static SurveillanceSettingsFragment newInstance() {
		return new SurveillanceSettingsFragment();
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		FragmentPagerAdapter adapter = new SurveillancePageViewerTabsAdapter(getChildFragmentManager());
		ViewPager viewPager = (ViewPager) getView().findViewById(R.id.pager);
		viewPager.setAdapter(adapter);
		TabPageIndicator indicator = (TabPageIndicator) getView().findViewById(R.id.indicator);
		indicator.setViewPager(viewPager);

		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					// on first page, you can get sliding menu by swiping the entire screen
					if (getSlidingMenu() != null)
						getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					// on others page, you can get sliding menu by swiping the half screen
					if (getSlidingMenu() != null)
						getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}

			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return super.getView();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onViewStateRestored(final Bundle savedInstanceState) {
		// Added to fix a error that the grid view is not restored properly
		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.surveillance_settings, container, false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clickYes() {
		// TODO Auto-generated method stub

	}
}
