package com.mymobkit.audio;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import com.mymobkit.app.MyMobKitApp;
import com.mymobkit.common.CommonUtils;
import com.mymobkit.config.AppConfig;
import com.mymobkit.profiling.PeriodicTimer;

/**
 * FileLogger writes lines of text to a file. It knows where debug output files
 * should be stored, and it can disable logging in production releases.
 * 
 */
public class FileLogger {
	private static final String TAG = AppConfig.LOG_TAG_APP + ":FileLogger";
	protected PeriodicTimer pt = new PeriodicTimer(5000);
	private OutputStream debugOutput;

	public FileLogger(String fileName) {
		if (AppConfig.DELIVER_DIAGNOSTIC_DATA) {
			try {
				debugOutput = new BufferedOutputStream(MyMobKitApp.getContext().openFileOutput(
						fileName, Context.MODE_WORLD_READABLE));
				Log.d(TAG, "Writing debug output to: " + Environment.getDataDirectory());
			} catch (FileNotFoundException e) {
				CommonUtils.dieWithError(e);
			}
		}
	}

	public void writeLine(final String line) {
		if (!AppConfig.DELIVER_DIAGNOSTIC_DATA)
			return;
		try {
			debugOutput.write(line.getBytes());
		} catch (IOException e) {
			CommonUtils.dieWithError(e);
		}
	}

	public void terminate() {
		if (debugOutput != null) {
			try {
				// TODO(Stuart Anderson): Pretty sure we don't need to flush()
				// here.
				debugOutput.flush();
				debugOutput.close();
			} catch (IOException e) {
			}
		}
	}
}
