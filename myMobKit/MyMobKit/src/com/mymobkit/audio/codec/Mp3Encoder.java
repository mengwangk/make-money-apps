package com.mymobkit.audio.codec;

public final class Mp3Encoder {

	
	/*public native void init(int channel, int sampleRate, int brate);

	public native void destroy();

	public native byte[] encode(short[] buffer, int len);*/

	public native int nativeOpenEncoder();

	public native void nativeCloseEncoder();

	public native int nativeEncodingPCM(byte[] pcmdata, int length, byte[] mp3Data);

	public native byte[] nativeEncodingPCMv2(short[] pcmdata, int length);

	static {
		System.loadLibrary("mp3encoder");
	}
}
