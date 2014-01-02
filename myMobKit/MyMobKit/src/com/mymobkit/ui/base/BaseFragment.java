package com.mymobkit.ui.base;

import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mymobkit.listener.DialogListener;

/**
 * Base fragment class.
 *
 */
public abstract class BaseFragment extends SherlockFragment implements OnItemClickListener, DialogListener {

	private SlidingMenu slidingMenu;
	
	public SlidingMenu getSlidingMenu() {
		return slidingMenu;
	}

	public void setSlidingMenu(SlidingMenu slidingMenu) {
		this.slidingMenu = slidingMenu;
	}
	
}
