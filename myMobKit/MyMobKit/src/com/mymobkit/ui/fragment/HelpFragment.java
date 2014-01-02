package com.mymobkit.ui.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;

import com.mymobkit.R;
import com.mymobkit.common.LocaleManager;
import com.mymobkit.ui.base.BaseFragment;

public final class HelpFragment extends BaseFragment {

	public static HelpFragment newInstance() {
		return new HelpFragment();
	}

	private static final String BASE_URL =
			"file:///android_asset/htmlhelp-" + LocaleManager.getTranslatedAssetLanguage() + '/';

	private WebView webView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		String url = webView.getUrl();
		if (url != null && !"".equals(url)) {
			webView.saveState(state);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.fragment_help, container, false);

		webView = (WebView) view.findViewById(R.id.help_contents);

		if (savedInstanceState == null) {
			webView.loadUrl(BASE_URL + "index.html");
		} else {
			webView.restoreState(savedInstanceState);
		}
		
		view.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
					webView.goBack();
					return true;
				}
				return false;
			}
		});

		return view;
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
