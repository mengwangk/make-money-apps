package com.mylotto;

import java.util.Iterator;
import java.util.List;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.mylotto.analysis.FrequencyAnalysisResults;
import com.mylotto.analysis.FrequencyAnalysisResults.DigitPosition;
import com.mylotto.analysis.FrequencyAnalysisResults.NumberFrequency;
import com.mylotto.analysis.FrequencyAnalysisResults.PairingFrequency;
import com.mylotto.analysis.MatchedDraw;
import com.mylotto.helper.Constants;
import com.mylotto.helper.DateUtils;

/**
 * View frequency prediction results.
 * 
 * @author MEKOH
 *
 */
public final class FrequencyPredictionView extends BaseActivity {

	private static final String CLASS_TAG = FrequencyPredictionView.class.getSimpleName();
	private FrequencyAnalysisResults results;
	private TextView empty;

	/**
	 * Handler to display the lotto list
	 */
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			if (results == null) {
				empty.setText(R.string.no_data);
			} else {

				TextView from = (TextView) findViewById(R.id.from);
				TextView until = (TextView) findViewById(R.id.until);
				from.setText(DateUtils.formatDate(results.from));
				until.setText(DateUtils.formatDate(results.until));

				TextView totalDraws = (TextView) findViewById(R.id.totaldraws);
				totalDraws.setText(String.valueOf(results.totalDraws));

				TableLayout positionFreqTable = (TableLayout) findViewById(R.id.positionfreqtable);
				positionFreqTable.removeAllViews();

				addPositionFreqTableHeaderRow(positionFreqTable);
				addPositionFreqTableRow(positionFreqTable, results.zeroNumberFreq);
				addPositionFreqTableRow(positionFreqTable, results.oneNumberFreq);
				addPositionFreqTableRow(positionFreqTable, results.twoNumberFreq);
				addPositionFreqTableRow(positionFreqTable, results.threeNumberFreq);
				addPositionFreqTableRow(positionFreqTable, results.fourNumberFreq);
				addPositionFreqTableRow(positionFreqTable, results.fiveNumberFreq);
				addPositionFreqTableRow(positionFreqTable, results.sixNumberFreq);
				addPositionFreqTableRow(positionFreqTable, results.sevenNumberFreq);
				addPositionFreqTableRow(positionFreqTable, results.eightNumberFreq);
				addPositionFreqTableRow(positionFreqTable, results.nineNumberFreq);

				TableLayout pairingFreqTable = (TableLayout) findViewById(R.id.pairingfreqtable);
				pairingFreqTable.removeAllViews();
				addPairFreqTableHeaderRow(pairingFreqTable);
				addPairingFreqTableRow(pairingFreqTable, results.zeroPairingFreq);
				addPairingFreqTableRow(pairingFreqTable, results.onePairingFreq);
				addPairingFreqTableRow(pairingFreqTable, results.twoPairingFreq);
				addPairingFreqTableRow(pairingFreqTable, results.threePairingFreq);
				addPairingFreqTableRow(pairingFreqTable, results.fourPairingFreq);
				addPairingFreqTableRow(pairingFreqTable, results.fivePairingFreq);
				addPairingFreqTableRow(pairingFreqTable, results.sixPairingFreq);
				addPairingFreqTableRow(pairingFreqTable, results.sevenPairingFreq);
				addPairingFreqTableRow(pairingFreqTable, results.eightPairingFreq);
				addPairingFreqTableRow(pairingFreqTable, results.ninePairingFreq);

				TableLayout sumofDigitsFreqTable = (TableLayout) findViewById(R.id.sumofdigitsfreqtable);
				sumofDigitsFreqTable.removeAllViews();
				Iterator<Integer> keys = results.sumofDigitsAnalysis.sumofDigitsFrequency.keySet().iterator();
				while (keys.hasNext()) {
					Integer key = keys.next();
					Integer freq = results.sumofDigitsAnalysis.sumofDigitsFrequency.get(key);
					addSumofDigitsFreqTableRow(results, sumofDigitsFreqTable, key, freq);
				}

				TextView percentage = (TextView) findViewById(R.id.percentage);
				TextView sumStart = (TextView) findViewById(R.id.sumstart);
				TextView sumEnd = (TextView) findViewById(R.id.sumend);
				percentage.setText(String.valueOf(results.sumofDigitsAnalysis.percentage));
				sumStart.setText(String.valueOf(results.sumofDigitsAnalysis.start));
				sumEnd.setText(String.valueOf(results.sumofDigitsAnalysis.end));

				/***
				 
				// Show top pairings
				TextView toppairings = (TextView) findViewById(R.id.toppairings);
				String delim = StringUtils.EMPTY;
				StringBuffer sb = new StringBuffer();
				for (String p : results.prediction.pairingSets) {
					sb.append(delim).append(p);
					delim = ",";
				}
				toppairings.setText(sb.toString());

				// Show combinations
				TextView combinations = (TextView) findViewById(R.id.combinations);
				delim = StringUtils.EMPTY;
				sb = new StringBuffer();
				for (String p : results.prediction.numbers) {
					sb.append(delim).append(p);
					delim = ",";
				}
				combinations.setText(sb.toString());

				TextView totalCombinations = (TextView) findViewById(R.id.totalcombinations);
				totalCombinations.setText(String.format(FrequencyPredictionView.this.getString(R.string.label_total_combinations), results.prediction.numbers.size()));

				TextView matches = (TextView) findViewById(R.id.matches);
				TableLayout matchesTable = (TableLayout) findViewById(R.id.matchtable);
				if (results.matches.matchedDraws.size() == 0) {
					matches.setVisibility(View.GONE);
					matchesTable.setVisibility(View.INVISIBLE);
				} else {
					for (MatchedDraw match : results.matches.matchedDraws) {
						addMatchTableRow(matchesTable, match);
					}
				}
				
				***/

			}
		}
	};

	private void addMatchTableRow(final TableLayout resultTable, final MatchedDraw match) {
		TableRow tr = new TableRow(FrequencyPredictionView.this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		TextView no = new TextView(FrequencyPredictionView.this);
		no.setText(match.matchedNo);
		no.setTextColor(Color.WHITE);
		no.setGravity(Gravity.CENTER);
		no.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.addView(no);

		TextView prize = new TextView(FrequencyPredictionView.this);
		prize.setText(match.prizeType);
		prize.setTextColor(Color.WHITE);
		prize.setGravity(Gravity.CENTER);
		prize.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.addView(prize);

		TextView dt = new TextView(FrequencyPredictionView.this);
		dt.setText(DateUtils.formatDate(match.drawDate));
		dt.setTextColor(Color.WHITE);
		dt.setGravity(Gravity.CENTER);
		dt.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.addView(dt);

		TextView me = new TextView(FrequencyPredictionView.this);
		if (match.exactMatch) {
			me.setText(FrequencyPredictionView.this.getString(R.string.label_yes));
		} else {
			me.setText(FrequencyPredictionView.this.getString(R.string.label_no));
		}
		me.setTextColor(Color.WHITE);
		me.setGravity(Gravity.CENTER);
		me.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.addView(me);

		// Add row to TableLayout
		resultTable.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private void setPosFreqFieldColor(final TextView tv, final List<DigitPosition> positions, final DigitPosition pos, final int trueColor) {
		if (positions.contains(pos)) {
			tv.setTextColor(trueColor);
		}
	}

	private TextView setPosFreqField(final NumberFrequency numberFrequency, final int val, final DigitPosition pos) {
		TextView tv = new TextView(FrequencyPredictionView.this);
		tv.setText(String.valueOf(val));
		setPosFreqFieldColor(tv, numberFrequency.topNumberPositions, pos, Color.GREEN);
		setPosFreqFieldColor(tv, numberFrequency.bottomNumberPositions, pos, Color.RED);
		tv.setGravity(Gravity.CENTER);
		tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		return tv;
	}

	private void addPositionFreqTableHeaderRow(final TableLayout resultTable) {
		TableRow tr = new TableRow(FrequencyPredictionView.this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.setBackgroundColor(Color.RED);

		addPositionFreqTableHeaderColumn(tr, this.getString(R.string.label_no));
		addPositionFreqTableHeaderColumn(tr, this.getString(R.string.label_position_1));
		addPositionFreqTableHeaderColumn(tr, this.getString(R.string.label_position_1));
		addPositionFreqTableHeaderColumn(tr, this.getString(R.string.label_position_1));
		addPositionFreqTableHeaderColumn(tr, this.getString(R.string.label_position_1));
		addPositionFreqTableHeaderColumn(tr, this.getString(R.string.label_total));

		// Add row to TableLayout
		resultTable.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private void addPositionFreqTableHeaderColumn(final TableRow tr, final String text) {
		TextView no = new TextView(FrequencyPredictionView.this);
		no.setTypeface(null, Typeface.BOLD);
		no.setText(text);
		no.setGravity(Gravity.CENTER);
		no.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.addView(no);
	}

	private void addPositionFreqTableRow(final TableLayout resultTable, final NumberFrequency numberFrequency) {
		TableRow tr = new TableRow(FrequencyPredictionView.this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		TextView no = new TextView(FrequencyPredictionView.this);
		no.setText(String.valueOf(numberFrequency.digit));
		no.setTextColor(Color.WHITE);
		no.setBackgroundColor(Color.RED);
		no.setGravity(Gravity.CENTER);
		no.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.addView(no);

		TextView firstPos = setPosFreqField(numberFrequency, numberFrequency.firstPositionFreq, DigitPosition.FIRST);
		tr.addView(firstPos);

		TextView secondPos = setPosFreqField(numberFrequency, numberFrequency.secondPositionFreq, DigitPosition.SECOND);
		tr.addView(secondPos);

		TextView thirdPos = setPosFreqField(numberFrequency, numberFrequency.thirdPositionFreq, DigitPosition.THIRD);
		tr.addView(thirdPos);

		TextView fourthPos = setPosFreqField(numberFrequency, numberFrequency.fourthPositionFreq, DigitPosition.FOURTH);
		tr.addView(fourthPos);

		TextView total = new TextView(FrequencyPredictionView.this);
		total.setText(String.valueOf(numberFrequency.totalFreq));
		total.setTextColor(Color.WHITE);
		total.setGravity(Gravity.CENTER);
		total.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.addView(total);

		// Add row to TableLayout
		resultTable.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private void setPairFreqFieldColor(final TextView tv, final List<Integer> positions, final int digit, final int trueColor, final int falseColor) {
		if (positions.contains(digit)) {
			tv.setTextColor(trueColor);
		} else {
			//tv.setTextColor(falseColor);
		}
	}

	private TextView setPairFreqField(final PairingFrequency pairingFrequency, final int val, final int digit) {
		TextView tv = new TextView(FrequencyPredictionView.this);
		tv.setText(String.valueOf(val));
		setPairFreqFieldColor(tv, pairingFrequency.topPairedDigits, digit, Color.GREEN, Color.WHITE);
		setPairFreqFieldColor(tv, pairingFrequency.bottomPairedDigits, digit, Color.RED, Color.WHITE);
		if (pairingFrequency.median == digit) {
			tv.setTextColor(Color.BLUE);
		}
		tv.setGravity(Gravity.CENTER);
		tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		return tv;
	}

	private void addPairFreqTableHeaderRow(final TableLayout resultTable) {
		TableRow tr = new TableRow(FrequencyPredictionView.this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.setBackgroundColor(Color.RED);

		addPairFreqTableHeaderColumn(tr, this.getString(R.string.label_no));
		addPairFreqTableHeaderColumn(tr,"0");
		addPairFreqTableHeaderColumn(tr,"1");
		addPairFreqTableHeaderColumn(tr,"2");
		addPairFreqTableHeaderColumn(tr,"3");
		addPairFreqTableHeaderColumn(tr,"4");
		addPairFreqTableHeaderColumn(tr,"5");
		addPairFreqTableHeaderColumn(tr,"6");
		addPairFreqTableHeaderColumn(tr,"7");
		addPairFreqTableHeaderColumn(tr,"8");
		addPairFreqTableHeaderColumn(tr,"9");
		
		// Add row to TableLayout
		resultTable.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private void addPairFreqTableHeaderColumn(final TableRow tr, final String text) {
		TextView no = new TextView(FrequencyPredictionView.this);
		no.setTypeface(null, Typeface.BOLD);
		no.setText(text);
		no.setGravity(Gravity.CENTER);
		no.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.addView(no);
	}
	
	private void addPairingFreqTableRow(final TableLayout resultTable, final PairingFrequency pairingFrequency) {
		TableRow tr = new TableRow(FrequencyPredictionView.this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		TextView no = new TextView(FrequencyPredictionView.this);
		no.setText(String.valueOf(pairingFrequency.digit));
		no.setTextColor(Color.WHITE);
		no.setBackgroundColor(Color.RED);
		no.setGravity(Gravity.CENTER);
		no.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.addView(no);

		TextView zero = setPairFreqField(pairingFrequency, pairingFrequency.zeroFreq, 0);
		tr.addView(zero);

		TextView one = setPairFreqField(pairingFrequency, pairingFrequency.oneFreq, 1);
		tr.addView(one);

		TextView two = setPairFreqField(pairingFrequency, pairingFrequency.twoFreq, 2);
		tr.addView(two);

		TextView three = setPairFreqField(pairingFrequency, pairingFrequency.threeFreq, 3);
		tr.addView(three);

		TextView four = setPairFreqField(pairingFrequency, pairingFrequency.fourFreq, 4);
		tr.addView(four);

		TextView five = setPairFreqField(pairingFrequency, pairingFrequency.fiveFreq, 5);
		tr.addView(five);

		TextView six = setPairFreqField(pairingFrequency, pairingFrequency.sixFreq, 6);
		tr.addView(six);

		TextView seven = setPairFreqField(pairingFrequency, pairingFrequency.sevenFreq, 7);
		tr.addView(seven);

		TextView eight = setPairFreqField(pairingFrequency, pairingFrequency.eightFreq, 8);
		tr.addView(eight);

		TextView nine = setPairFreqField(pairingFrequency, pairingFrequency.nineFreq, 9);
		tr.addView(nine);

		// Add row to TableLayout
		resultTable.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private void addSumofDigitsFreqTableRow(final FrequencyAnalysisResults results, final TableLayout resultTable, final Integer sum, final Integer freq) {
		TableRow tr = new TableRow(FrequencyPredictionView.this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		TextView sumField = new TextView(FrequencyPredictionView.this);
		sumField.setText(String.valueOf(sum));
		if (sum >= results.sumofDigitsAnalysis.start && sum <= results.sumofDigitsAnalysis.end) {
			sumField.setTextColor(Color.GREEN);
		} else {
			sumField.setTextColor(Color.WHITE);
		}
		sumField.setGravity(Gravity.CENTER);
		sumField.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.addView(sumField);

		TextView freqField = new TextView(FrequencyPredictionView.this);
		freqField.setText(String.valueOf(freq));
		freqField.setTextColor(Color.WHITE);
		freqField.setGravity(Gravity.CENTER);
		freqField.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.addView(freqField);

		// Add row to TableLayout
		resultTable.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.analysis_prediction_freq_view);
		this.empty = (TextView) findViewById(R.id.empty);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(Constants.LOG_TAG, " " + CLASS_TAG + " onResume");
		displayResults();
	}

	private void displayResults() {
		Log.v(Constants.LOG_TAG, " " + CLASS_TAG + " displayResults");

		new Thread() {
			@Override
			public void run() {
				MyLottoApplication application = (MyLottoApplication) getApplication();
				results = application.getFrequencyAnalysisResults();
				handler.sendEmptyMessage(0);
			}
		}.start();
	}
}
