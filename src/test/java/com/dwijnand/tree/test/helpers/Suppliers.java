package com.dwijnand.tree.test.helpers;

import java.util.HashMap;

import com.dwijnand.tree.suppliers.NewMutableMapsSupplier;
import com.dwijnand.tree.suppliers.NewMutableMultimapsSupplier;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;

public final class Suppliers {

    private Suppliers() {
        // Utility class
    }

    public static final NewMutableMultimapsSupplier<LinkedHashMultimap<String, String>, String, String> LINKED_HASH_MULTIMAP_SUPPLIER = NewMutableMultimapsSupplier
            .newNewLinkedHashMultimapSupplier();

    public static final NewMutableMapsSupplier<HashMap<String, String>, String, String> HASH_MAP_SUPPLIER = NewMutableMapsSupplier
            .newNewHashMapSupplier();

    public static final Supplier<ImmutableMultimap.Builder<String, String>> IMMUTABLE_MULTIMAP_BUILDER_SUPPLIER = new Supplier<ImmutableMultimap.Builder<String, String>>() {

        @Override
        public ImmutableMultimap.Builder<String, String> get() {
            return ImmutableMultimap.builder();
        }

    };

    public static final Supplier<ImmutableMap.Builder<String, String>> IMMUTABLE_MAP_BUILDER_SUPPLIER = new Supplier<ImmutableMap.Builder<String, String>>() {

        @Override
        public ImmutableMap.Builder<String, String> get() {
            return ImmutableMap.builder();
        }

    };

}
