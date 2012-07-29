package com.simpleblocker.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

import com.simpleblocker.R;
import com.simpleblocker.listeners.ContactDialogListener;
import com.simpleblocker.operations.IncomingCallOperations;
import com.simpleblocker.ui.activities.base.BaseFragmentActivity;
import com.simpleblocker.ui.adapters.PageViewerHelpAdapter;
import com.simpleblocker.ui.adapters.PageViewerTabsAdapter;
import com.simpleblocker.ui.dialogs.DialogOperations;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * Main activity screen.
 * 
 */
public class MainActivity extends BaseFragmentActivity implements ContactDialogListener {

	/*
	 * private TabHost tabHost; private ViewPager viewPager; private TabsAdapter
	 * tabsAdapter; private HorizontalScrollView horizontalScrollView;
	 */

	private FragmentPagerAdapter pageViewerTabsAdapter;
	private ViewPager viewPager;
	private PageIndicator titlePageIndicator;

	private SlidingDrawer slidingDrawer;
	private FragmentPagerAdapter pHelpAdapter;
	private ViewPager helpPager;
	private PageIndicator helpIndicator;
	private Button helpButton;
	private LinearLayout tabLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(AppConfig.MY_THEME);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/*
		 * tabHost = (TabHost) findViewById(android.R.id.tabhost);
		 * tabHost.setup();
		 * 
		 * viewPager = (ViewPager) findViewById(R.id.pager);
		 * 
		 * horizontalScrollView = (HorizontalScrollView)
		 * findViewById(R.id.horizontalscrollview); tabsAdapter = new
		 * TabsAdapter(this, tabHost, viewPager, horizontalScrollView);
		 * 
		 * 
		 * tabsAdapter.addTab(tabHost.newTabSpec(getString(R.string.
		 * label_tab_blocked_list
		 * )).setIndicator(getString(R.string.label_tab_blocked_list)),
		 * BlockedListFragment.class, null);
		 * 
		 * tabsAdapter.addTab(tabHost.newTabSpec(getString(R.string.
		 * label_tab_call_log
		 * )).setIndicator(getString(R.string.label_tab_call_log)),
		 * CallLogFragment.class, null);
		 * 
		 * tabsAdapter.addTab(tabHost.newTabSpec(getString(R.string.
		 * label_tab_settings
		 * )).setIndicator(getString(R.string.label_tab_settings)),
		 * SettingsFragment.class, null);
		 * 
		 * for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
		 * tabHost.getTabWidget().getChildAt(i).setPadding(15, 15, 15, 15);
		 * tabHost.getTabWidget().getChildAt(i).getLayoutParams().height -= 10;
		 * }
		 * 
		 * if (savedInstanceState != null) {
		 * tabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); }
		 */
		
