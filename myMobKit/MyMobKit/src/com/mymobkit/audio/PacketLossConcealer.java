package com.mymobkit.audio;

/**
 * Interface to the native packet loss concealment code that stretches and
 * shrinks audio without changing pitch.
 * 
 */
public class PacketLossConcealer {
	static {
		System.loadLibrary("speex");
		init();
	}

	private static native void init();

	public static native void rx(short audio[]);

	public static native void fillIn(short audio[]);

	public static native int maxOutputLengthAtSpeed(int nSamples, float rate);

	public static native int changeSpeed(short output[], short input[], int inputLen, float rate);
}
