package com.mymobkit.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * Counts events
 * 
 */
public class CountMetric implements SampledMetrics {
	private Map<String, Object> accum = new HashMap<String, Object>(1);

	@Override
	public synchronized Map<String, Object> sample() {
		HashMap<String, Object> result = new HashMap<String, Object>(accum);
		accum.clear();
		return result;
	}

	public synchronized void increment(String key, int amount) {
		Integer v = (Integer) accum.get(key);
		if (v == null) {
			accum.put(key, amount);
		} else {
			accum.put(key, v + amount);
		}
	}
}
