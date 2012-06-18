package com.mylotto.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mylotto.data.Lotto;
import com.mylotto.data.PrizeType;
import com.mylotto.data.Result4D;
import com.mylotto.data.SearchCriteria;

/**
 * Database helper class.
 * 
 * @author MEKOH
 * 
 */
public final class DbHelper {
	public static final String DB_NAME = "lotto.db";
	public static final int DB_VERSION = 3;

	private static final String CLASS_NAME = DbHelper.class.getSimpleName();

	public static final String DB_TABLE_COUNTRY = "country";
	public static final String DB_TABLE_LOTTO = "lotto";
	public static final String DB_TABLE_MAGNUM = "magnum";
	public static final String DB_TABLE_TOTO = "toto";
	public static final String DB_TABLE_DAMACAI = "damacai";

	public static final String DB_COL_ID = "_id";
	public static final String DB_COL_NAME = "name";
	public static final String DB_COL_COUNTRY_ID = "country_id";
	public static final String DB_COL_IMAGE_NAME = "image_name";

	public static final String DB_COL_DRAW_NO = "draw_no";
	public static final String DB_COL_DRAW_DATE = "draw_date";
	public static final String DB_COL_DRAW_DAY = "draw_day";
	public static final String DB_COL_PRIZE_TYPE = "prize_type";
	public static final String DB_COL_MATCHED_NO = "matched_no";

	private static final String[] COLS_COUNTRY = new String[] { DB_COL_ID, DB_COL_NAME };

	private static final String[] COLS_LOTTO = new String[] { DB_COL_ID, DB_COL_COUNTRY_ID, DB_COL_NAME, DB_COL_IMAGE_NAME };

	private static final String[] COLS_RESULTS_4D = new String[] { DB_COL_ID, DB_COL_DRAW_NO, DB_COL_DRAW_DATE, DB_COL_DRAW_DAY, DB_COL_PRIZE_TYPE, DB_COL_MATCHED_NO };

	private SQLiteDatabase db;
	private final DbOpenHelper dbOpenHelper;

