package com.mymobkit.audio;

/**
 * Stores encoded (not encrypted) audio data along with sequence information
 * from the encoding stream and the packet stream this data arrived in.
 * 
 */
public class EncodedAudioData implements Comparable<EncodedAudioData> {
	public byte[] data;
	public long sequenceNumber;
	public long sourceSequenceNumber;

	public EncodedAudioData(byte data[], long sequenceNumber, long sourceSequenceNumber) {
		this.data = data;
		this.sequenceNumber = sequenceNumber;
		this.sourceSequenceNumber = sourceSequenceNumber;
	}

	public int compareTo(EncodedAudioData data) {
		if (sequenceNumber < data.sequenceNumber)
			return -1;
		if (sequenceNumber > data.sequenceNumber)
			return 1;
		return 0;
	}

	public boolean equals(EncodedAudioData data) {
		if (data == null)
			return false;
		return sequenceNumber == data.sequenceNumber;
	}

	@Override
	public int hashCode() {
		return (int) sequenceNumber;
	}
}