package com.mymobkit.common;

/**
 * Factory interface
 * @param <T> product type
 */
public interface Factory<T> {
  T getInstance();
}
