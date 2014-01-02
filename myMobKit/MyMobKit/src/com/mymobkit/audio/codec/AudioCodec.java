
package com.mymobkit.audio.codec;

import com.mymobkit.config.AppConfig;


/**
 * Provides the basic interface for all audio codecs as well as default implementations
 * for some less common methods.  .
 *
 * New codecs need to be added to this static factory method in this class.
 *
 */
public abstract class AudioCodec {
  private final static String TAG = AppConfig.LOG_TAG_APP + ":AudioCodec";
  public static int SAMPLE_RATE = 8000;
  public static int FRAME_RATE = 50;
  public static int SAMPLES_PER_FRAME = SAMPLE_RATE/FRAME_RATE;

  //returns the number of raw samples written to rawData
  public abstract int decode( byte [] encodedData, short [] rawData, int encodedBytes );

  //returns the number of encoded bytes written to encodedData
	public abstract int encode( short [] rawData, byte [] encodedData, int rawSamples );

  public void waitForInitializationComplete() {
    return;
  }

  public void terminate() {}

  public static AudioCodec getInstance(String codecID) {
    if( codecID.equals( "SPEEX" ) )
      return new SpeexCodec();
    if( codecID.equals( "G711" ) )
      return new G711AudioCodec();
    if( codecID.equals( "NullAudioCodec"))
      return new NullAudioCodec();

    throw new AssertionError("Unknown codec: " + codecID);
  }
}
