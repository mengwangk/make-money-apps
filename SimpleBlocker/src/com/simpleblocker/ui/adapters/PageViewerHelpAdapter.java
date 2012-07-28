package com.simpleblocker.ui.adapters;

import com.simpleblocker.ui.fragments.HelpFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public final class PageViewerHelpAdapter extends FragmentPagerAdapter {

	
	
	public PageViewerHelpAdapter (FragmentManager fm) {
		super(fm);
	}
        
	@Override
	public Fragment getItem(int position) {		
		return HelpFragment.newInstance(position);
	}

	@Override
	public int getCount() {		
		return HelpFragment.NUM_PAGES;
	}
	
	

}
