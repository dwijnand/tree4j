package com.dwijnand.tree4j.test.helpers;

import java.util.Map;

import com.dwijnand.tree4j.ImmutableMultimapTree.ChildrenBuilderFactory;
import com.dwijnand.tree4j.ImmutableMultimapTree.ParentsBuildersFactory;
import com.dwijnand.tree4j.common.Factory;
import com.dwijnand.tree4j.common.MutableMap;
import com.dwijnand.tree4j.common.MutableMaps;
import com.dwijnand.tree4j.common.MutableMultimap;
import com.dwijnand.tree4j.common.MutableMultimaps;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public final class Factories {

    private Factories() {
        // Utility class
    }

    public static final Factory<MutableMultimap<String, String>> LINKED_HASH_MULTIMAP_SUPPLIER = new Factory<MutableMultimap<String, String>>() {

        @Override
        public MutableMultimap<String, String> get() {
            final Multimap<String, String> multimap = LinkedHashMultimap
                    .create();
            return MutableMultimaps.wrap(multimap);
        }

    };

    public static final Factory<MutableMap<String, String>> HASH_MAP_SUPPLIER = new Factory<MutableMap<String, String>>() {

        @Override
        public MutableMap<String, String> get() {
            final Map<String, String> map = Maps.newHashMap();
            return MutableMaps.wrap(map);
        }

    };

    public static final ChildrenBuilderFactory<? extends ImmutableMultimap.Builder<String, String>, String> IMMUTABLE_MULTIMAP_BUILDER_SUPPLIER = ChildrenBuilderFactory
            .newImmutableMultimapsBuildersFactory();

    public static final ParentsBuildersFactory<? extends ImmutableMap.Builder<String, String>, String> IMMUTABLE_MAP_BUILDER_SUPPLIER = ParentsBuildersFactory
            .newImmutableMapsBuildersFactory();

}
