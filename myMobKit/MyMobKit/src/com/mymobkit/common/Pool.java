package com.mymobkit.common;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores a collection of objects allowing instances to be checked out and
 * returned.
 * 
 * The intent is that this allows reuse of large objects with short lifetimes
 * without loading the GC
 * 
 * @param <T> type of object held in the pool
 * 
 */
public class Pool<T> {
	List<T> pool = Collections.synchronizedList(new ArrayList<T>());
	Factory<T> itemFactory;

	public Pool(Factory<T> factory) {
		itemFactory = factory;
	}

	public T getItem() {
		T item;
		try {
			item = pool.remove(0);
		} catch (IndexOutOfBoundsException e) {
			Log.d("Pool", "new Instance");
			return itemFactory.getInstance();
		}
		return item;
	}

	public void returnItem(T item) {
		pool.add(item);
	}
}
