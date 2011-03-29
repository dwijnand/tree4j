package com.dwijnand.tree4j.common;

import com.google.common.collect.ForwardingMap;

public abstract class ForwardingMutableMap<K, V> extends ForwardingMap<K, V>
        implements MutableMap<K, V> {

}
