package com.dwijnand.tree4j.common;

import com.google.common.collect.ForwardingMultimap;

public abstract class ForwardingMutableMultimap<K, V> extends
        ForwardingMultimap<K, V> implements MutableMultimap<K, V> {

}
