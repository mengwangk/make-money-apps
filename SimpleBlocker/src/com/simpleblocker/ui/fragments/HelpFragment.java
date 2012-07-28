package com.simpleblocker.ui.fragments;

import com.actionbarsherlock.app.SherlockFragment;
import com.simpleblocker.R;
import com.simpleblocker.utils.AppConfig;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public final class HelpFragment extends SherlockFragment {
	
	public static final int NUM_PAGES = 2;
	
	private WebView webView;	
			
	/**
	 * Gets a new instance of the {@link HelpFragment} with the 
	 * correspondent URI
	 * 
	 * @param helpPage
	 * @return
	 */
	public static HelpFragment newInstance (int helpPage) {			
		
		String uri = null;
		HelpFragment helpFragment = new HelpFragment();
		
		switch (helpPage) {
			case 0:
				uri = AppConfig.HELP_HELP_URI;
				break;
			case 1: 
				uri = AppConfig.HELP_ABOUT_URI;
				break;
		}
				
		Bundle bundle = new Bundle();
		bundle.putString(AppConfig.FRAGMENT_TYPE, uri);
		helpFragment.setArguments(bundle);
		
		return helpFragment;
	}
	
	
	/**
	 * Gets the uri used in the fragment
	 * @return
	 */
	public String getUri() {
		return getArguments().getString(AppConfig.FRAGMENT_TYPE);
	}
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {				
		
		if (container == null)
			return null;								
			
		View view = inflater.inflate(R.layout.help, container, false);										
		String uri = getUri();
		if (uri != null) {												
			 webView = (WebView) view.findViewById(R.id.webview);
			 webView.getSettings().setJavaScriptEnabled(true);
			 webView.loadUrl(uri);
		}										
		return view;
	}
	
	public int getPageCount(){
		return NUM_PAGES;
	}

}
