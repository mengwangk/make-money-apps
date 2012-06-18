package com.mylotto;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.util.Log;

import com.mylotto.analysis.FrequencyAnalysisResults;
import com.mylotto.analysis.NumberAnalysisResults;
import com.mylotto.data.Lotto;
import com.mylotto.data.Prefs;
import com.mylotto.helper.DbHelper;
import com.mylotto.service.ILottoMessenger;

/**
 * Extend Application for global state information for an application. Access the application via
 * Activity.getApplication().
 * 
 * There are several ways to store global state information, this is one of them. Another is to
 * create a class with static members and just access it from Activities.
 * 
 * Either approach works, and there is debate about which is better. Either way, make sure to clean
 * up in life-cycle pause or destroy methods if you use resources that need cleaning up (static
 * maps, etc).
 * 
 * @author MEKOH
 * 
 */
public final class MyLottoApplication extends Application {
	
	private static final String CLASS_TAG = MyLottoApplication.class.getSimpleName();

	private Lotto selectedLotto;
	private List<Lotto> availableLottos;
	private Prefs prefs;
	private boolean isSynchronized = false;
	private ILottoMessenger lottoMessenger;
	private NumberAnalysisResults numberAnalysisResults;
	private FrequencyAnalysisResults frequencyAnalysisResults;
	

	public MyLottoApplication() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Cache information from database
		DbHelper dbHelper = new DbHelper(this, true);
		try {
			this.prefs = new Prefs(getApplicationContext());
			availableLottos = new ArrayList<Lotto>(1);				
			availableLottos = dbHelper.getLottoByCountryId(this.prefs.getCountryId());
			dbHelper.cleanUp();
			dbHelper = null;
		} catch (Exception ex){
			Log.e(CLASS_TAG,
					"Unable to load data from database [" + ex.getMessage() + "]");
		} finally {
			if (dbHelper != null){
				dbHelper.cleanUp();
				dbHelper = null;
			}
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public Lotto getSelectedLotto() {
		return this.selectedLotto;
	}

	public void setSelectedLotto(final Lotto lotto) {
		this.selectedLotto = lotto;
	}

	public List<Lotto> getAvailableLottos(){
		return this.availableLottos;
	}
	
	public void setSynchronizedFlag(boolean val){
		this.isSynchronized = val;
	}
	
	public boolean getSynchronizedFlag(){
		return this.isSynchronized;
	}
	
	public void setLottoMessenger(ILottoMessenger messenger){
		this.lottoMessenger = messenger;
	}
	
	public ILottoMessenger getLottoMessenger(){
		return this.lottoMessenger;
	}
	
	public void setNumberAnalysisResults(NumberAnalysisResults results){
		this.numberAnalysisResults = results;
	}
	
	public NumberAnalysisResults getNumberAnalysisResults(){
		return this.numberAnalysisResults;
	}
	
	public void setFrequencyAnalysisResults(FrequencyAnalysisResults results){
		this.frequencyAnalysisResults = results;
	}
	
	public FrequencyAnalysisResults getFrequencyAnalysisResults(){
		return this.frequencyAnalysisResults;
	}
	
}
