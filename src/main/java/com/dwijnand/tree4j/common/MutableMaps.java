package com.dwijnand.tree4j.common;

import java.util.Map;

public final class MutableMaps {
    private MutableMaps() {
    }

    public static <K, V> MutableMap<K, V> wrap(final Map<K, V> map) {
        return new ForwardingMutableMap<K, V>() {
            @Override
            protected Map<K, V> delegate() {
                return map;
            }
        };
    }
}
