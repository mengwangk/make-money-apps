package com.mylotto;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mylotto.data.Lotto;
import com.mylotto.data.LottoAdapter;
import com.mylotto.data.Prefs;
import com.mylotto.helper.Constants;

/**
 * Display lotto results.
 * 
 * @author MEKOH
 *
 */
public final class LottoList extends BaseActivity {

	private static final String CLASS_TAG = LottoList.class.getSimpleName();

	private TextView empty;
	//private ProgressDialog progressDialog;
	private LottoAdapter lottoAdapter;
	private List<Lotto> lottos;
	private Prefs prefs;

	/**
	 * Handler to display the lotto list
	 */
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			Log.v(Constants.LOG_TAG, " " + CLASS_TAG
					+ " worker thread done, setup LottoAdapter");
			//progressDialog.dismiss();
			if ((lottos == null) || (lottos.size() == 0)) {
				empty.setText(R.string.no_data);
			} else {
				// set list properties
				final ListView listView = (ListView) findViewById(R.id.list);
				lottoAdapter = new LottoAdapter(LottoList.this, lottos);
				listView.setAdapter(lottoAdapter);
			}
		}
	};

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lottolist);

		this.prefs = new Prefs(getApplicationContext());
		this.empty = (TextView) findViewById(R.id.empty);

		// set list properties
		final ListView listView = (ListView) findViewById(R.id.list);//getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setEmptyView(this.empty);
		listView.setClickable(true);
		listView.setOnItemClickListener(itemListener);

		// Start the service
		this.startService();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(Constants.LOG_TAG, " " + CLASS_TAG + " onResume");
		loadLottos();
	}

	public OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view,
				final int position, final long id) {
			Lotto selectedLotto = lottos.get(position);
			Log.d(CLASS_TAG, "Selected lotto: " + selectedLotto.name);

			// set the current lotto to the Application (global state placed there)
			MyLottoApplication application = (MyLottoApplication) getApplication();
			application.setSelectedLotto(selectedLotto);

			// Display the results
			Intent intent = new Intent(Constants.INTENT_ACTION_VIEW_RESULTS);
			startActivity(intent);
		}

	};

	/**
	 * Load the lotto list
	 * 
	 */
	private void loadLottos() {
		Log.v(Constants.LOG_TAG, " " + CLASS_TAG + " loadLottos");

		//this.progressDialog = ProgressDialog.show(this,
		//		getString(R.string.working), getString(R.string.retrieving),
		//		true, false);

		// get lottos in a separate thread for ProgressDialog/Handler
		// when complete send "empty" message to handler
		new Thread() {
			@Override
			public void run() {
				MyLottoApplication application = (MyLottoApplication) getApplication();
				lottos = application.getAvailableLottos();
				handler.sendEmptyMessage(0);
			}
		}.start();
	}
}