	private boolean isReadOnly = false;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public DbHelper(final Context context) {
		this.isReadOnly = false;
		this.dbOpenHelper = DbOpenHelper.getInstance(context, DB_NAME, isReadOnly);
		establishDb();
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public DbHelper(final Context context, final boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
		this.dbOpenHelper = DbOpenHelper.getInstance(context, DB_NAME, isReadOnly);
		establishDb();

	}

	private void establishDb() {
		if (this.db == null) {
			if (isReadOnly) {
				this.db = this.dbOpenHelper.getReadableDatabase();
			} else {
				this.db = this.dbOpenHelper.getWritableDatabase();
			}
		}
	}

	public void cleanUp() {
		if (this.db != null) {
			this.db.close();
			this.db = null;
		}
	}

	/**
	 * Insert 4D result to the table.
	 * 
	 * @param tableName
	 *            Target table
	 * @param result
	 *            4D result
	 */
	public void insert4D(final String tableName, final Result4D result) {
		this.db.beginTransaction();
		try {
			ContentValues values = new ContentValues();

			result.drawDay = DateUtils.getWeekdayName(result.drawDate);

			// Common values
			values.put(DB_COL_DRAW_NO, result.drawNo);
			values.put(DB_COL_DRAW_DAY, result.drawDay);
			values.put(DB_COL_DRAW_DATE, DateUtils.formatDate(result.drawDate));

			// Insert first prize
			values.put(DB_COL_PRIZE_TYPE, PrizeType.FIRST_PRIZE);
			values.put(DB_COL_MATCHED_NO, result.firstPrize);
			this.db.insert(tableName, null, values);

			// Insert second prize
			values.put(DB_COL_PRIZE_TYPE, PrizeType.SECOND_PRIZE);
			values.put(DB_COL_MATCHED_NO, result.secondPrize);
			this.db.insert(tableName, null, values);

			// Insert third prize
			values.put(DB_COL_PRIZE_TYPE, PrizeType.THIRD_PRIZE);
			values.put(DB_COL_MATCHED_NO, result.thirdPrize);
			this.db.insert(tableName, null, values);

			// Insert special
			values.put(DB_COL_PRIZE_TYPE, PrizeType.SPECIAL);
			for (String no : result.specialNumbers) {
				values.put(DB_COL_MATCHED_NO, no);
				this.db.insert(tableName, null, values);
			}

			// Insert consolation
			values.put(DB_COL_PRIZE_TYPE, PrizeType.CONSOLATION);
			for (String no : result.consolationNumbers) {
				values.put(DB_COL_MATCHED_NO, no);
				this.db.insert(tableName, null, values);
			}

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void updateLotto(final Lotto lotto) {
		ContentValues values = new ContentValues();

		/*
		 * values.put("zip", location.zip); values.put("city", location.city);
		 * values.put("region", location.region); values.put("lastalert",
		 * location.lastalert); values.put("alertenabled",
		 * location.alertenabled); this.db.update(DbHelper.DB_TABLE, values,
		 * "_id=" + location.id, null);
		 */
	}

	public void deleteLotto(final long id) {
		this.db.delete(DbHelper.DB_TABLE_LOTTO, "_id=" + id, null);
	}

	public void deleteLottoByDrawDate(final String tableName, final Date drawDate) {
		this.db.delete(tableName, "draw_date='" + DateUtils.formatDate(drawDate) + "'", null);
	}

	public Lotto get(final String zip) {
		Cursor c = null;
		Lotto lotto = null;

		/*
		 * try { c = this.db.query(true, DbHelper.DB_TABLE, DbHelper.COLS,
		 * "zip = '" + zip + "'", null, null, null, null, null); if
		 * (c.getCount() > 0) { c.moveToFirst(); location = new Location();
		 * location.id = c.getLong(0); location.zip = c.getString(1);
		 * location.city = c.getString(2); location.region = c.getString(3);
		 * location.lastalert = c.getLong(4); location.alertenabled =
		 * c.getInt(5); } } catch (SQLException e) { Log.v(Constants.LOG_TAG,
		 * DbHelper.CLASSNAME, e); } finally { if (c != null && !c.isClosed()) {
		 * c.close(); } }
		 */
		return lotto;
	}

	public List<Lotto> getLottoByCountryId(final String countryId) {
		ArrayList<Lotto> ret = new ArrayList<Lotto>();
		Cursor c = null;
		try {
			c = this.db.query(DbHelper.DB_TABLE_LOTTO, DbHelper.COLS_LOTTO, "country_id=" + countryId, null, null, null, null);
			int numRows = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				Lotto lotto = new Lotto();
				lotto.id = c.getLong(0);
				lotto.countryId = c.getLong(1);
				lotto.name = c.getString(2);
				lotto.imageName = c.getString(3);
				ret.add(lotto);
				c.moveToNext();
			}
			c.close();
			c = null;
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, DbHelper.CLASS_NAME, e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				c = null;
			}
		}
		return ret;
	}

	/**
	 * Get 4D result by the passed in criteria
	 * 
	 * @param tableName
	 * @param criteria
	 * @return
	 */
	public Result4D getResult4DWithCriteria(final String tableName, final String criteria) {
		Result4D result4D = new Result4D();
		Cursor c = null;
		try {
			c = this.db.query(tableName, DbHelper.COLS_RESULTS_4D, criteria, null, null, null, null);
			int numRows = c.getCount();
			c.moveToFirst();
			// Get common information
			if (c.getCount() > 0) {
				result4D.id = c.getLong(0);
				result4D.drawNo = c.getString(1);
				result4D.drawDate = DateUtils.parseDate(c.getString(2));
				result4D.drawDay = c.getString(3);
			}
			String prizeType;
			for (int i = 0; i < numRows; ++i) {
				// Get the prize type and add to the results
				prizeType = c.getString(4);
				if (PrizeType.FIRST_PRIZE.equals(prizeType)) {
					result4D.firstPrize = c.getString(5);
				} else if (PrizeType.SECOND_PRIZE.equals(prizeType)) {
					result4D.secondPrize = c.getString(5);
				} else if (PrizeType.THIRD_PRIZE.equals(prizeType)) {
					result4D.thirdPrize = c.getString(5);
				} else if (PrizeType.SPECIAL.equals(prizeType)) {
					result4D.specialNumbers.add(c.getString(5));
				} else if (PrizeType.CONSOLATION.equals(prizeType)) {
					result4D.consolationNumbers.add(c.getString(5));
				}
				c.moveToNext();
			}

			c.close();
			c = null;
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, DbHelper.CLASS_NAME, e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				c = null;
			}
		}
		return result4D;
	}

