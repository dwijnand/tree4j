package org.dapacode.tree4j.common;

import com.google.common.collect.ForwardingMultimap;

// TODO document this class entirely
public abstract class DelegatingMutableMultimap<K, V> extends ForwardingMultimap<K, V> implements MutableMultimap<K, V> {
  protected DelegatingMutableMultimap() {}
}
