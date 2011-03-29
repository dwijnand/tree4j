package com.dwijnand.tree4j.test.helper;

import java.util.Map;

import com.dwijnand.tree4j.common.Factory;
import com.dwijnand.tree4j.common.MutableMap;
import com.dwijnand.tree4j.common.MutableMaps;
import com.dwijnand.tree4j.common.MutableMultimap;
import com.dwijnand.tree4j.common.MutableMultimaps;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public final class Factories {

    private Factories() {
        // Utility class
    }

    public static final Factory<MutableMultimap<String, String>> LINKED_HASH_MULTIMAP_FACTORY = new Factory<MutableMultimap<String, String>>() {

        @Override
        public MutableMultimap<String, String> get() {
            final Multimap<String, String> multimap = LinkedHashMultimap
                    .create();
            return MutableMultimaps.wrap(multimap);
        }

    };

    public static final Factory<MutableMap<String, String>> HASH_MAP_FACTORY = new Factory<MutableMap<String, String>>() {

        @Override
        public MutableMap<String, String> get() {
            final Map<String, String> map = Maps.newHashMap();
            return MutableMaps.wrap(map);
        }

    };

    public static final Factory<MutableMap<String, String>> LINKED_HASH_MAP_FACTORY = new Factory<MutableMap<String, String>>() {

        @Override
        public MutableMap<String, String> get() {
            final Map<String, String> map = Maps.newLinkedHashMap();
            return MutableMaps.wrap(map);
        }

    };

}
