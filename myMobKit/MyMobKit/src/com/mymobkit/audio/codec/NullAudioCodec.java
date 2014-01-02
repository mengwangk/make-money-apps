package com.mymobkit.audio.codec;

import com.mymobkit.common.Conversions;


/**
 * An audio codec that does nothing.  Encoded audio is precisely equal
 * to the unencoded audio, except represented in a byte[] instead of a
 * short[].
 *
 */
public class NullAudioCodec extends AudioCodec {

  @Override
  public int decode(byte[] encodedData, short[] rawData, int encLen) {
    for (int i = 0; i < AudioCodec.SAMPLES_PER_FRAME; i++) {
       rawData[i] = Conversions.byteArrayToShort(encodedData, i * 2);
    }
    return encodedPacketSize();

  }

  @Override
  public int encode(short[] rawData, byte[] encodedData, int rawLen) {
    for( int i = 0; i < AudioCodec.SAMPLES_PER_FRAME; i++) {
      Conversions.shortToByteArray(encodedData, i*2, rawData[i]);
    }
    return AudioCodec.SAMPLES_PER_FRAME;
  }

  public int encodedPacketSize() {
    return AudioCodec.SAMPLES_PER_FRAME * 2;
  }
}
