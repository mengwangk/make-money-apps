package com.mylotto;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mylotto.data.Lotto;
import com.mylotto.data.Result4D;
import com.mylotto.data.SearchCriteria;
import com.mylotto.service.ILottoMessenger;

/**
 * Results screen
 * 
 * @author MEKOH
 * 
 */
public final class Results extends BaseActivity {

	private static final String CLASS_TAG = Results.class.getSimpleName();

	private Lotto lotto;
	private Spinner dateSelector;
	private List<SearchCriteria> searchCriteriaList;
	private SearchCriteria selectedCriteria;

	private final Handler initializeHandler = new Handler();

	private final ScreenInitializer screenInitializer = new ScreenInitializer();

	private TextView firstPrize;
	private TextView secondPrize;
	private TextView thirdPrize;

	private TextView special1;
	private TextView special2;
	private TextView special3;
	private TextView special4;
	private TextView special5;
	private TextView special6;
	private TextView special7;
	private TextView special8;
	private TextView special9;
	private TextView special10;

	private TextView consolation1;
	private TextView consolation2;
	private TextView consolation3;
	private TextView consolation4;
	private TextView consolation5;
	private TextView consolation6;
	private TextView consolation7;
	private TextView consolation8;
	private TextView consolation9;
	private TextView consolation10;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);

		// get the selected lotto from the Application
		// (global state placed there)
		MyLottoApplication application = (MyLottoApplication) getApplication();
		lotto = application.getSelectedLotto();

		setTitle(String.format(getTitle().toString(), lotto.name));

		dateSelector = (Spinner) findViewById(R.id.dateselector);
		firstPrize = (TextView) findViewById(R.id.firstprize);
		secondPrize = (TextView) findViewById(R.id.secondprize);
		thirdPrize = (TextView) findViewById(R.id.thirdprize);

		special1 = (TextView) findViewById(R.id.special1);
		special2 = (TextView) findViewById(R.id.special2);
		special3 = (TextView) findViewById(R.id.special3);
		special4 = (TextView) findViewById(R.id.special4);
		special5 = (TextView) findViewById(R.id.special5);
		special6 = (TextView) findViewById(R.id.special6);
		special7 = (TextView) findViewById(R.id.special7);
		special8 = (TextView) findViewById(R.id.special8);
		special9 = (TextView) findViewById(R.id.special9);
		special10 = (TextView) findViewById(R.id.special10);

		consolation1 = (TextView) findViewById(R.id.consolation1);
		consolation2 = (TextView) findViewById(R.id.consolation2);
		consolation3 = (TextView) findViewById(R.id.consolation3);
		consolation4 = (TextView) findViewById(R.id.consolation4);
		consolation5 = (TextView) findViewById(R.id.consolation5);
		consolation6 = (TextView) findViewById(R.id.consolation6);
		consolation7 = (TextView) findViewById(R.id.consolation7);
		consolation8 = (TextView) findViewById(R.id.consolation8);
		consolation9 = (TextView) findViewById(R.id.consolation9);
		consolation10 = (TextView) findViewById(R.id.consolation10);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Spawn a thread to initialize the screen
		initializeHandler.removeCallbacks(screenInitializer);
		initializeHandler.post(screenInitializer);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		initializeHandler.removeCallbacks(screenInitializer);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * Runnable to initialize the screen.
	 * 
	 */
	class ScreenInitializer implements Runnable {

		public void run() {

			try {
				final ILottoMessenger lottoMessenger = ((MyLottoApplication) getApplication()).getLottoMessenger();
				searchCriteriaList = lottoMessenger.getSearchCriteria(lotto.name);
				List<String> searchList = new ArrayList<String>();
				for (SearchCriteria criteria : searchCriteriaList) {
					searchList.add(criteria.drawDate + " - " + criteria.drawDay);
				}
				String[] searchValues = new String[searchList.size()];
				searchList.toArray(searchValues);
				ArrayAdapter<String> selections = new ArrayAdapter<String>(Results.this, android.R.layout.simple_spinner_item, searchValues);
				dateSelector.setAdapter(selections);
				dateSelector.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {

						try {
							int index = dateSelector.getSelectedItemPosition();
							selectedCriteria = searchCriteriaList.get(index);
							Log.d(CLASS_TAG, "Selected draw no: " + selectedCriteria.drawNo);

							if (selectedCriteria == null)
								return;

							Result4D result = lottoMessenger.getResult4DbyDrawNo(lotto.name, selectedCriteria.drawNo);

							if (result == null)
								return;

							firstPrize.setText(result.firstPrize);
							secondPrize.setText(result.secondPrize);
							thirdPrize.setText(result.thirdPrize);

							special1.setText(result.specialNumbers.get(0));
							special2.setText(result.specialNumbers.get(1));
							special3.setText(result.specialNumbers.get(2));
							special4.setText(result.specialNumbers.get(3));
							special5.setText(result.specialNumbers.get(4));
							special6.setText(result.specialNumbers.get(5));
							special7.setText(result.specialNumbers.get(6));
							special8.setText(result.specialNumbers.get(7));
							special9.setText(result.specialNumbers.get(8));
							special10.setText(result.specialNumbers.get(9));

							consolation1.setText(result.consolationNumbers.get(0));
							consolation2.setText(result.consolationNumbers.get(1));
							consolation3.setText(result.consolationNumbers.get(2));
							consolation4.setText(result.consolationNumbers.get(3));
							consolation5.setText(result.consolationNumbers.get(4));
							consolation6.setText(result.consolationNumbers.get(5));
							consolation7.setText(result.consolationNumbers.get(6));
							consolation8.setText(result.consolationNumbers.get(7));
							consolation9.setText(result.consolationNumbers.get(8));
							consolation10.setText(result.consolationNumbers.get(9));

						} catch (Exception ex) {
							Log.e(CLASS_TAG, "Error retrieving results: " + ex.getMessage());
						}
					}

					public void onNothingSelected(final AdapterView<?> parent) {
					}

				});
			} catch (Exception e) {
				Log.e(CLASS_TAG, "Failed to initialize screen: " + e.getMessage());
			}
		}
	}
}
