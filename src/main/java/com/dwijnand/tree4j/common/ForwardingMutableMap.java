package com.dwijnand.tree4j.common;

import com.google.common.collect.ForwardingMap;

// TODO document this class entirely
public abstract class ForwardingMutableMap<K, V> extends ForwardingMap<K, V> implements MutableMap<K, V> {
  protected ForwardingMutableMap() {}
}
