package com.mylotto.data;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mylotto.R;
import com.mylotto.helper.Constants;

/**
 * ListView adapter
 * 
 * @author MEKOH
 *
 */
public class LottoAdapter extends BaseAdapter {

	private static final String CLASS_TAG = LottoAdapter.class.getSimpleName();

	private final Context context;
	private final List<Lotto> lottos;
	private static LayoutInflater inflater = null;
	
	/**
	 * Constructor
	 * 
	 * @param context
	 * @param lottos
	 */
	public LottoAdapter(Context context, List<Lotto> lottos) {
		this.context = context;
		this.lottos = lottos;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Log.v(Constants.LOG_TAG, " " + CLASS_TAG + " lotto size - "
				+ this.lottos.size());
	}

	public int getCount() {
		return this.lottos.size();
	}

	public Object getItem(int position) {
		return this.lottos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Lotto lotto = this.lottos.get(position);
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.item, null);

		TextView text = (TextView) vi.findViewById(R.id.text);
		text.setText(lotto.name);
		ImageView image = (ImageView) vi.findViewById(R.id.image);
		image.setImageResource(Constants.IMAGE_RESOURCES.get(lotto.imageName));
		return vi;

	}	
}
