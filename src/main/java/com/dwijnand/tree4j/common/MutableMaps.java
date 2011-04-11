package com.dwijnand.tree4j.common;

import java.util.Map;

public final class MutableMaps {
    private MutableMaps() {
    }

    public static <T extends Map<K, V>, K, V> MutableMap<K, V> wrap(
            final T map) {
        return new ForwardingMutableMap<K, V>() {
            @Override
            protected Map<K, V> delegate() {
                return map;
            }
        };
    }
}
