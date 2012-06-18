package com.mylotto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * My Bet
 * 
 * @author MEKOH
 * 
 */
public final class MyBet extends BaseActivity {

	private TextView empty;

	private final String[] intents = { "com.mylotto.VIEW_NUMBER_ANALYSIS", "com.mylotto.VIEW_PREDICTION_FREQUENCY", "com.mylotto.VIEW_PREDICTION_HOT_COLD" };

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybet);
		this.empty = (TextView) findViewById(R.id.empty);

		String[] mybets = getResources().getStringArray(R.array.mybet);

		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setEmptyView(this.empty);
		listView.setClickable(true);
		listView.setOnItemClickListener(itemListener);

		// By using setAdapter method in listview we an add string array in
		// list.
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mybets));
	}

	public OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			// Display the results
			Intent intent = new Intent(intents[position]);
			startActivity(intent);
		}

	};

}
