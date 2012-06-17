package com.mylotto.analysis;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mylotto.data.PrizeType;

/**
 * Helper class
 * 
 * @author MEKOH
 *
 */
public final class AnalysisHelper {
	
	/**
	 * Check if the number is FIRST, SECOND or THIRD prize.
	 * Return true if yes.
	 * 
	 * @param prizeType
	 * @return
	 */
	public static boolean isTop3Prize(final String prizeType) {
		if (PrizeType.FIRST_PRIZE.equalsIgnoreCase(prizeType) || PrizeType.SECOND_PRIZE.equalsIgnoreCase(prizeType) || PrizeType.THIRD_PRIZE.equalsIgnoreCase(prizeType)) {
			return true;
		}
		return false;
	}


	/**
	 * Sort a map by values
	 * 
	 * @param map Unsorted map
	 * @return Sorted map
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	/**
	 * Sort a map by values
	 * 
	 * @param map Unsorted map
	 * @return Sorted map
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDesc(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
