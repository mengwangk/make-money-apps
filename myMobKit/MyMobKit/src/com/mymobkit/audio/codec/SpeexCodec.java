package com.mymobkit.audio.codec;

import android.util.Log;

/**
 * An audio codec that uses the Speex library to encode packets. Calls through
 * to the native library implementations of encode and decode.
 * 
 */
public class SpeexCodec extends AudioCodec {
	public static final String TAG = SpeexCodec.class.getSimpleName();
	public Thread loadThread = new Thread() {
		@Override
		public void run() {
			try {
				System.loadLibrary("speex");
			} catch (Throwable e) {
				throw new AssertionError(e);
			}
			Log.d(TAG, "loaded redspeex, now opening it");
			int result = openSpeex();
			if (result != 0) {
				throw new AssertionError("Speex initialization failed");
			}
		}
	};

	@Override
	public void waitForInitializationComplete() {
		if (loadThread.isAlive()) {
			Log.d(TAG, "Waiting for Speex to load...");
			try {
				loadThread.join();
			} catch (InterruptedException e) {
				Log.w(TAG, e);
			}
		}
	}

	public SpeexCodec() {
		loadThread.start();
	}

	public native int openSpeex();

	public native void closeSpeex();

	@Override
	public void terminate() {
		closeSpeex();
	}

	@Override
	public native int decode(byte[] encodedData, short[] rawData, int encLen);

	@Override
	public native int encode(short[] rawData, byte[] encodedData, int rawLen);
}
