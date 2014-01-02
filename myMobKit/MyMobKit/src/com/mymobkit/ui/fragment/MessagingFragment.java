package com.mymobkit.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.common.collect.Lists;
import com.mymobkit.R;
import com.mymobkit.ui.base.BaseFragment;

public class MessagingFragment extends BaseFragment {
	
	private List<String> t = Lists.newArrayList("122");
	
	public static MessagingFragment newInstance() {
		return new MessagingFragment();
	}

	public MessagingFragment(){
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_help, container, false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onViewStateRestored(final Bundle savedInstanceState) {
		// Added to fix a error that the grid view is not restored properly
		super.onViewStateRestored(savedInstanceState);
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