		pageViewerTabsAdapter = new PageViewerTabsAdapter(getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(pageViewerTabsAdapter);
		titlePageIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		titlePageIndicator.setViewPager(viewPager);
		tabLayout = (LinearLayout) findViewById(R.id.tablayout);

		// Stuff created to navigate through the different help and about me
		// info
		helpButton = (Button) findViewById(R.id.handle);
		slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);
		pHelpAdapter = new PageViewerHelpAdapter(getSupportFragmentManager());
		helpPager = (ViewPager) findViewById(R.id.helppager);
		helpPager.setAdapter(pHelpAdapter);
		helpIndicator = (CirclePageIndicator) findViewById(R.id.helpindicator);
		helpIndicator.setViewPager(helpPager);

		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				tabLayout.setVisibility(View.INVISIBLE);
				helpButton.setBackgroundResource(R.drawable.ic_menu_down_arrow);
			}
		});

		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				tabLayout.setVisibility(View.VISIBLE);
				helpButton.setBackgroundResource(R.drawable.ic_menu_help);
			}
		});

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			super.onSaveInstanceState(outState);
		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}
	}

	@Override
	public void onBackPressed() {
		// If we have the slidingDrawer open, we close it, if not the normal
		// behavior of the back button
		if (slidingDrawer.isOpened())
			slidingDrawer.close();
		else
			super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * This handler manages the action regarding to check if the user click yes
	 * over the confirm action that realize the first database synchronization
	 * or not.
	 */
	public final Handler handler = new Handler() {

		public void handleMessage(Message message) {
			final String NUM_CONTACTS = "NUM_CONTACTS";
			int numContacts = message.getData().getInt(NUM_CONTACTS);
			Log.d(AppConfig.LOG_TAG, "Num contacts sync 1st time: " + numContacts);
		}
	};

	@Override
	public void clickYes() {
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host 0dp
	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
	 * show as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct paged in the ViewPager whenever the selected tab
	 * changes.
	 */

	/*
	 * public static class TabsAdapter extends FragmentPagerAdapter implements
	 * TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener { private
	 * final Context myContext; private final TabHost myTabHost; private final
	 * ViewPager myViewPager; private final HorizontalScrollView
	 * myHorizontalScrollView; private final ArrayList<TabInfo> myTabs = new
	 * ArrayList<TabInfo>();
	 * 
	 * static final class TabInfo { private final String tag; private final
	 * Class<?> clss; private final Bundle args;
	 * 
	 * TabInfo(String _tag, Class<?> _class, Bundle _args) { this.tag = _tag;
	 * this.clss = _class; this.args = _args; } }
	 * 
	 * static class DummyTabFactory implements TabHost.TabContentFactory {
	 * private final Context mContext;
	 * 
	 * public DummyTabFactory(Context context) { this.mContext = context; }
	 * 
	 * @Override public View createTabContent(String tag) { View v = new
	 * View(mContext); v.setMinimumWidth(0); v.setMinimumHeight(0); return v; }
	 * }
	 * 
	 * public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager
	 * pager, HorizontalScrollView horizontalScrollView) {
	 * super(activity.getSupportFragmentManager()); this.myContext = activity;
	 * this.myTabHost = tabHost; this.myViewPager = pager;
	 * this.myHorizontalScrollView = horizontalScrollView;
	 * this.myTabHost.setOnTabChangedListener(this);
	 * this.myViewPager.setAdapter(this);
	 * this.myViewPager.setOnPageChangeListener(this); }
	 * 
	 * public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
	 * tabSpec.setContent(new DummyTabFactory(myContext)); String tag =
	 * tabSpec.getTag();
	 * 
	 * TabInfo info = new TabInfo(tag, clss, args); myTabs.add(info);
	 * myTabHost.addTab(tabSpec); notifyDataSetChanged(); }
	 * 
	 * @Override public int getCount() { return myTabs.size(); }
	 * 
	 * @Override public Fragment getItem(int position) { TabInfo info =
	 * myTabs.get(position); return Fragment.instantiate(myContext,
	 * info.clss.getName(), info.args); }
	 * 
	 * @Override public void onTabChanged(String tabId) { int position =
	 * myTabHost.getCurrentTab(); myViewPager.setCurrentItem(position); }
	 * 
	 * @Override public void onPageScrolled(int position, float positionOffset,
	 * int positionOffsetPixels) { }
	 * 
	 * @Override public void onPageSelected(int position) {
	 * 
	 * myHorizontalScrollView.scrollTo(((int) (myTabHost.getWidth() * (position
	 * / ((double) (getCount() - 1))))), 0);
	 * 
	 * // Unfortunately when TabHost changes the current tab, it kindly // also
	 * takes care of putting focus on it when not in touch mode. // The jerk. //
	 * This hack tries to prevent this from pulling focus out of our //
	 * ViewPager. TabWidget widget = myTabHost.getTabWidget(); int
	 * oldFocusability = widget.getDescendantFocusability();
	 * widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
	 * myTabHost.setCurrentTab(position);
	 * widget.setDescendantFocusability(oldFocusability); }
	 * 
	 * @Override public void onPageScrollStateChanged(int state) { } }
	 */
}
