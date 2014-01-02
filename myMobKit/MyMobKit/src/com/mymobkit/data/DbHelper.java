package com.mymobkit.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mymobkit.config.AppConfig;
import com.mymobkit.model.ConfigParam;

/**
 * Database helper class.
 * 
 * @author MEKOH
 * 
 */
public final class DbHelper {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":DbHelper";

	public enum Field {

		ID("id"),
		MSG_CONTENT("msg_content"),
		MSG_TYPE("msg_type"),
		STATUS("status"),
		DATE_CREATED("date_created"),
		DATE_MODIFIED("date_modified"),
		NAME("name"),
		VALUE("value"),
		MODULE("module"),
		DESCRIPTION("description"),
		CONFIGURABLE("configurable");

		private final String id;

		private Field(String id) {

			this.id = id;
		}

		public String getId() {

			return id;
		}
	}

	public static SimpleDateFormat dateFormat() {
		return dateFormatter;
	}

	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final String DB_NAME = "mymobkit.db";
	public static final int DB_VERSION = 3;

	public static final String DB_TABLE_APPCONFIG = "AppConfig";

	private static final String[] COLS_APPCONFIG = new String[] { Field.ID.getId(), Field.NAME.getId(), Field.VALUE.getId(), Field.MODULE.getId(), Field.DESCRIPTION.getId(), Field.CONFIGURABLE.getId(), Field.DATE_CREATED.getId(), Field.DATE_MODIFIED.getId() };

	private SQLiteDatabase db;
	private final DbOpenHelper dbOpenHelper;

	private boolean isReadOnly = false;

	private final Context context;

	public static final String SEMAPHORE = "SEMAPHORE_MYMOBKIT";

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public DbHelper(final Context context) {
		this.isReadOnly = false;
		this.dbOpenHelper = DbOpenHelper.getInstance(context, DB_NAME, isReadOnly);
		this.context = context;
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
		this.context = context;
		establishDb();

	}

	private void establishDb() {
		synchronized (SEMAPHORE) {
			if (this.db == null) {
				if (isReadOnly) {
					this.db = this.dbOpenHelper.getReadableDatabase();
				} else {
					this.db = this.dbOpenHelper.getWritableDatabase();
				}
			}
		}
	}

	public void cleanUp() {
		synchronized (SEMAPHORE) {
			if (this.db != null) {
				this.db.close();
				this.db = null;
			}
		}
	}

	/**
	 * Get table record count.
	 * 
	 * @param tableName
	 * @return
	 */
	public int getCount(final String tableName) {
		synchronized (SEMAPHORE) {
			Cursor c = null;
			try {
				c = this.db.rawQuery("select count(*) from ?s", new String[] { tableName });
				c.moveToFirst();
				return c.getInt(0);
			} catch (SQLException e) {
				Log.e(TAG, "[getCount] Error getting record count", e);
			} finally {
				if (c != null && !c.isClosed()) {
					c.close();
					c = null;
				}
			}
			return 0;
		}
	}

	public void beginTransaction() {
		this.db.beginTransaction();
	}

	public void setTransactionSuccessful() {
		this.db.setTransactionSuccessful();
	}

	public void endTransaction() {
		if (this.db.inTransaction())
			this.db.endTransaction();
	}

	public static boolean getBoolean(String val) {
		return "1".equals(val) || "true".equals(val);
	}

	private int getLastIncrementId() {
		Cursor c = null;
		try {
			c = this.db.rawQuery("select last_insert_rowid()", null);
			c.moveToFirst();
			return c.getInt(0);
		} catch (SQLException e) {
			Log.e(TAG, "[getLastIncrementId] Error getting last row id", e);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				c = null;
			}
		}
		return 0;
	}

	private String qualifyColumnName(final String tableName, final String columnName) {
		return tableName + "." + columnName;
	}

	public ConfigParam getAppConfig(final String name, final String module) {
		ConfigParam config = new ConfigParam();
		synchronized (SEMAPHORE) {
			Cursor c = null;
			try {
				c = this.db.query(DbHelper.DB_TABLE_APPCONFIG, DbHelper.COLS_APPCONFIG, Field.NAME.getId()
						+ "='" + name + "' and " + Field.MODULE.getId() + "='" + module + "'", null, null, null, null);
				int numRows = c.getCount();
				c.moveToFirst();
				if (numRows > 0) {
					config.setId(c.getString(0));
					config.setName(c.getString(1));
					config.setValue(c.getString(2));
					config.setModule(c.getString(3));
					config.setDescription(c.getString(4));
					config.setConfigurable(c.getString(5));
					config.setDateCreated(c.getString(6));
					config.setDateModified(c.getString(7));
				}
				c.close();
				c = null;
			} catch (SQLException e) {
				Log.e(TAG, e.getMessage(), e);
			} finally {
				if (c != null && !c.isClosed()) {
					c.close();
					c = null;
				}
			}
			return config;
		}
	}

	public boolean updateConfigValue(final String name, final String module, final String value) {
		ContentValues values = new ContentValues();
		values.put(Field.VALUE.getId(), value);
		int row = this.db.update(DB_TABLE_APPCONFIG, values, Field.NAME.getId()
				+ "='" + name + "' and " + Field.MODULE.getId() + "='" + module + "'", null);
		return (row > 0 ? true : false);

	}

}
