package com.mymobkit.audio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.mymobkit.R;
import com.mymobkit.audio.codec.AudioCodec;
import com.mymobkit.common.Factory;
import com.mymobkit.common.Pool;
import com.mymobkit.config.AppConfig;
import com.mymobkit.monitor.CountMetric;
import com.mymobkit.monitor.HistogramMetric;
import com.mymobkit.profiling.PacketLogger;
import com.mymobkit.profiling.PeriodicTimer;
import com.mymobkit.profiling.ProfilingTimer;

/**
 * MicrophoneReader provides robust async access to the device microphone. A
 * reader thread performs blocking reads on the microphone device and writes the
 * results to an async queue.
 * 
 * The microphone reader provides non-blocking access to any data in this queue
 * and handles clearing and restarting the reader in the event of buffer
 * overflows or hardware issues.
 * 
 */
public class MicrophoneReader {
	public static final String TAG = AppConfig.LOG_TAG_APP + ":MicrophoneReader";
	private static final int AUDIO_SOURCE = Build.VERSION.SDK_INT >= 11 ? MediaRecorder.AudioSource.VOICE_COMMUNICATION : MediaRecorder.AudioSource.DEFAULT;

	public static final int AUDIO_BUFFER_SIZE = 8000 + AudioRecord.getMinBufferSize(AudioCodec.SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
	private AudioRecord audioSource = new AudioRecord(AUDIO_SOURCE, AudioCodec.SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, AUDIO_BUFFER_SIZE * 10);
	private byte encodedData[] = new byte[512];
	private LinkedList<EncodedAudioData> audioQueue;
	private AudioCodec codec;
	private boolean micStarted = false;
	private long sequenceNumber = 0;
	PeriodicTimer debugTimer = new PeriodicTimer(5000);
	private int totalSamplesRead;
	private MicReadThread micThread = new MicReadThread();
	private final boolean singleThread = false;
	private ProfilingTimer readTime = new ProfilingTimer("Mic Read Time");
	private PeriodicTimer debugTextUpdateTimer = new PeriodicTimer(1000);
	private DecimalFormat loadFormat = new DecimalFormat("0.00");
	private PacketLogger packetLogger;

	private final AtomicReference<AudioException> micThreadException;
	private final AtomicReference<Boolean> enableMute;
	private final CountMetric counter;
	private final HistogramMetric waveformStats = new HistogramMetric(Short.MIN_VALUE, Short.MAX_VALUE, 16);

	private List<AudioChunk> micAudioList =
			Collections.synchronizedList(new ArrayList<AudioChunk>());
	private Pool<AudioChunk> chunkPool = new Pool<AudioChunk>(new Factory<AudioChunk>() {

		public AudioChunk getInstance() {
			return new AudioChunk(new short[AudioCodec.SAMPLES_PER_FRAME], 0);
		}
	});

	public MicrophoneReader(LinkedList<EncodedAudioData> outgoingAudio, AudioCodec codec, PacketLogger packetLogger) {
		this.codec = codec;
		this.packetLogger = packetLogger;
		audioQueue = outgoingAudio;
		micThreadException = new AtomicReference<AudioException>();
		enableMute = new AtomicReference<Boolean>(false);
		counter = new CountMetric();
	}

	private void waitForMicReady() throws AudioException {
		int waitCount = 0;
		while (audioSource.getState() != AudioRecord.STATE_INITIALIZED) {
			if (waitCount > 50) {
				micThread.terminate();
				throw new AudioException(R.string.msg_microphone_failure);
			}

			waitCount++;
			audioSource.release();
			audioSource = new AudioRecord(MediaRecorder.AudioSource.MIC, AudioCodec.SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, AUDIO_BUFFER_SIZE * 10);
			try {
				Log.d(TAG, "Waiting for Microphone to initialize...[" + waitCount + "]");
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	public void go() throws AudioException {
		short audioData[];
		AudioChunk chunk;

		AudioException exception = micThreadException.get();
		if (exception != null) {
			throw new AudioException(exception);
		}

		if (singleThread) {
			micThread.readFromMic();
		} else if (!micThread.isAlive()) {
			// This will throw an unhelpful error if the
			// micThread dies...
			micThread.start();
		}

		if (micAudioList.size() > 50) {
			micAudioList.clear();
			Log.d(TAG, "cleared mic queue, too much backlog");
		}
		while (!micAudioList.isEmpty() &&
				audioQueue.size() < 2) {
			try {
				chunk = micAudioList.remove(0);
				audioData = chunk.getChunk();
			} catch (IndexOutOfBoundsException e) {
				return; // no data
			}

			int encodedDataLen = codec.encode(audioData, encodedData, AudioCodec.SAMPLES_PER_FRAME);
			chunkPool.returnItem(chunk);
			byte encodedBuffer[] = new byte[encodedDataLen];
			System.arraycopy(encodedData, 0, encodedBuffer, 0, encodedDataLen);
			packetLogger.logPacket(chunk.sequenceNumber, PacketLogger.PACKET_ENCODED);
			audioQueue.add(new EncodedAudioData(encodedBuffer, chunk.sequenceNumber, chunk.sequenceNumber));
		}
	}

	public void terminate() {
		if (!singleThread) {
			micThread.terminate();
		}
		if (audioSource.getState() == AudioRecord.STATE_INITIALIZED) {
			audioSource.stop();
			audioSource.release();
		}
	}

	private class AudioChunk {
		public long sequenceNumber;
		private short chunk[];

		public AudioChunk(short audio[], long seqNum) {
			chunk = audio;
			sequenceNumber = seqNum;
		}

		public short[] getChunk() {
			return chunk;
		}
	}

	private class MicReadThread extends Thread {
		private boolean terminate;
		private boolean outOfLoop;

		public MicReadThread() {
			super(TAG);
		}

		@Override
		public void run() {
			try {
				while (!terminate) {
					readFromMic();
				}
			} catch (AudioException e) {
				micThreadException.set(e);
			}
			outOfLoop = true;
			synchronized (this) {
				notify();
			}
		}

		public void terminate() {
			terminate = true;
		}

		AudioChunk staticChunk = new AudioChunk(new short[AudioCodec.SAMPLES_PER_FRAME], 0);

		public void readFromMic() throws AudioException {
			if (!micStarted) {
				Log.d(TAG, "Starting audio recording");
				micStarted = true;
				waitForMicReady();
				audioSource.startRecording();
			}
			if (audioSource.getState() != AudioRecord.STATE_INITIALIZED) {
				Log.d(TAG, "AudioRecord is not initialized");
			}
			if (audioSource.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
				Log.d(TAG, "not in record state, restarting...");
				micStarted = false;
			}

			long readStartTime = SystemClock.uptimeMillis();
			AudioChunk chunk = chunkPool.getItem();
			readTime.start();
			int samplesRead = audioSource.read(chunk.getChunk(), 0, AudioCodec.SAMPLES_PER_FRAME);
			readTime.stop();
			chunk.sequenceNumber = sequenceNumber++;

			packetLogger.logPacket(chunk.sequenceNumber, PacketLogger.PACKET_IN_MIC_QUEUE);

			checkClipping(chunk);

			if (enableMute.get()) {
				muteAudio(chunk);
			}

			micAudioList.add(chunk);

			if (samplesRead != AudioCodec.SAMPLES_PER_FRAME) {
				Log.w(TAG, "VoiceSender read only "
						+ Integer.toString(samplesRead) + " samples");
			}

			totalSamplesRead += samplesRead;
			long readStopTime = SystemClock.uptimeMillis();
			if (readStopTime - readStartTime > 300) {
				Log.e(TAG, "STRANGE LONG MIC READ TIME: " + (readStopTime - readStartTime));
			}
		}
	}

	private void checkClipping(AudioChunk chunk) {
		int clips = 0;
		for (short sample : chunk.getChunk()) {
			waveformStats.addEvent(sample);
			if (sample == Short.MAX_VALUE || sample == Short.MIN_VALUE) {
				clips++;
			}
		}
		counter.increment("clipped", clips);
		counter.increment("samples", chunk.getChunk().length);
	}

	public void flush() {
		micAudioList.clear();
	}

	public void setMute(boolean updatedMuteSetting) {
		enableMute.set(updatedMuteSetting);
	}

	public void muteAudio(AudioChunk chunk) {
		Arrays.fill(chunk.getChunk(), (short) 0);
	}
}
