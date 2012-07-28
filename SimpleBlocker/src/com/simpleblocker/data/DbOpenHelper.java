package com.simpleblocker.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/***
 * Helper singleton class to manage SQLiteDatabase Create and Restore
 * 
 */
public class DbOpenHelper extends SQLiteOpenHelper {

	private static final String CLASS_TAG = DbOpenHelper.class.getSimpleName();

	private static SQLiteDatabase sqliteDb;
	private static DbOpenHelper instance;

	private static final int DATABASE_VERSION = 1;

	// the default database path is :
	// /data/data/pkgNameOfYourApplication/databases/
	private static String DB_PATH_PREFIX = "/data/data/";
	private static String DB_PATH_SUFFIX = "/databases/";

	private Context context;

	/***
	 * Constructor
	 * 
	 * @param context
	 *            : app context
	 * @param name
	 *            : database name
	 * @param factory
	 *            : cursor Factory
	 * @param version
	 *            : DB version
	 */
	private DbOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
		Log.i(CLASS_TAG, "Create or Open database : " + name);
	}

	/***
	 * Initialize method
	 * 
	 * @param context
	 *            : application context
	 * @param databaseName
	 *            : database name
	 */
	private static void initialize(Context context, String databaseName, boolean isReadOnly) {
		if (instance == null) {

			/**
			 * Try to check if there is an Original copy of DB in asset
			 * Directory
			 */

			if (!checkDatabase(context, databaseName)) {
				// if not exists, I try to copy from asset directory
				try {
					copyDataBase(context, databaseName);
				} catch (IOException e) {
					Log.e(CLASS_TAG, "Database " + databaseName + " does not exists and there is no Original Version in Asset dir");
				}
			}

			Log.i(CLASS_TAG, "Try to create instance of database (" + databaseName + ")");

			instance = new DbOpenHelper(context, databaseName, null, DATABASE_VERSION);
			if (isReadOnly) {
				sqliteDb = instance.getReadableDatabase();
			} else {
				sqliteDb = instance.getWritableDatabase();
			}

			Log.i(CLASS_TAG, "instance of database (" + databaseName + ") created !");
		}
	}

	/***
	 * Static method for getting singleton instance
	 * 
	 * @param context
	 *            : application context
	 * @param databaseName
	 *            : database name
	 * @return : singleton instance
	 */
	public static final DbOpenHelper getInstance(Context context, String databaseName, boolean isReadOnly) {
		initialize(context, databaseName, isReadOnly);
		return instance;
	}

	/***
	 * Method to get database instance
	 * 
	 * @return database instance
	 */
	public SQLiteDatabase getDatabase() {
		return sqliteDb;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(CLASS_TAG, "onCreate : nothing to do");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(CLASS_TAG, "onCreate : nothing to do");

	}

	/***
	 * Method for Copy the database from asset directory to application's data
	 * directory
	 * 
	 * @param databaseName
	 *            : database name
	 * @throws IOException
	 *             : exception if file does not exists
	 */
	private void copyDataBase(String databaseName) throws IOException {
		copyDataBase(context, databaseName);
	}

	/***
	 * Static method for copy the database from asset directory to application's
	 * data directory
	 * 
	 * @param aContext
	 *            : application context
	 * @param databaseName
	 *            : database name
	 * @throws IOException
	 *             : exception if file does not exists
	 */
	private static void copyDataBase(Context aContext, String databaseName) throws IOException {

		// Open your local db as the input stream
		InputStream myInput = aContext.getAssets().open(databaseName);

		// Path to the just created empty db
		String outFileName = getAbsolutePathToDatabase(aContext, databaseName);

		Log.i(CLASS_TAG, "Check if create dir : " + getDatabaseFolder(aContext));

		// if the path doesn't exist first, create it
		File f = new File(getDatabaseFolder(aContext));
		if (!f.exists())
			f.mkdir();

		Log.i(CLASS_TAG, "Trying to copy local DB to : " + outFileName);

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the input file to the output file
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

		Log.i(CLASS_TAG, "DB (" + databaseName + ") copied!");
	}

	/***
	 * Method to check if database exists in application's data directory
	 * 
	 * @param databaseName
	 *            : database name
	 * @return : boolean (true if exists)
	 */
	public boolean checkDatabase(String databaseName) {
		return checkDatabase(context, databaseName);
	}

	/***
	 * Static Method to check if database exists in application's data directory
	 * 
	 * @param aContext
	 *            : application context
	 * @param databaseName
	 *            : database name
	 * @return : boolean (true if exists)
	 */
	public static boolean checkDatabase(Context aContext, String databaseName) {
		SQLiteDatabase checkDB = null;

		try {
			String myPath = getAbsolutePathToDatabase(aContext, databaseName);

			Log.i(CLASS_TAG, "Trying to connect to : " + myPath);

			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

			Log.i(CLASS_TAG, "Database " + databaseName + " found!");

			checkDB.close();
		} catch (SQLiteException e) {
			Log.i(CLASS_TAG, "Database " + databaseName + " does not exists!");

		}

		return checkDB != null ? true : false;
	}

	/***
	 * Method that returns database path in the application's data directory
	 * 
	 * @param databaseName
	 *            : database name
	 * @return : complete path
	 */
	private String getAbsolutePathToDatabase(String databaseName) {
		return getAbsolutePathToDatabase(context, databaseName);
	}

	/***
	 * Static Method that returns database path in the application's data
	 * directory
	 * 
	 * @param aContext
	 *            : application context
	 * @param databaseName
	 *            : database name
	 * @return : complete path
	 */
	private static String getAbsolutePathToDatabase(Context aContext, String databaseName) {

		return getDatabaseFolder(aContext) + databaseName;
	}

	/**
	 * Get the path to the database folder
	 * 
	 * @param aContext
	 * @return
	 */
	private static String getDatabaseFolder(Context aContext) {
		/*
		 * String state = Environment.getExternalStorageState(); if
		 * (Environment.MEDIA_MOUNTED.equals(state) &&
		 * !Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) { File extStorage
		 * = Environment.getExternalStorageDirectory(); return
		 * extStorage.getAbsolutePath() + File.separator +
		 * aContext.getPackageName() + File.separator; }
		 */
		return DB_PATH_PREFIX + aContext.getPackageName() + DB_PATH_SUFFIX;
	}

}