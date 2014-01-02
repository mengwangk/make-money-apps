package com.mymobkit.audio;

/**
 * An exception related to the audio subsystem.
 * 
 */
public final class AudioException extends Exception {
	private static final long serialVersionUID = -5101453437124907514L;
	private final int clientMessage;

	public AudioException(int clientMessage) {
		this.clientMessage = clientMessage;
	}

	public AudioException(AudioException cause) {
		super(cause);
		this.clientMessage = cause.clientMessage;
	}

	public int getClientMessage() {
		return clientMessage;
	}
}
