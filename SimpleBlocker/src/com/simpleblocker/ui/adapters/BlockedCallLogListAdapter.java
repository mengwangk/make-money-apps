package com.simpleblocker.ui.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.simpleblocker.R;
import com.simpleblocker.data.models.BlockedCallLog;
import com.simpleblocker.data.models.CallLog;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;

public final class BlockedCallLogListAdapter <T> extends ArrayAdapter<T> implements SectionIndexer {

	private SherlockListFragment fragment;
	private List<T> contactList;
	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;
	//private List<T> selectedCallLogs;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param viewResourceId
	 * @param objects
	 * @param fragment
	 */
	public BlockedCallLogListAdapter(final Context context, final int viewResourceId, final List<T> objects, final SherlockListFragment fragment) {

		super(context, viewResourceId, objects);

		this.fragment = fragment;

		// Gets the elements passed by parameter
		contactList = (ArrayList<T>) objects;

		//selectedCallLogs = new ArrayList<T>(1);

		// Initialize the indexer to use in the list
		alphaIndexer = new HashMap<String, Integer>();

		// We get the first letter of each item, but as is a HashMap we get only
		// the last one of them
		int size = contactList.size();
		for (int i = size - 1; i >= 0; i--) {
			T item = contactList.get(i);
			alphaIndexer.put(((BlockedCallLog) item).getContactName().substring(0, 1), i);
		}

		// Creates the key list with the first letter
		List<String> keyList = new ArrayList<String>(alphaIndexer.size());
		for (String key : alphaIndexer.keySet()) {
			keyList.add(key);
		}

		// We create the array with the keys
		sections = new String[keyList.size()];
		keyList.toArray(sections);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {

		final View view;
		if (convertView == null)
			view = fragment.getSherlockActivity().getLayoutInflater().inflate(R.layout.blocked_call_log_list_item, parent, false);
		else
			view = convertView;

		// Gets the item in the actual position and casting it
		final T item = (T) getItem(position);

		try {
			final TextView contactName;
			final TextView contactPhone;
			final TextView timestamp;
			//final CheckBox contactSelected;
			final ImageView callType;

			contactName = (TextView) view.findViewById(R.id.contactname);
			contactPhone = (TextView) view.findViewById(R.id.contactphone);
			//contactSelected = (CheckBox) view.findViewById(R.id.contactselected);
			callType = (ImageView) view.findViewById(R.id.calltype);
			timestamp = (TextView) view.findViewById(R.id.timestamp);

			//final Contact thisCallLog = (CallLog) item;
			
			/*
			contactSelected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						if (!selectedCallLogs.contains(thisCallLog))
							selectedCallLogs.add(item);
					} else {
						selectedCallLogs.remove(item);
					}
				}
			});
			

			if (selectedCallLogs.contains(thisCallLog))
				contactSelected.setChecked(true);
			else
				contactSelected.setChecked(false);

			*/
			
			contactName.setText(((BlockedCallLog) item).getContactName()); 
			timestamp.setText(((BlockedCallLog) item).getTimestamp()); 
			contactPhone.setText(((BlockedCallLog) item).getPhoneNo());
			callType.setImageResource(R.drawable.ic_menu_blocked_call);

		} catch (Exception e) {
			Log.e(AppConfig.LOG_TAG, ExceptionUtils.getString(e));
		}

		return view;
	}

	@Override
	public Object[] getSections() {
		return sections;
	}

	@Override
	public int getPositionForSection(int section) {

		String letter = sections[section];
		return alphaIndexer.get(letter);
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}
	
/*
	public List<T> getSelectedCallLogs() {
		return selectedCallLogs;
	}

	public void removeSelectedCallLogs() {
		selectedCallLogs.clear();
	}*/
}

