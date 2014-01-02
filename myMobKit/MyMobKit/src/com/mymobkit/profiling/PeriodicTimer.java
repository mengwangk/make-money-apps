package com.mymobkit.profiling;

import android.os.SystemClock;

/**
 * Allows periodic actions in a synchronous context.
 * 
 * The periodically() method will return true at most once in each period, where
 * period is expressed in milliseconds.
 * 
 */
public class PeriodicTimer {
	private long last, period;
	private long lastPeriodActual;

	public PeriodicTimer(long period) {
		this.period = period;
		last = SystemClock.uptimeMillis();
	}

	public long millisSinceLastPeriod() {
		long now = SystemClock.uptimeMillis();
		return now - last;
	}

	public boolean periodically() {
		long now = SystemClock.uptimeMillis();
		if (now > last + period) {
			lastPeriodActual = now - last;
			last = now;
			return true;
		}
		return false;
	}

	public long getActualLastPeriod() {
		return lastPeriodActual;
	}

	public long getPeriod() {
		return period;
	}
}
