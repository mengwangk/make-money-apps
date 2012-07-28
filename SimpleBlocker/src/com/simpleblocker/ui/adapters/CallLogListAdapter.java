package com.simpleblocker.ui.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.simpleblocker.R;
import com.simpleblocker.data.models.CallLog;
import com.simpleblocker.data.models.Contact;
import com.simpleblocker.utils.AppConfig;
import com.simpleblocker.utils.ExceptionUtils;

/**
 * This class is the adapter to use in the call log list, both the add contacts
 * to ban list, and to remove contacts from this one.
 * 
 * @param <T>
 */
public class CallLogListAdapter<T> extends ArrayAdapter<T> implements SectionIndexer {

	private SherlockListFragment fragment;
	private List<T> contactList;
	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;
	private List<T> selectedCallLogs;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param viewResourceId
	 * @param objects
	 * @param fragment
	 */
	public CallLogListAdapter(final Context context, final int viewResourceId, final List<T> objects, final SherlockListFragment fragment) {

		super(context, viewResourceId, objects);

		this.fragment = fragment;

		// Gets the elements passed by parameter
		contactList = (ArrayList<T>) objects;

		selectedCallLogs = new ArrayList<T>(1);

		// Initialize the indexer to use in the list
		alphaIndexer = new HashMap<String, Integer>();

		// We get the first letter of each item, but as is a HashMap we get only
		// the last one of them
		int size = contactList.size();
		for (int i = size - 1; i >= 0; i--) {
			T item = contactList.get(i);
			alphaIndexer.put(((CallLog) item).getContactName().substring(0, 1), i);
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
			view = fragment.getSherlockActivity().getLayoutInflater().inflate(R.layout.call_log_list_item, parent, false);
		else
			view = convertView;

		// Gets the item in the actual position and casting it
		final T item = (T) getItem(position);

		try {
			final TextView contactName;
			final TextView contactPhone;
			final CheckBox contactSelected;
			final ImageView callType;

			contactName = (TextView) view.findViewById(R.id.contactname);
			contactPhone = (TextView) view.findViewById(R.id.contactphone);
			contactSelected = (CheckBox) view.findViewById(R.id.contactselected);
			callType = (ImageView) view.findViewById(R.id.calltype);

			final Contact thisCallLog = (CallLog) item;
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

			contactName.setText(((CallLog) item).getContactName());
			contactPhone.setText(((CallLog) item).getPhoneNo());

			switch (((CallLog) item).getCallType()) {
			case android.provider.CallLog.Calls.INCOMING_TYPE:
				callType.setImageResource(R.drawable.sym_call_incoming);
				break;
			case android.provider.CallLog.Calls.OUTGOING_TYPE:
				callType.setImageResource(R.drawable.sym_call_outgoing);
				break;
			case android.provider.CallLog.Calls.MISSED_TYPE:
				callType.setImageResource(R.drawable.sym_call_missed);
				break;
			default:
				break;
			}

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

	public List<T> getSelectedCallLogs() {
		return selectedCallLogs;
	}

	public void removeSelectedCallLogs() {
		selectedCallLogs.clear();
	}

}