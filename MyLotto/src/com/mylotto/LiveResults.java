package com.mylotto;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Display live draw results.
 * 
 * @author MEKOH
 *
 */
public class LiveResults extends BaseActivity {

	private static final String CLASS_TAG = LiveResults.class.getSimpleName();
	private AlertDialog.Builder adb;
	private String[] lottoNames;
	private String[] lottoUrls;
	private Spinner lottoSelector;
	private WebView webViewer;
	private String title;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.live_results);
		this.adb = new AlertDialog.Builder(this);
		this.lottoNames = getResources().getStringArray(R.array.lotto);
		this.lottoUrls = getResources().getStringArray(R.array.lotto_url);
		this.lottoSelector = (Spinner) findViewById(R.id.lottoselector);
		this.webViewer = (WebView) findViewById(R.id.webview);
		this.webViewer.getSettings().setJavaScriptEnabled(true);
		this.title = LiveResults.this.getTitle().toString();
		this.webViewer.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				LiveResults.this.setTitle(LiveResults.this.getString(R.string.loading));
				LiveResults.this.setProgress(progress * 100);
				if (progress == 100)
					LiveResults.this.setTitle(title);
			}
		});

		ArrayAdapter<String> selections = new ArrayAdapter<String>(LiveResults.this, android.R.layout.simple_spinner_item, lottoNames);
		lottoSelector.setAdapter(selections);
		lottoSelector.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					int index = lottoSelector.getSelectedItemPosition();
					String url = lottoUrls[index];
					webViewer.loadUrl(url);
				} catch (Exception ex) {
					Log.e(CLASS_TAG, "Error retrieving live results: " + ex.getMessage());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
}
