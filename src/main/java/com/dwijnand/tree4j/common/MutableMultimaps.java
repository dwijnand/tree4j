package com.dwijnand.tree4j.common;

import com.google.common.collect.Multimap;

public final class MutableMultimaps {
    private MutableMultimaps() {
    }

    public static <T extends Multimap<K, V>, K, V> MutableMultimap<K, V> wrap(
            final T map) {
        return new ForwardingMutableMultimap<K, V>() {
            @Override
            protected Multimap<K, V> delegate() {
                return map;
            }
        };
    }
}
