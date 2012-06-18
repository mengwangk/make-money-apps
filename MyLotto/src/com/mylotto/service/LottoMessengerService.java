package com.mylotto.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.mylotto.data.Result4D;
import com.mylotto.data.SearchCriteria;
import com.mylotto.helper.Constants.SyncStatus;
import com.mylotto.helper.DbHelper;

/**
 * Provide the APIs so that the service can be accessed remotely.
 * 
 * @author MEKOH
 * 
 */
public class LottoMessengerService extends LottoService {

	private static final String CLASS_TAG = LottoMessengerService.class.getSimpleName();

	private final class LottoMessenger extends ILottoMessenger.Stub {

		@Override
		public boolean startSynch() throws RemoteException {
			
			/*
			MyLottoApplication application = (MyLottoApplication) getApplication();
			if (application.getSynchronizedFlag()) {	
				return true;	// Already synchronized
			}
			*/
			
			if (syncStatus == SyncStatus.IN_PROGRESS || 
				syncStatus == SyncStatus.SUCCESS)
				return true;
			
			new Thread() {
				@Override
				public void run() {
					downloadResults();
				}
			}.start();
			return true;
		}

		@Override
		public boolean stopSync() throws RemoteException {
			return false;
		}

		@Override
		public Result4D getResult4DbyDrawNo(String name, String drawNo) throws RemoteException {
			DbHelper dbHelper = new DbHelper(getApplicationContext());
			try {
				return dbHelper.getResult4DByDrawNo(name, drawNo);
			} catch (Exception ex) {
				Log.e(CLASS_TAG, "Unable to retrieve results using provided draw no [" + ex.getMessage() + "]");
			} finally {
				if (dbHelper != null) {
					dbHelper.cleanUp();
					dbHelper = null;
				}
			}
			return new Result4D();
		}
		
		@Override
		public List<Result4D> getPastResults(String name, int noOfDraw) throws RemoteException {
			DbHelper dbHelper = new DbHelper(getApplicationContext());
			try {
				return dbHelper.getPastDraws(name, noOfDraw);
			} catch (Exception ex) {
				Log.e(CLASS_TAG, "Unable to retrieve past results [" + ex.getMessage() + "]");
			} finally {
				if (dbHelper != null) {
					dbHelper.cleanUp();
					dbHelper = null;
				}
			}
			return new ArrayList<Result4D>();
		}

		@Override
		public List<Result4D> getPastResultsByDates(String name, String fromDate, String toDate) throws RemoteException {
			DbHelper dbHelper = new DbHelper(getApplicationContext());
			try {
				return dbHelper.getPastDrawsByDates(name, fromDate, toDate);
			} catch (Exception ex) {
				Log.e(CLASS_TAG, "Unable to retrieve past results [" + ex.getMessage() + "]");
			} finally {
				if (dbHelper != null) {
					dbHelper.cleanUp();
					dbHelper = null;
				}
			}
			return new ArrayList<Result4D>();
		}
	
		@Override
		public List<Result4D> getPastResultsAfterDate(String name, String fromDate) throws RemoteException {
			DbHelper dbHelper = new DbHelper(getApplicationContext());
			try {
				return dbHelper.getPastDrawsAfterDate(name, fromDate);
			} catch (Exception ex) {
				Log.e(CLASS_TAG, "Unable to retrieve past results [" + ex.getMessage() + "]");
			} finally {
				if (dbHelper != null) {
					dbHelper.cleanUp();
					dbHelper = null;
				}
			}
			return new ArrayList<Result4D>();
		}
		
		@Override
		public List<SearchCriteria> getSearchCriteria(String name) throws RemoteException {
			DbHelper dbHelper = new DbHelper(getApplicationContext());
			try {
				return dbHelper.getDrawSearchCriteria(name);
			} catch (Exception ex) {
				Log.e(CLASS_TAG, "Unable to retrieve draw search criteria [" + ex.getMessage() + "]");
			} finally {
				if (dbHelper != null) {
					dbHelper.cleanUp();
					dbHelper = null;
				}
			}
			return new ArrayList<SearchCriteria>();
		}

		@Override
		public int getStatus() throws RemoteException {
			return syncStatus.ordinal();
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		return new LottoMessenger();
	}
}
