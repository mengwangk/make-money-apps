package com.mylotto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mylotto.analysis.FrequencyAnalysisQuery;
import com.mylotto.analysis.FrequencyAnalysisResults;
import com.mylotto.analysis.FrequencyPredictionImpl;
import com.mylotto.analysis.IAnalysisListener;
import com.mylotto.data.Lotto;
import com.mylotto.data.Prefs;
import com.mylotto.data.Result4D;
import com.mylotto.data.SearchCriteria;
import com.mylotto.helper.Constants;
import com.mylotto.helper.DateUtils;
import com.mylotto.service.ILottoMessenger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Perform frequency prediction.
 * 
 * @author MEKOH
 *
 */
public final class FrequencyPrediction extends BaseActivity implements IAnalysisListener {

	private static final String CLASS_TAG = FrequencyPrediction.class.getSimpleName();

	private AlertDialog.Builder adb;
	private Prefs myPref = null;
	private ProgressDialog progressDialog;
	private FrequencyAnalysisQuery query;
	private FrequencyAnalysisResults results;
	private ILottoMessenger lottoMessenger;
	private Handler progressHandler;
	private MyLottoApplication application;
	private List<String> lottoNames;
	private List<SearchCriteria> searchCriteriaList;

	/**
	 * Handler to display the analysis results
	 */
	private final Handler resultHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			Log.v(Constants.LOG_TAG, " " + CLASS_TAG + " worker thread done. Displaying results");
			progressDialog.dismiss();

