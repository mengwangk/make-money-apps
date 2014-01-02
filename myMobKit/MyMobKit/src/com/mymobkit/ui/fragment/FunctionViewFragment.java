package com.mymobkit.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mymobkit.R;
import com.mymobkit.config.AppConfig.MenuListSetup;
import com.mymobkit.ui.activity.SlidingActivityHolder;
import com.mymobkit.ui.base.BaseFragment;

/**
 * 
 * Display customized view of the various utilities.
 * 
 */
public final class FunctionViewFragment extends BaseFragment {

	private List<MenuListSetup> functions = new ArrayList<MenuListSetup>(1);
	private GridView gridView;

	/**
	 * Singleton access method.
	 * 
	 * @return Singleton instance.
	 */
	public static FunctionViewFragment newInstance(final List<MenuListSetup> menuList) {
		return new FunctionViewFragment(menuList);
	}

	public FunctionViewFragment(final List<MenuListSetup> menuList) {
		for (MenuListSetup m : menuList) {
			if (m.getSubMenu() != null) {
				List<MenuListSetup> subMenu = m.getSubMenu();
				functions.addAll(subMenu);
			}
		}
	}

	/**
	 * Default constructor
	 */
	public FunctionViewFragment() {

	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		gridView = (GridView) getSherlockActivity().findViewById(R.id.gridview_function_view);
		gridView.setAdapter(new ImageAdapter());
		gridView.setOnItemClickListener(this);

	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_function_view, container, false);
	}

	@Override
	public void onViewStateRestored(final Bundle savedInstanceState) {
		// Added to fix a error that the grid view is not restored properly
		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void clickYes() {
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		MenuListSetup function = functions.get(position);
		SlidingActivityHolder activity = (SlidingActivityHolder) getActivity();
		((MenuListFragment)activity.getMenuList()).highlightItem(function);
		/*
		if (function.getIntentType() == IntentType.ACTIVITY) {
			Intent targetIntent = MenuListSetup.createActivityIntent(function);
			startActivity(targetIntent);
		} else {
			SlidingActivityHolder activity = (SlidingActivityHolder) getActivity();
			if (activity != null) {
				// activity.changeDisplay(function);
				((MenuListFragment)activity.getMenuList()).highlightItem(function);
			}
		}
		*/
	}

	/**
	 * Image adapter for the grid view.
	 * 
	 */
	public final class ImageAdapter extends BaseAdapter {

		/**
		 * Constructor.
		 * 
		 */
		public ImageAdapter() {
		}

		@Override
		public int getCount() {
			return functions.size();
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			View v;
			if (convertView == null) {
				LayoutInflater li = getSherlockActivity().getLayoutInflater();
				v = li.inflate(R.layout.item, null);
				TextView tv = (TextView) v.findViewById(R.id.item_text);
				tv.setText(functions.get(position).getTitle());
				ImageView iv = (ImageView) v.findViewById(R.id.item_image);
				iv.setImageResource(functions.get(position).getIcon());
			} else {
				v = convertView;
			}
			return v;
		}

		@Override
		public Object getItem(final int arg) {
			return null;
		}

		@Override
		public long getItemId(final int arg) {
			return 0;
		}
	}

}
