package com.dwijnand.tree4j.test.helpers;

import java.util.HashMap;

import com.dwijnand.tree4j.common.Factory;
import com.dwijnand.tree4j.common.ImmutableMapsBuildersFactory;
import com.dwijnand.tree4j.common.ImmutableMultimapsBuildersFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;

public final class Factories {

    private Factories() {
        // Utility class
    }

    public static final Factory<LinkedHashMultimap<String, String>> LINKED_HASH_MULTIMAP_SUPPLIER = new Factory<LinkedHashMultimap<String, String>>() {

        @Override
        public LinkedHashMultimap<String, String> get() {
            return LinkedHashMultimap.create();
        }

    };

    public static final Factory<HashMap<String, String>> HASH_MAP_SUPPLIER = new Factory<HashMap<String, String>>() {

        @Override
        public HashMap<String, String> get() {
            return Maps.newHashMap();
        }

    };

    public static final ImmutableMultimapsBuildersFactory<? extends ImmutableMultimap.Builder<String, String>, String, String> IMMUTABLE_MULTIMAP_BUILDER_SUPPLIER = ImmutableMultimapsBuildersFactory
            .newImmutableMultimapsBuildersFactory();

    public static final ImmutableMapsBuildersFactory<? extends ImmutableMap.Builder<String, String>, String, String> IMMUTABLE_MAP_BUILDER_SUPPLIER = ImmutableMapsBuildersFactory
            .newImmutableMapsBuildersFactory();

}
