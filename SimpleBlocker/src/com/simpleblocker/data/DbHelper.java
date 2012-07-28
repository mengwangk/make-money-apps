package com.simpleblocker.data;

import java.util.ArrayList;
import java.util.List;

import com.simpleblocker.R;
import com.simpleblocker.data.models.BlockedCallLog;
import com.simpleblocker.data.models.CallLog;
import com.simpleblocker.data.models.Contact;
import com.simpleblocker.data.models.EmptyContact;
import com.simpleblocker.data.models.Phone;
import com.simpleblocker.utils.AppConfig;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

/**
 * Database helper class.
 * 
 * @author MEKOH
 * 
 */
public final class DbHelper {

	public static final String DB_NAME = "sb.db";
	public static final int DB_VERSION = 3;

	private static final String CLASS_NAME = DbHelper.class.getSimpleName();

	public static final String DB_TABLE_CONTACT = "contact";
	public static final String DB_TABLE_PHONE = "phone";
	public static final String DB_TABLE_CALL_LOG = "call_log";

	public static final String DB_COL_ID = "id";
	public static final String DB_COL_CONTACT_ID = "contact_id";
	public static final String DB_COL_CONTACT_NAME = "contact_name";

	public static final String DB_COL_PHONE_PHONE_NO = "phone_no";
	public static final String DB_COL_PHONE_PHONE_TYPE = "phone_type";
	public static final String DB_COL_PHONE_FK_C_ID = "c_id";
	public static final String DB_COL_IS_WILDCARD_MATCH = "is_wildcard_match";

	public static final String DB_COL_TIMESTAMP = "timestamp";

	private static final String[] COLS_CONTACT = new String[] { DB_COL_ID, DB_COL_CONTACT_ID, DB_COL_CONTACT_NAME };
	private static final String[] COLS_PHONE = new String[] { DB_COL_ID, DB_COL_PHONE_PHONE_NO, DB_COL_PHONE_PHONE_TYPE, DB_COL_PHONE_FK_C_ID,
			DB_COL_IS_WILDCARD_MATCH };
	private static final String[] COLS_CALL_LOG = new String[] { DB_COL_ID, DB_COL_CONTACT_NAME, DB_COL_PHONE_PHONE_NO, DB_COL_TIMESTAMP };

	private SQLiteDatabase db;
	private final DbOpenHelper dbOpenHelper;

	private boolean isReadOnly = false;

	private Context context;

