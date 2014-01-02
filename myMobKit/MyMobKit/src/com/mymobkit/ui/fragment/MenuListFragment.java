package com.mymobkit.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.mymobkit.R;
import com.mymobkit.config.AppConfig;
import com.mymobkit.config.AppConfig.IntentType;
import com.mymobkit.config.AppConfig.MenuListSetup;
import com.mymobkit.ui.activity.SlidingActivityHolder;
import com.mymobkit.ui.adapter.MenuListItem;

/**
 * 
 * Sliding menu list.
 */
public final class MenuListFragment extends SherlockListFragment {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":MenuListFragment";

	private int selectedPos = -1;
	private String intentType = "";

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if (selectedPos >= 0) {
			if (!IntentType.ACTIVITY.name().equals(this.intentType)) {
				MenuListItem item = (MenuListItem) getListView().getItemAtPosition(selectedPos);
				highlightItem(item, selectedPos);
			} else {
				// if it is activity, then go back to previous position
				int prevPos = savedInstanceState.getInt("prev_pos");
				MenuListItem item = (MenuListItem) getListView().getItemAtPosition(prevPos);
				highlightItem(item, prevPos);
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			this.selectedPos = savedInstanceState.getInt("pos");
			this.intentType = savedInstanceState.getString("intent_type");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			// previous position
			int pos = savedInstanceState.getInt("pos");
			if (pos != 0) { // if it is non zero
				savedInstanceState.putInt("prev_pos", pos);
			}
			// current position
			pos = ((MenuListAdapter) getListAdapter()).getSelectedPos();
			MenuListItem item = (MenuListItem) getListView().getItemAtPosition(pos);
			if (item != null) {
				savedInstanceState.putInt("pos", pos);
				savedInstanceState.putString("intent_type", item.setup.getIntentType().name());
			}
		}
	}

	/**
	 * Default constructor
	 */
	public MenuListFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MenuListAdapter adapter = new MenuListAdapter(getActivity());

		for (MenuListSetup m : MenuListSetup.getRootMenu()) {
			adapter.add(new MenuListItem(m));
			if (m.getSubMenu() != null) {
				for (MenuListSetup subMenu : m.getSubMenu()) {
					adapter.add(new MenuListItem(subMenu));
				}
			}
		}
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(final ListView lv, final View v, final int position, final long id) {
		super.onListItemClick(lv, v, position, id);
		Log.d(TAG, "[onListItemClick] Selected Position " + position);
		MenuListItem item = (MenuListItem) lv.getItemAtPosition(position);
		highlightItem(item, position);
	}

	private void highlightItem(final MenuListItem item, final int position) {
		if (item != null) {
			MenuListSetup function = item.setup;
			executeFunction(function);
		}
		((MenuListAdapter) getListAdapter()).setSelectedPos(position);
	}

	public void highlightItem(final MenuListSetup function) {
		executeFunction(function);
		ListView lv = getListView();
		int itemCount = lv.getCount();
		for (int i = 0; i < itemCount; i++) {
			MenuListItem item = (MenuListItem) lv.getItemAtPosition(i);
			if (function == item.setup) {
				((MenuListAdapter) getListAdapter()).setSelectedPos(i);
				break;
			}
		}
	}

	private void executeFunction(final MenuListSetup function) {
		if (function.getIntentType() == IntentType.ACTIVITY) {
			Intent targetIntent = MenuListSetup.createActivityIntent(function);
			if (function.getResultCode() <= 0)
				startActivity(targetIntent);
			else
				startActivityForResult(targetIntent, function.getResultCode());
		} else {
			SlidingActivityHolder slidingActivity = (SlidingActivityHolder) getActivity();
			if (slidingActivity != null) {
				slidingActivity.changeDisplay(function);
			}
		}
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && requestCode > 0) {
			int funcId = data.getIntExtra(AppConfig.FUNCTION_PARAM, -1);
			if (funcId != -1) {
				// Get the function to display
				MenuListSetup setup = MenuListSetup.fromCode(funcId);
				highlightItem(setup);
			}
		}
	}
	/**
	 * 
	 * Menu list adapter.
	 * 
	 */
	public class MenuListAdapter extends ArrayAdapter<MenuListItem> {

		private int selectedPos;

		/**
		 * Constructor.
		 * 
		 * @param context
		 */
		public MenuListAdapter(Context context) {
			super(context, 0);
			this.selectedPos = -1;
		}

		public void setSelectedPos(int pos) {
			this.selectedPos = pos;
			notifyDataSetChanged();
		}

		public int getSelectedPos() {
			return this.selectedPos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			// }
			MenuListSetup setup = getItem(position).setup;
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(setup.getIcon());
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(setup.getTitle());
			if (setup.getLevel() == 0) {
				icon.setBackgroundColor(getResources().getColor(android.R.color.background_light));
				title.setBackgroundColor(getResources().getColor(android.R.color.background_light));
			} else {
				convertView.setPadding(convertView.getPaddingLeft() + 10, convertView.getPaddingTop(), convertView.getPaddingRight(), convertView.getPaddingBottom());
			}

			if (position == selectedPos) {
				convertView.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
				icon.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
				title.setBackgroundColor(getResources().getColor(R.color.holo_blue_bright));
				title.setTextColor(getResources().getColor(android.R.color.white));
			}
			return convertView;
		}
	}

}
