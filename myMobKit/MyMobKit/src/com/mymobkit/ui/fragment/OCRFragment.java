package com.mymobkit.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mymobkit.R;
import com.mymobkit.ui.base.BaseFragment;

public final class OCRFragment extends BaseFragment {
	
	public static OCRFragment newInstance() {
		return new OCRFragment();
	}

	public OCRFragment(){
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
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clickYes() {
		// TODO Auto-generated method stub
		
	}


	
}

