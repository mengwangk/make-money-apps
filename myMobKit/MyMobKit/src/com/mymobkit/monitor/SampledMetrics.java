package com.mymobkit.monitor;

import java.util.Map;

/**
 * Returns current values of the metric being recorded. The CallMonitor starts a
 * thread that periodically and simultaneously queries all SampledMetrics and
 * creates a single MonitoredEvent from their aggregate.
 * 
 */
public interface SampledMetrics {
	Map<String, Object> sample();
}
