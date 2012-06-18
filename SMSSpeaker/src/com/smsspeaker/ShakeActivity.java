package com.smsspeaker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public final class ShakeActivity extends Activity implements ShakeListener.Callback {
	private ShakeListener shaker = null;
	private TextView transcript = null;
	private ScrollView scroll = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*setContentView(R.layout.main);

		transcript = (TextView) findViewById(R.id.transcript);
		scroll = (ScrollView) findViewById(R.id.scroll);
		*/
		shaker = new ShakeListener(this, 1.25d, 500, this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		shaker.close();
	}

	public void shakingStarted() {
		Log.d("ShakerDemo", "Shaking started!");
		transcript.setText(transcript.getText().toString() + "Shaking started\n");
		scroll.fullScroll(View.FOCUS_DOWN);
	}

	public void shakingStopped() {
		Log.d("ShakerDemo", "Shaking stopped!");
		transcript.setText(transcript.getText().toString() + "Shaking stopped\n");
		scroll.fullScroll(View.FOCUS_DOWN);
	}
}