package com.mylotto;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.mylotto.analysis.IAnalysisListener;
import com.mylotto.analysis.NumberAnalysisImpl;
import com.mylotto.analysis.NumberAnalysisQuery;
import com.mylotto.analysis.NumberAnalysisResults;
import com.mylotto.data.Lotto;
import com.mylotto.data.Prefs;
import com.mylotto.data.Result4D;
import com.mylotto.helper.Constants;
import com.mylotto.service.ILottoMessenger;

/**
 * Perform number analysis
 * 
 * @author MEKOH
 * 
 */
public final class NumberAnalysis extends BaseActivity implements IAnalysisListener {

	private static final String CLASS_TAG = NumberAnalysis.class.getSimpleName();
	private AlertDialog.Builder adb;
	private Prefs myPref = null;
	private ProgressDialog progressDialog;
	private NumberAnalysisQuery query;
	private NumberAnalysisResults results;
	private ILottoMessenger lottoMessenger;
	private Handler progressHandler;
	private MyLottoApplication application;

	/**
	 * Handler to display the analysis results
	 */
	private final Handler resultHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			Log.v(Constants.LOG_TAG, " " + CLASS_TAG + " worker thread done. Displaying results");
			progressDialog.dismiss();

			// Dismiss the activity and display the results in another activity
			if (results != null && results.matchedDraws.size() > 0) {
				// Assign results to application
				application.setNumberAnalysisResults(results);
				Intent intent = new Intent(Constants.INTENT_ACTION_VIEW_NUMBER_ANALYSIS_RESULTS);
				startActivity(intent);
			} else {
				// Unable to perform analysis. Display the error message
				AlertDialog ad = NumberAnalysis.this.adb.create();
				ad.setMessage(NumberAnalysis.this.getString(R.string.alert_number_analysis_failed));
				ad.show();
			}
		}
	};

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.analysis_number);
		this.adb = new AlertDialog.Builder(this);

		// get our application prefs handle
		this.myPref = new Prefs(this);

		final Button btnAnalyze = (Button) findViewById(R.id.analyze);
		final Spinner lottoSelections = (Spinner) findViewById(R.id.lottolist);
		final EditText noofPastDraw = (EditText) findViewById(R.id.noofpastdraw);

		noofPastDraw.setText(myPref.getNoOfDraw());

		progressHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// process incoming messages here
				switch (msg.what) {
				case 0:
					// update progress bar
					NumberAnalysis.this.progressDialog.setMessage("" + (String) msg.obj);
					break;
				case 1:
					NumberAnalysis.this.progressDialog.cancel();
					finish();
					break;
				case 2: // error occurred
					NumberAnalysis.this.progressDialog.cancel();
					finish();
					break;
				}
			}
		};

		application = (MyLottoApplication) getApplication();
		lottoMessenger = application.getLottoMessenger();
		List<Lotto> lottos = application.getAvailableLottos();
		List<String> lottoNames = new ArrayList<String>(lottos.size());
		for (Lotto lotto : lottos) {
			lottoNames.add(lotto.name);
		}
		ArrayAdapter<String> selections = new ArrayAdapter<String>(NumberAnalysis.this, android.R.layout.simple_spinner_item, lottoNames);
		lottoSelections.setAdapter(selections);

		btnAnalyze.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				try {

					final EditText txtMatchedNo = (EditText) findViewById(R.id.matchno);
					final CheckBox chkMatchExact = (CheckBox) findViewById(R.id.matchexact);
					final String selectedLotto = lottoSelections.getSelectedItem().toString();

					if (txtMatchedNo.getText().length() == 0 || txtMatchedNo.getText().length() != 4) {
						// Must enter a draw no
						AlertDialog ad = NumberAnalysis.this.adb.create();
						ad.setMessage(NumberAnalysis.this.getString(R.string.alert_draw_no_blank));
						ad.show();
						return;
					}

					// Verify no of draw is entered and the range is within 0 to
					// myprefs.getNoOfDraw()
					if (noofPastDraw.getText().length() == 0 || (Integer.parseInt(noofPastDraw.getText().toString()) <= 0)
							|| (Integer.parseInt(noofPastDraw.getText().toString()) > Integer.parseInt(myPref.getNoOfDraw()))) {

						// Must enter a draw no
						AlertDialog ad = NumberAnalysis.this.adb.create();
						ad.setMessage(String.format(NumberAnalysis.this.getString(R.string.alert_no_of_draw_required, myPref.getNoOfDraw())));
						ad.show();
						return;

					}
					final boolean matchExact = chkMatchExact.isChecked();
					progressDialog = ProgressDialog.show(NumberAnalysis.this, NumberAnalysis.this.getString(R.string.message_number_analysis),
							NumberAnalysis.this.getString(R.string.message_analysis_in_progress), true, false);

					// get results in a separate thread for
					// ProgressDialog/Handler
					// when complete send "empty" message to handler
					new Thread() {
						@Override
						public void run() {
							// Looper.prepare();

							// set up our message - used to convey progress information
							Message msg = new Message();
							msg.what = 0;

							query = new NumberAnalysisQuery(txtMatchedNo.getText().toString(), selectedLotto, Integer.parseInt(noofPastDraw.getText().toString()), matchExact);
							Log.d(CLASS_TAG, query.toString());
							try {
								List<Result4D> pastResults = lottoMessenger.getPastResults(selectedLotto, Integer.parseInt(noofPastDraw.getText().toString()));
								msg.what = 0;
								msg.obj = NumberAnalysis.this.getString(R.string.message_history_retrieved);
								NumberAnalysis.this.progressHandler.sendMessage(msg);
								NumberAnalysisImpl analyzer = new NumberAnalysisImpl(query);
								analyzer.listener = NumberAnalysis.this;
								results = analyzer.perform(pastResults);
							} catch (Exception ex) {
								Log.e(CLASS_TAG, "Unable to perform analysis [" + ex.getMessage() + "]");
							}
							resultHandler.sendEmptyMessage(0);
						}
					}.start();

					// we're done!
					// finish();
				} catch (Exception e) {
					Log.i(NumberAnalysis.CLASS_TAG, "Failed to perform analysis [" + e.getMessage() + "]");
				}
			}
		});

	}

	@Override
	public void notifyStatus(final String status) {
		// set up our message - used to convey progress information
		Message msg = new Message();
		msg.what = 0;
		msg.obj = status;
		this.progressHandler.sendMessage(msg);
	}

	@Override
	public String getMessage(int resId) {
		return this.getString(resId);
	}
}
