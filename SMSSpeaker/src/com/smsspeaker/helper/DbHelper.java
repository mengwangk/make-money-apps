package com.smsspeaker.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.smsspeaker.InboundData;

/**
 * Database helper class.
 * 
 * @author MEKOH
 * 
 */
public final class DbHelper {
	public static final String DB_NAME = "data.db";
	public static final int DB_VERSION = 3;

	private static final String CLASS_NAME = DbHelper.class.getSimpleName();

	public static final String DB_TABLE_INBOUND_DATA = "inbound_data";

	public static final String DB_COL_ID = "_id";
	public static final String DB_COL_SUBJECT = "subject";
	public static final String DB_COL_DETAILS = "details";
	public static final String DB_COL_TYPE = "type";
	public static final String DB_COL_TIMESTAMP = "timestamp";

	private static final String[] COLS_INBOUND_DATA = new String[] { DB_COL_ID, DB_COL_SUBJECT, DB_COL_DETAILS, DB_COL_TYPE, DB_COL_TIMESTAMP };

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

	public void insertInboundData(final InboundData data) {
		this.db.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			values.put(DB_COL_SUBJECT, data.subject);
			values.put(DB_COL_DETAILS, data.details);
			values.put(DB_COL_TYPE, data.type);
			values.put(DB_COL_TIMESTAMP, DateUtils.formatDate(data.timestamp));
			this.db.insert(DB_TABLE_INBOUND_DATA, null, values);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public List<InboundData> getAllInboundData() {
		ArrayList<InboundData> ret = new ArrayList<InboundData>();
		Cursor c = null;
		try {
			c = this.db.query(DbHelper.DB_TABLE_INBOUND_DATA, DbHelper.COLS_INBOUND_DATA, null, null, null, null, DB_COL_TIMESTAMP);
			int numRows = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				InboundData data = new InboundData();
				data.id = c.getLong(0);
				data.subject = c.getString(1);
				data.details = c.getString(2);
				data.type = c.getString(3);
				data.timestamp = DateUtils.parseDate(c.getString(4));
				ret.add(data);
				c.moveToNext();
			}
			c.close();
			c = null;
		} catch (SQLException e) {
			Log.e(DbHelper.CLASS_NAME, e.getMessage(), e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				c = null;
			}
		}
		return ret;
	}

	public void deleteAllInboundData() {
		this.db.delete(DbHelper.DB_TABLE_INBOUND_DATA, null, null);
	}

}
