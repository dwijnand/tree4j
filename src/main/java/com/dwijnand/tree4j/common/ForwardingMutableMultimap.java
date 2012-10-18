package com.dwijnand.tree4j.common;

import com.google.common.collect.ForwardingMultimap;

// TODO document this class entirely
public abstract class ForwardingMutableMultimap<K, V> extends ForwardingMultimap<K, V> implements MutableMultimap<K, V> {
  protected ForwardingMutableMultimap() {}
}
