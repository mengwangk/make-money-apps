package com.mymobkit.profiling;

import com.mymobkit.audio.FileLogger;
import com.mymobkit.config.AppConfig;

import android.os.SystemClock;

/**
 * Logs information about audio packet events to a file.
 * 
 * The log line format is: timeInMillis packetSequenceNumber stage extra
 * 
 * where stage is one of the constants defined in this file. This allows
 * reconstruction of the at which a specific packet hit different parts of the
 * audio processing pipeline.
 * 
 * The sequence number is always the codec sequence number, not the RTP sequence
 * number
 * 
 */
public class PacketLogger extends FileLogger {
	public static final String PACKET_DATA_FILENAME = "packetData.txt";
	public static final String TAG = "PacketLogger";

	public static final int PACKET_IN_MIC_QUEUE = 0;
	public static final int PACKET_ENCODED = 1;
	public static final int PACKET_SENDING = 2;
	public static final int PACKET_BUNDLED = 3;
	public static final int PACKET_RECEIVED = 4;
	public static final int PACKET_DECODED = 5;
	public static final int PLAYHEAD = 6;
	public static final int PLAYHEAD_JUMP_FORWARD = 7;
	public static final int PLAYHEAD_JUMP_BACK = 8;
	public static final int PLAY_BUFFER_EMPTY = 9;
	public static final int FILLING_GAP = 10;
	public static final int PLAY_QUEUE_INSERT = 11;
	public static final int EXPECTED_PACKET_NUM = 12;
	public static final int LATENCY_PEAK = 13;
	public static final int FAILED_READ = 14;
	private static final long decimate = 1;

	public PacketLogger() {
		super(PACKET_DATA_FILENAME);
	}

	public void logPacket(long packetNumber, int stage) {
		logPacket(packetNumber, stage, -1);
	}

	public void logPacket(long packetNumber, int stage, int extra) {
		if (!AppConfig.DELIVER_DIAGNOSTIC_DATA)
			return;
		if (packetNumber % decimate != 0 && stage <= 6)
			return;
		String info = new String() +
				SystemClock.uptimeMillis() + " " +
				packetNumber + " " +
				stage + " " +
				extra + "\n";
		writeLine(info);
	}

}