	/**
	 * Get 4D result by draw no
	 * 
	 * @param tableName
	 * @param drawNo
	 * @return
	 */
	public Result4D getResult4DByDrawNo(final String tableName, final String drawNo) {
		String criteria = "draw_no='" + drawNo + "'";
		return getResult4DWithCriteria(tableName, criteria);
	}

	/**
	 * Get 4D result by draw date
	 * 
	 * @param tableName
	 * @param lottoNo
	 * @return
	 */
	public Result4D getResult4DByDrawDate(final String tableName, final Date drawDate) {
		String criteria = "draw_date='" + DateUtils.formatDate(drawDate) + "'";
		return getResult4DWithCriteria(tableName, criteria);
	}

	/**
	 * Get search criteria for a particular lotto
	 * 
	 * @param tableName
	 * @param lottoNo
	 * @return
	 */
	public List<SearchCriteria> getDrawSearchCriteria(final String tableName) {
		List<SearchCriteria> searchCriteriaList = new ArrayList<SearchCriteria>();
		Cursor c = null;
		try {
			String sql = "select distinct draw_date, draw_day, draw_no from " + tableName + " order by draw_date desc";
			c = this.db.rawQuery(sql, null);
			int numRows = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				SearchCriteria criteria = new SearchCriteria();
				criteria.drawDate = c.getString(0);
				criteria.drawDay = c.getString(1);
				criteria.drawNo = c.getString(2);
				searchCriteriaList.add(criteria);
				c.moveToNext();
			}
			c.close();
			c = null;
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, DbHelper.CLASS_NAME, e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				c = null;
			}
		}
		return searchCriteriaList;
	}

	/**
	 * Get all distinct draw dates
	 * 
	 * @param tableName
	 * @param lottoNo
	 * @return
	 */
	public List<Date> getAllDrawDates(final String tableName) {
		List<Date> drawDates = new ArrayList<Date>();
		Cursor c = null;
		try {
			String sql = "select distinct draw_date from " + tableName + " order by draw_date desc";
			c = this.db.rawQuery(sql, null);
			int numRows = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				Date drawDate = DateUtils.parseDate(c.getString(0));
				drawDates.add(drawDate);
				c.moveToNext();
			}
			c.close();
			c = null;
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, DbHelper.CLASS_NAME, e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				c = null;
			}
		}
		return drawDates;
	}

	/**
	 * Get the latest draw date from the lotto table
	 * 
	 * @param tableName
	 *            Lotto table name
	 * @return Latest draw date
	 */
	public Result4D getLatest4DResult(final String tableName) {
		Result4D result4D = new Result4D();
		Cursor c = null;
		try {
			String sql = "select max(draw_date) from " + tableName;
			c = this.db.rawQuery(sql, null);
			int numRows = c.getCount();
			c.moveToFirst();
			Date latestDrawDate = null;
			if (numRows > 0)
				for (int i = 0; i < numRows; ++i) {
					latestDrawDate = DateUtils.parseDate(c.getString(0));
				}
			c.close();
			c = null;

			if (latestDrawDate != null) {
				// Get the latest result by draw date
				return getResult4DByDrawDate(tableName, result4D.drawDate);
			}
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, DbHelper.CLASS_NAME, e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				c = null;
			}
		}
		/*
		 * if (drawDate != null) { return Utils.parseDate(drawDate); }
		 */
		return result4D;
	}

	/**
	 * Get draw history
	 * 
	 * @param noOfDraw
	 * @return
	 */
	public List<Result4D> getPastDraws(final String tableName, final int noOfDraw) {
		String sql = String
				.format("select _id, draw_no, draw_date, draw_day,prize_type, matched_no from %s where draw_date in (select distinct draw_date from %s order by draw_date desc limit %s) order by draw_date desc",
						tableName, tableName, String.valueOf(noOfDraw));
		Cursor c = null;
		try {
			c = this.db.rawQuery(sql, null);
			List<Result4D> results = getResult4DRows(c);
			c.close();
			c = null;
			return results;
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, DbHelper.CLASS_NAME, e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				c = null;
			}
		}
		return new ArrayList<Result4D>();
	}
	
	/**
	 * Get past draw results by date ranges.
	 * 
	 * @param tableName
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<Result4D> getPastDrawsByDates(final String tableName, final String fromDate, final String toDate) {
		String sql = String
				.format("select _id, draw_no, draw_date, draw_day,prize_type, matched_no from %s where draw_date between '%s' and '%s' order by draw_date desc",
						tableName, fromDate, toDate);
		Cursor c = null;
		try {
			c = this.db.rawQuery(sql, null);
			List<Result4D> results = getResult4DRows(c);
			c.close();
			c = null;
			return results;
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, DbHelper.CLASS_NAME, e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				c = null;
			}
		}
		return new ArrayList<Result4D>();
	}
	
	/**
	 * Get past draw results greater than from date.
	 * 
	 * @param tableName
	 * @param fromDate
	 * @return
	 */
	public List<Result4D> getPastDrawsAfterDate(final String tableName, final String fromDate) {
		String sql = String
				.format("select _id, draw_no, draw_date, draw_day,prize_type, matched_no from %s where draw_date > '%s' order by draw_date desc",
						tableName, fromDate);
		Cursor c = null;
		try {
			c = this.db.rawQuery(sql, null);
			List<Result4D> results = getResult4DRows(c);
			c.close();
			c = null;
			return results;
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, DbHelper.CLASS_NAME, e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				c = null;
			}
		}
		return new ArrayList<Result4D>();
	}
	
	
	/**
	 * Get all 4D results records
	 * @param c
	 * @return
	 */
	private List<Result4D> getResult4DRows(final Cursor c){
		List<Result4D> results = new ArrayList<Result4D>();
		if (c == null) return results;
		int numRows = c.getCount();
		c.moveToFirst();
		if (numRows > 0) {
			String drawNo = StringUtils.EMPTY;
			Result4D result4D = null;
			for (int i = 0; i < numRows; ++i) {
				if (!drawNo.equals(c.getString(1))) {
					result4D = new Result4D();
					drawNo = c.getString(1);
					result4D.id = c.getLong(0);
					result4D.drawNo = c.getString(1);
					result4D.drawDate = DateUtils.parseDate(c.getString(2));
					result4D.drawDay = c.getString(3);
					results.add(result4D);
				}

				// Get the prize type and add to the results
				String prizeType = c.getString(4);
				if (PrizeType.FIRST_PRIZE.equals(prizeType)) {
					result4D.firstPrize = c.getString(5);
				} else if (PrizeType.SECOND_PRIZE.equals(prizeType)) {
					result4D.secondPrize = c.getString(5);
				} else if (PrizeType.THIRD_PRIZE.equals(prizeType)) {
					result4D.thirdPrize = c.getString(5);
				} else if (PrizeType.SPECIAL.equals(prizeType)) {
					result4D.specialNumbers.add(c.getString(5));
				} else if (PrizeType.CONSOLATION.equals(prizeType)) {
					result4D.consolationNumbers.add(c.getString(5));
				}
				c.moveToNext();
			}
		}
		return results;
	}
}
