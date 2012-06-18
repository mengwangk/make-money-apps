package com.smsspeaker;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TwoLineListItem;
 
public class ViewerArrayAdapter extends ArrayAdapter<InboundData> {
 
	private final int resourceId;
 
	public ViewerArrayAdapter(Context context, int textViewResourceId, List<InboundData> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
 
		InboundData msg = getItem(position);
 
		// if the array item is null, nothing to display, just return null
		if (msg == null) {
			return null;
		}
 
		// We need the layout inflater to pick up the view from xml
		LayoutInflater inflater = (LayoutInflater)
						getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		// Pick up the TwoLineListItem defined in the xml file
		TwoLineListItem view;
		if (convertView == null) {
			view = (TwoLineListItem) inflater.inflate(resourceId, parent, false);
		} else {
			view = (TwoLineListItem) convertView;
		}
 
		// Set value for the first text field
		if (view.getText1() != null) {
			view.getText1().setText(msg.subject);
		}
 
		// set value for the second text field
		if (view.getText2() != null) {
			view.getText2().setText(msg.details + "\r\n\r\n" + msg.timestamp.toLocaleString());
		}
 
		return view;
	}
 
}