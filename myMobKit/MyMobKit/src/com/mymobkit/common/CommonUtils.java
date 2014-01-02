package com.mymobkit.common;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.mymobkit.app.MyMobKitApp;
import com.mymobkit.config.AppConfig;

/**
 * Random utility functions.
 * 
 */
public class CommonUtils {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ie) {
			throw new AssertionError(ie);
		}
	}

	public static byte[] getBytes(String fromString) {
		try {
			return fromString.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}

	public static String getString(byte[] fromBytes) {
		try {
			return new String(fromBytes, "UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}

	public static String getSecret(int size) {
		try {
			byte[] secret = new byte[size];
			SecureRandom.getInstance("SHA1PRNG").nextBytes(secret);
			return Base64.encodeBytes(secret);
		} catch (NoSuchAlgorithmException nsae) {
			throw new AssertionError(nsae);
		}
	}

	public static boolean isEmpty(String value) {
		return (value == null || value.trim().length() == 0);
	}

	public static boolean isEmpty(CharSequence value) {
		return value == null || isEmpty(value.toString());
	}

	public static boolean isEmpty(Editable value) {
		return value == null || isEmpty(value.toString());
	}

	public static void showAlertDialog(Context context, String title, String message) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setPositiveButton(android.R.string.ok, null);
		dialog.show();
	}

	// XXX-S The consumers of these are way way down in the audio/microphone
	// code.
	// Is it possible to refactor them so that they bubble up their errors in a
	// way
	// that's a little cleaner than reaching back up from all the way down
	// there?
	public static void dieWithError(int msgId) {
		Log.d(AppConfig.LOG_TAG_APP, "Dying with error.");
	}

	public static void dieWithError(Exception e) {
		Log.w(AppConfig.LOG_TAG_APP, e);
	}
}
