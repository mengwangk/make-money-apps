package com.mymobkit.profiling;

import android.util.Log;

/**
 * Timer utility used for profiling the audio pipeline
 * 
 */
public class ProfilingTimer {
	private long startTime = 0;
	private long accumTime = 0;
	private boolean running = false;
	private String name;

	public ProfilingTimer(String name) {
		this.name = name;
	}

	public void start() {
		running = true;
		startTime = System.currentTimeMillis();
	}

	public void stop() {
		if (running == false)
			return;
		running = false;
		accumTime += System.currentTimeMillis() - startTime;
	}

	public double getAccumTime() {
		if (running) {
			stop();
			start();
		}
		return accumTime / 1000.0;
	}

	public void reset() {
		running = false;
		accumTime = 0;
	}

	public void print(double totalTime) {
		Log.d("TimeProfiler", name + ": pct:" + getAccumTime() / totalTime + " total:" + getAccumTime());
	}

	public void restart() {
		reset();
		start();
	}

	public String getName() {
		return name;
	}
}
