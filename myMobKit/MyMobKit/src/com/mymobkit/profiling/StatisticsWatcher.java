package com.mymobkit.profiling;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import com.mymobkit.monitor.SampledMetrics;

/**
 * Utility class that tracks statistics of a sequence of observations
 * 
 */
public class StatisticsWatcher {
	private float min = Float.MAX_VALUE;
	private float max = -Float.MAX_VALUE;
	private float avgSize = 0;
	private float avgVar = 0;
	private float w = .05f;
	public String debugName = "default";
	private boolean doPrintDebug = false;
	private boolean doReset = false;
	private float trueAverage = 0;
	private int nSample = 0;

	public void reset() {
		nSample = 0;
	}

	public void setW(float val) {
		w = val;
	}

	public void setAvg(float val) {
		avgSize = val;
	}

	public void observeValue(float val) {
		avgSize = (val - avgSize) * w + avgSize;
		float d = (val - avgSize);
		avgVar = avgVar * (1 - w) + d * d * w;

		nSample++;
		trueAverage = (nSample - 1) / (float) nSample * trueAverage + 1.0f / nSample * val;
		min = Math.min(min, val);
		max = Math.max(max, val);

		if (doPrintDebug && pt.periodically()) {
			Log.d("StatsWatcher", "[" + debugName + "] avg: " + avgSize + " stddev: " + Math.sqrt(avgVar) + " trueAvg=" + trueAverage);
		}
	}

	private PeriodicTimer pt = new PeriodicTimer(5000);

	public float getAvg() {
		return avgSize;
	}

	public float getVar() {
		return avgVar;
	}

	public float getTrueAverage() {
		return trueAverage;
	}

	public void setPrintDebug(boolean b) {
		doPrintDebug = b;
	}

	public SampledMetrics getSampler() {
		return new SampledMetrics() {
			private Map<String, Object> metrics = new HashMap<String, Object>(2);

			@Override
			public Map<String, Object> sample() {
				metrics.put("avg", getAvg());
				metrics.put("var", getVar());
				metrics.put("min", min);
				metrics.put("max", max);

				Log.d("Statistics", "Sample Min: " + min);

				min = Float.MAX_VALUE;
				max = -Float.MAX_VALUE;

				return metrics;
			}
		};
	}
}
