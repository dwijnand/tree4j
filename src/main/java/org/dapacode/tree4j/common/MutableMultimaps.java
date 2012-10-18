package org.dapacode.tree4j.common;

import com.google.common.collect.Multimap;

// TODO document this class entirely
public final class MutableMultimaps {
  private MutableMultimaps() {}

  public static <K, V> MutableMultimap<K, V> wrap(final Multimap<K, V> mMap) {
    return new ForwardingMutableMultimap<K, V>() {
      @Override
      protected Multimap<K, V> delegate() {
        return mMap;
      }
    };
  }
}