	public static final String SEMAPHORE = "SEMAPHORE";

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
				Log.e(AppConfig.LOG_TAG, DbHelper.CLASS_NAME, e);
			} finally {
				if (c != null && !c.isClosed()) {
					c.close();
					c = null;
				}
			}
			return 0;
		}
	}

	/**
	 * Return the first contact which matches the phone number.
	 * 
	 * @param phoneNo
	 * @return
	 */
	public Contact isNumberBlocked(final String phoneNo) {
		synchronized (SEMAPHORE) {
			Cursor c = null;
			try {
				String query = "select " + qualifyColumnName(DB_TABLE_CONTACT, DB_COL_ID) + ","
						+ qualifyColumnName(DB_TABLE_CONTACT, DB_COL_CONTACT_ID) + "," + qualifyColumnName(DB_TABLE_CONTACT, DB_COL_CONTACT_NAME)
						+ "," + qualifyColumnName(DB_TABLE_PHONE, DB_COL_PHONE_PHONE_NO) + ","
						+ qualifyColumnName(DB_TABLE_PHONE, DB_COL_PHONE_PHONE_TYPE) + " from " + DB_TABLE_CONTACT + "," + DB_TABLE_PHONE + " where "
						+ qualifyColumnName(DB_TABLE_CONTACT, DB_COL_ID) + "=" + qualifyColumnName(DB_TABLE_PHONE, DB_COL_PHONE_FK_C_ID) + " and "
						+ qualifyColumnName(DB_TABLE_PHONE, DB_COL_PHONE_PHONE_NO) + "=?";
				c = this.db.rawQuery(query, new String[] { phoneNo });
				int numRows = c.getCount();
				c.moveToFirst();
				if (numRows > 0) {
					// Contact is found
					Contact contact = new Contact();
					contact.setId(c.getString(0));
					contact.setContactId(c.getString(1));
					contact.setContactName(c.getString(2));
					Phone phone = new Phone(contact, c.getString(3), c.getString(4));
					contact.getPhoneList().add(phone);
					return contact;
				}
			} catch (SQLException e) {
				Log.e(AppConfig.LOG_TAG, DbHelper.CLASS_NAME, e);
			} finally {
				if (c != null && !c.isClosed()) {
					c.close();
					c = null;
				}
			}
			return new EmptyContact();
		}
	}
	
	
	/**
	 * Return the first contact which matches the phone number.
	 * 
	 * @param phoneNo
	 * @return
	 */
	public List<Contact> getWildCardContacts() {
		synchronized (SEMAPHORE) {
			Cursor c = null;
			List<Contact> contactList = new ArrayList<Contact>(1);
			try {
				String query = "select " + qualifyColumnName(DB_TABLE_CONTACT, DB_COL_ID) + ","
						+ qualifyColumnName(DB_TABLE_CONTACT, DB_COL_CONTACT_ID) + "," + qualifyColumnName(DB_TABLE_CONTACT, DB_COL_CONTACT_NAME)
						+ "," + qualifyColumnName(DB_TABLE_PHONE, DB_COL_PHONE_PHONE_NO) + ","
						+ qualifyColumnName(DB_TABLE_PHONE, DB_COL_PHONE_PHONE_TYPE) + " from " + DB_TABLE_CONTACT + "," + DB_TABLE_PHONE + " where "
						+ qualifyColumnName(DB_TABLE_CONTACT, DB_COL_ID) + "=" + qualifyColumnName(DB_TABLE_PHONE, DB_COL_PHONE_FK_C_ID) + " and "
						+ qualifyColumnName(DB_TABLE_PHONE, DB_COL_IS_WILDCARD_MATCH) + "=1";
				c = this.db.rawQuery(query, null);
				int numRows = c.getCount();
				c.moveToFirst();
				if (numRows > 0) {
					// Contact is found
					Contact contact = new Contact();
					contact.setId(c.getString(0));
					contact.setContactId(c.getString(1));
					contact.setContactName(c.getString(2));
					Phone phone = new Phone(contact, c.getString(3), c.getString(4));
					contact.getPhoneList().add(phone);
					contactList.add(contact);
				}
			} catch (SQLException e) {
				Log.e(AppConfig.LOG_TAG, DbHelper.CLASS_NAME, e);
			} finally {
				if (c != null && !c.isClosed()) {
					c.close();
					c = null;
				}
			}
			return contactList;
		}
	}


	public int deleteBlockedContacts(final List<Contact> contacts) {
		synchronized (SEMAPHORE) {
			int recordCount = 0;
			this.db.beginTransaction();
			try {
				for (Contact c : contacts) {
					// Delete contact phones
					this.db.delete(DB_TABLE_PHONE, DB_COL_PHONE_FK_C_ID + "=?", new String[] { c.getId() });

					// Delete contact
					recordCount += this.db.delete(DB_TABLE_CONTACT, DB_COL_ID + "=?", new String[] { c.getId() });
				}
				db.setTransactionSuccessful();
			} catch (SQLException e) {
				Log.e(AppConfig.LOG_TAG, DbHelper.CLASS_NAME, e);
			} finally {
				db.endTransaction();
			}
			return recordCount;
		}
	}

	public int deleteBlockedCallLogs() {
		synchronized (SEMAPHORE) {
			int recordCount = 0;
			this.db.beginTransaction();
			try {
				// Delete call log
				recordCount += this.db.delete(DB_TABLE_CALL_LOG, null, null);
				db.setTransactionSuccessful();
			} catch (SQLException e) {
				Log.e(AppConfig.LOG_TAG, DbHelper.CLASS_NAME, e);
			} finally {
				db.endTransaction();
			}
			return recordCount;
		}
	}

	public List<Contact> getBlockedContacts() {
		synchronized (SEMAPHORE) {
			ArrayList<Contact> contacts = new ArrayList<Contact>(1);
			Cursor c = null;
			try {
				c = this.db.query(DbHelper.DB_TABLE_CONTACT, DbHelper.COLS_CONTACT, null, null, null, null, DB_COL_CONTACT_NAME + " ASC");
				int numRows = c.getCount();
				c.moveToFirst();
				for (int i = 0; i < numRows; ++i) {
					Contact contact = new Contact();
					contact.setId(c.getString(0));
					contact.setContactId(c.getString(1));
					contact.setContactName(c.getString(2));
					contacts.add(contact);
					c.moveToNext();
				}
				c.close();

				// Retrieve contact phones
				for (Contact contact : contacts) {
					c = this.db.query(DbHelper.DB_TABLE_PHONE, DbHelper.COLS_PHONE, DB_COL_PHONE_FK_C_ID + "=" + contact.getId(), null, null, null,
							null);
					numRows = c.getCount();
					c.moveToFirst();
					for (int i = 0; i < numRows; ++i) {
						Phone phone = new Phone(contact, c.getString(1), c.getString(2));
						contact.getPhoneList().add(phone);
						c.moveToNext();
					}
					c.close();
				}
				c = null;
			} catch (SQLException e) {
				Log.e(DbHelper.CLASS_NAME, e.getMessage(), e);
			} finally {
				if (c != null && !c.isClosed()) {
					c.close();
					c = null;
				}
			}
			return contacts;
		}
	}

	public List<BlockedCallLog> getBlockedCallLogs() {
		synchronized (SEMAPHORE) {
			List<BlockedCallLog> callLogs = new ArrayList<BlockedCallLog>(1);
			Cursor c = null;
			try {
				c = this.db.query(DbHelper.DB_TABLE_CALL_LOG, DbHelper.COLS_CALL_LOG, null, null, null, null, DB_COL_TIMESTAMP + " DESC");
				int numRows = c.getCount();
				c.moveToFirst();
				for (int i = 0; i < numRows; ++i) {
					BlockedCallLog callLog = new BlockedCallLog(c.getString(1), c.getString(2), c.getString(3));
					callLog.setId(c.getString(0));
					callLogs.add(callLog);
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
			return callLogs;
		}
	}

	public void insertBlockedContact(final Contact contact) {
		synchronized (SEMAPHORE) {
			this.db.beginTransaction();
			try {
				ContentValues values = new ContentValues();
				values.put(DB_COL_CONTACT_ID, contact.getContactId());
				values.put(DB_COL_CONTACT_NAME, contact.getContactName());
				this.db.insert(DB_TABLE_CONTACT, null, values);

				int cId = getLastIncrementId();
				for (Phone p : contact.getPhoneList()) {
					ContentValues phoneValues = new ContentValues();
					phoneValues.put(DB_COL_PHONE_PHONE_NO, p.getContactPhone());
					phoneValues.put(DB_COL_PHONE_PHONE_TYPE, p.getContactPhoneType());
					phoneValues.put(DB_COL_PHONE_FK_C_ID, cId);
					this.db.insert(DB_TABLE_PHONE, null, phoneValues);
				}
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}
	}

	public void insertBlockedContact(final CallLog callLog) {
		synchronized (SEMAPHORE) {
			this.db.beginTransaction();
			try {
				ContentValues values = new ContentValues();
				values.put(DB_COL_CONTACT_ID, callLog.getContactId());
				values.put(DB_COL_CONTACT_NAME, callLog.getContactName());
				this.db.insert(DB_TABLE_CONTACT, null, values);

				int cId = getLastIncrementId();
				ContentValues phoneValues = new ContentValues();
				phoneValues.put(DB_COL_PHONE_PHONE_NO, callLog.getPhoneNo());
				phoneValues.put(DB_COL_PHONE_PHONE_TYPE, context.getString(R.string.label_default_phone_type));
				phoneValues.put(DB_COL_PHONE_FK_C_ID, cId);
				phoneValues.put(DB_COL_IS_WILDCARD_MATCH, callLog.isWildCard());
				this.db.insert(DB_TABLE_PHONE, null, phoneValues);
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}
	}

	public void insertPhone(final Phone phone) {
		synchronized (SEMAPHORE) {
			this.db.beginTransaction();
			try {
				ContentValues values = new ContentValues();
				this.db.insert(DB_TABLE_CONTACT, null, values);
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}
	}
	
	public void insertBlockedCallLog(final BlockedCallLog callLog) {
		synchronized (SEMAPHORE) {
			this.db.beginTransaction();
			try {
				ContentValues values = new ContentValues();
				if (!TextUtils.isEmpty(callLog.getContactName()))
					values.put(DB_COL_CONTACT_NAME, callLog.getContactName());
				else
					values.put(DB_COL_CONTACT_NAME, context.getString(R.string.label_no_available));
				values.put(DB_COL_PHONE_PHONE_NO, callLog.getPhoneNo());
				values.put(DB_COL_TIMESTAMP, callLog.getTimestamp());
				this.db.insert(DB_TABLE_CALL_LOG, null, values);
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
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

	private static boolean getBoolean(String val) {
		return "1".equals(val);
	}

	private int getLastIncrementId() {
		Cursor c = null;
		try {
			c = this.db.rawQuery("select last_insert_rowid()", null);
			c.moveToFirst();
			return c.getInt(0);
		} catch (SQLException e) {
			Log.e(AppConfig.LOG_TAG, DbHelper.CLASS_NAME, e);
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
}
