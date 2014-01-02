package com.mymobkit.audio.codec;

/**
 * Indicates an exception related to an {@link AudioCodec}
 *
 */
public class CodecSetupException extends Exception {

  public CodecSetupException(String string) {
    super(string);
  }
  public CodecSetupException(String msg, Throwable reason ) {
    super(msg, reason);
  }

  private static final long serialVersionUID = 1L;

}
