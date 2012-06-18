package com.mylotto;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.mylotto.analysis.AnalysisHelper;
import com.mylotto.analysis.MatchedDraw;
import com.mylotto.analysis.NumberAnalysisResults;
import com.mylotto.helper.Constants;
import com.mylotto.helper.DateUtils;
import com.mylotto.helper.StringUtils;

/**
 * Activity to display number analysis results.
 * 
 * @author MEKOH
 *
 */
public final class NumberAnalysisView extends BaseActivity {

	private static final String CLASS_TAG = NumberAnalysisView.class.getSimpleName();
	private NumberAnalysisResults results;
	private TextView empty;

	/**
	 * Handler to display the lotto list
	 */
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			if ((results == null) || (results.matchedDraws.size() == 0)) {
				empty.setText(R.string.no_data);
			} else {

				// set list properties
				//final ListView listView = (ListView) findViewById(R.id.list);
				//ResultAdapter resultAdapter = new ResultAdapter(NumberAnalysisView.this, results);
				//listView.setAdapter(resultAdapter);

				
				TableLayout resultTable = (TableLayout)findViewById(R.id.resulttable);
				int id = 1;
				for (MatchedDraw matchedDraw : results.matchedDraws) {
					id++;
					TableRow tr = new TableRow(NumberAnalysisView.this);
					tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

					if (AnalysisHelper.isTop3Prize(matchedDraw.prizeType)) {
						tr.setBackgroundColor(0xFF0000DD);
					} else {
						//tr.setBackgroundColor(0xFFCC0000);
					}
					
					if (matchedDraw.exactMatch){
						tr.setBackgroundColor(0xFF00C000);
					}
					
					TextView matchedNo = new TextView(NumberAnalysisView.this);
					matchedNo.setText(matchedDraw.matchedNo);
					matchedNo.setTextColor(Color.WHITE);
					matchedNo.setId(100+id);
					matchedNo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					tr.addView(matchedNo);

					TextView prizeType = new TextView(NumberAnalysisView.this);
					prizeType.setText(matchedDraw.prizeType);
					prizeType.setTextColor(Color.WHITE);
					prizeType.setId(200+id);
					prizeType.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					tr.addView(prizeType);

					TextView drawDate = new TextView(NumberAnalysisView.this);
					drawDate.setText(DateUtils.formatDate(matchedDraw.drawDate));
					drawDate.setTextColor(Color.WHITE);
					drawDate.setId(300+id);
					drawDate.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					tr.addView(drawDate);

					// Add row to TableLayout
					resultTable.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				}

				TextView from = (TextView) findViewById(R.id.from);
				TextView until = (TextView) findViewById(R.id.until);
				from.setText(DateUtils.formatDate(results.from));
				until.setText(DateUtils.formatDate(results.until));

				TextView firstMatchOn = (TextView) findViewById(R.id.firstmatchedon);
				TextView lastMatchOn = (TextView) findViewById(R.id.lastmatchedon);
				firstMatchOn.setText(DateUtils.formatDate(results.firstMatchedOn));
				lastMatchOn.setText(DateUtils.formatDate(results.lastMatchedOn));

				TextView totalDraws = (TextView) findViewById(R.id.totaldraws);
				totalDraws.setText(String.valueOf(results.totalDraws));

				TextView totalMatched = (TextView) findViewById(R.id.totalmatched);
				totalMatched.setText(String.valueOf(results.totalMatched));

				TextView totalExactMatched = (TextView) findViewById(R.id.totalexactmatched);
				totalExactMatched.setText(String.valueOf(results.totalExactMatched));

				TextView totalRandomMatched = (TextView) findViewById(R.id.totalrandommatched);
				totalRandomMatched.setText(String.valueOf(results.totalRandomMatched));

				TextView totalExactMatchedTop3 = (TextView) findViewById(R.id.totalexactmatchedtop3);
				totalExactMatchedTop3.setText(String.valueOf(results.totalExactMatchTop3));

				TextView totalRandomMatchedTop3 = (TextView) findViewById(R.id.totalrandommatchedtop3);
				totalRandomMatchedTop3.setText(String.valueOf(results.totalRandomMatchTop3));

				TextView averageDrawGaps = (TextView) findViewById(R.id.averagedrawgap);
				averageDrawGaps.setText(String.valueOf(results.averageGapBetweenDraws));

				TextView predictedNextDraw = (TextView) findViewById(R.id.predictednextdraw);
				predictedNextDraw.setText(String.valueOf(results.predictedNextDraw));

				TextView txtGaps = (TextView) findViewById(R.id.textView15);
				SpannableString lblGaps = new SpannableString(getText(R.string.label_gaps));
				lblGaps.setSpan(new UnderlineSpan(), 0, lblGaps.length(), 0);
				txtGaps.setText(lblGaps);
				TextView gaps = (TextView) findViewById(R.id.gaps);
				String gapView = StringUtils.EMPTY;
				for (Integer gap : results.gaps) {
					gapView += gap + "-->";
				}
				gaps.setText(gapView);
			}
		}
	};

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.analysis_number_view);
		this.empty = (TextView) findViewById(R.id.empty);

		// set list properties
		/*
		final ListView listView = (ListView) findViewById(R.id.list);//getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setEmptyView(this.empty);
		listView.setClickable(true);
		listView.setOnItemClickListener(itemListener);
		*/

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(Constants.LOG_TAG, " " + CLASS_TAG + " onResume");
		displayResults();
	}

	/*
	public OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			// Do nothing for now
		}
	};
	*/

	private void displayResults() {
		Log.v(Constants.LOG_TAG, " " + CLASS_TAG + " displayResults");

		new Thread() {
			@Override
			public void run() {
				MyLottoApplication application = (MyLottoApplication) getApplication();
				results = application.getNumberAnalysisResults();
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	/**
	 * Adapter to display the result.
	 * 
	 * @author MEKOH
	 *
	 **/
	public class ResultAdapter extends BaseAdapter {

		private final Context context;
		private final NumberAnalysisResults results;
		private LayoutInflater inflater = null;

		/**
		 * Constructor.
		 * 
		 * @param context
		 * @param results
		 */
		public ResultAdapter(Context context, NumberAnalysisResults results) {
			this.context = context;
			this.results = results;
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return this.results.matchedDraws.size();
		}

		public Object getItem(final int position) {
			return this.results.matchedDraws.get(position);
		}

		public long getItemId(final int position) {
			return position;
		}

		public View getView(final int position, final View convertView, final ViewGroup parent) {
			MatchedDraw matchedDraw = this.results.matchedDraws.get(position);
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.matched_draw, null);

			if (AnalysisHelper.isTop3Prize(matchedDraw.prizeType)) {
				vi.setBackgroundColor(0xFF0000DD);
			} else {
				vi.setBackgroundColor(0xFFCC0000);
			}
			TextView matchNo = (TextView) vi.findViewById(R.id.matchedno);
			matchNo.setText(matchedDraw.matchedNo);

			TextView prizeType = (TextView) vi.findViewById(R.id.prizetype);
			prizeType.setText(matchedDraw.prizeType);

			TextView drawDate = (TextView) vi.findViewById(R.id.drawdate);
			drawDate.setText(DateUtils.formatDate(matchedDraw.drawDate));
			return vi;

		}
	}
}
