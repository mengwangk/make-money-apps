package com.mymobkit.video;

/**
 * JPEG handler.
 * 
 */
public final class JpegHandler {
	
	public native byte[] initRgb();

	public native byte[] initYuv(int frameBufferSize, int frameWidth, int frameHeight);

	public native byte[] encodeYUV420SP(byte[] yuv420spBuff);

	static {
		System.loadLibrary("jpeghandler");
	}
}