			// Dismiss the activity and display the results in another activity
			if (results != null) {
				// Assign results to application
				application.setFrequencyAnalysisResults(results);
				Intent intent = new Intent(Constants.INTENT_ACTION_VIEW_FREQUENCY_PREDICTION_RESULTS);
				startActivity(intent);
			} else {
				// Unable to perform analysis. Display the error message
				AlertDialog ad = FrequencyPrediction.this.adb.create();
				ad.setMessage(FrequencyPrediction.this.getString(R.string.alert_freq_prediction_failed));
				ad.show();
			}

		}
	};

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.analysis_prediction_freq);
		this.adb = new AlertDialog.Builder(this);

		// get our application prefs handle
		this.myPref = new Prefs(this);

		final Button btnAnalyze = (Button) findViewById(R.id.analyze);
		final Spinner lottoSelections = (Spinner) findViewById(R.id.lottolist);
		final Spinner fromDateSelections = (Spinner) findViewById(R.id.fromdate);
		final Spinner toDateSelections = (Spinner) findViewById(R.id.todate);
		final EditText noofPastDraw = (EditText) findViewById(R.id.noofpastdraw);

		progressHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// process incoming messages here
				switch (msg.what) {
				case 0:
					// update progress bar
					FrequencyPrediction.this.progressDialog.setMessage("" + (String) msg.obj);
					break;
				case 1:
					FrequencyPrediction.this.progressDialog.cancel();
					finish();
					break;
				case 2: // error occurred
					FrequencyPrediction.this.progressDialog.cancel();
					finish();
					break;
				}
			}
		};

		application = (MyLottoApplication) getApplication();
		lottoMessenger = application.getLottoMessenger();
		List<Lotto> lottos = application.getAvailableLottos();
		lottoNames = new ArrayList<String>(lottos.size());
		for (Lotto lotto : lottos) {
			lottoNames.add(lotto.name);
		}
		ArrayAdapter<String> selections = new ArrayAdapter<String>(FrequencyPrediction.this, android.R.layout.simple_spinner_item, lottoNames);
		lottoSelections.setAdapter(selections);
		lottoSelections.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {

				try {
					int index = lottoSelections.getSelectedItemPosition();
					String lottoName = lottoNames.get(index);
					Log.d(CLASS_TAG, "Selected lotto: " + lottoName);

					if (lottoName == null)
						return;

					searchCriteriaList = lottoMessenger.getSearchCriteria(lottoName);
					List<String> searchList = new ArrayList<String>();
					for (SearchCriteria criteria : searchCriteriaList) {
						searchList.add(criteria.drawDate + " - " + criteria.drawDay);
					}
					String[] searchValues = new String[searchList.size()];
					searchList.toArray(searchValues);
					ArrayAdapter<String> selections = new ArrayAdapter<String>(FrequencyPrediction.this, android.R.layout.simple_spinner_item, searchValues);
					fromDateSelections.setAdapter(selections);
					fromDateSelections.setSelection(selections.getCount() - 1);
					toDateSelections.setAdapter(selections);
					noofPastDraw.setText(String.valueOf(searchCriteriaList.size()));

				} catch (Exception ex) {
					Log.e(CLASS_TAG, "Error setting draw dates: [" + ex.getMessage() + "]");
				}
			}

			public void onNothingSelected(final AdapterView<?> parent) {
			}

		});

		fromDateSelections.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
				final int fromIndex = fromDateSelections.getSelectedItemPosition();
				final int toIndex = toDateSelections.getSelectedItemPosition();
				if (fromIndex <= toIndex) {
					AlertDialog ad = FrequencyPrediction.this.adb.create();
					ad.setMessage(FrequencyPrediction.this.getString(R.string.alert_invalid_date_range));
					ad.show();
					return;
				}
				noofPastDraw.setText(String.valueOf(fromIndex - toIndex + 1 ));
			}

			public void onNothingSelected(final AdapterView<?> parent) {
			}
		});

		toDateSelections.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
				final int fromIndex = fromDateSelections.getSelectedItemPosition();
				final int toIndex = toDateSelections.getSelectedItemPosition();
				if (fromIndex <= toIndex) {
					AlertDialog ad = FrequencyPrediction.this.adb.create();
					ad.setMessage(FrequencyPrediction.this.getString(R.string.alert_invalid_date_range));
					ad.show();
					return;
				}
				noofPastDraw.setText(String.valueOf(fromIndex - toIndex + 1 ));
			}

			public void onNothingSelected(final AdapterView<?> parent) {
			}
		});

		btnAnalyze.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				try {

					// Verify from date and to date
					final int fromIndex = fromDateSelections.getSelectedItemPosition();
					final int toIndex = toDateSelections.getSelectedItemPosition();
					if (fromIndex <= toIndex) {
						AlertDialog ad = FrequencyPrediction.this.adb.create();
						ad.setMessage(FrequencyPrediction.this.getString(R.string.alert_invalid_date_range));
						ad.show();
						return;
					}
					
					final String selectedLotto = lottoSelections.getSelectedItem().toString();
					final String fromDate = searchCriteriaList.get(fromIndex).drawDate;
					final String toDate = searchCriteriaList.get(toIndex).drawDate;

					// Verify no of draw is entered and the range is within 0 to
					// myprefs.getNoOfDraw()

					if (noofPastDraw.getText().length() == 0 || (Integer.parseInt(noofPastDraw.getText().toString()) <= 0)
							|| (Integer.parseInt(noofPastDraw.getText().toString()) > Integer.parseInt(myPref.getNoOfDraw()))) {

						// Must enter a draw no
						AlertDialog ad = FrequencyPrediction.this.adb.create();
						ad.setMessage(String.format(FrequencyPrediction.this.getString(R.string.alert_no_of_draw_required, myPref.getNoOfDraw())));
						ad.show();
						return;
					}

					progressDialog = ProgressDialog.show(FrequencyPrediction.this, FrequencyPrediction.this.getString(R.string.message_prediction_analysis),
							FrequencyPrediction.this.getString(R.string.message_frequency_prediction_in_progress), true, false);

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
							
							query = new FrequencyAnalysisQuery(selectedLotto, Integer.parseInt(noofPastDraw.getText().toString()), fromDate, toDate);
							Log.d(CLASS_TAG, query.toString());
							try {
								final List<Result4D> pastResults = lottoMessenger.getPastResultsByDates(selectedLotto, fromDate, toDate);
								final List<Result4D> unUsedResults = lottoMessenger.getPastResultsAfterDate(selectedLotto, toDate);
								msg.what = 0;
								msg.obj = FrequencyPrediction.this.getString(R.string.message_history_retrieved);
								FrequencyPrediction.this.progressHandler.sendMessage(msg);
								FrequencyPredictionImpl analyzer = new FrequencyPredictionImpl(query);
								analyzer.listener = FrequencyPrediction.this;
								results = analyzer.perform(pastResults, unUsedResults);
							} catch (Exception ex) {
								Log.e(CLASS_TAG, "Unable to perform analysis [" + ex.getMessage() + "]");
							}
							resultHandler.sendEmptyMessage(0);
						}
					}.start();

					// we're done!
					// finish();
				} catch (Exception e) {
					Log.i(FrequencyPrediction.CLASS_TAG, "Failed to perform analysis [" + e.getMessage() + "]");
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
	public String getMessage(final int resId) {
		return this.getString(resId);
	}
}
