package com.dwijnand.tree4j.test.helpers;

import java.util.HashMap;

import com.dwijnand.tree4j.suppliers.NewImmutableMapsBuildersSupplier;
import com.dwijnand.tree4j.suppliers.NewImmutableMultimapsBuildersSupplier;
import com.dwijnand.tree4j.suppliers.NewMutableMapsSupplier;
import com.dwijnand.tree4j.suppliers.NewMutableMultimapsSupplier;
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

    public static final NewImmutableMultimapsBuildersSupplier<? extends ImmutableMultimap.Builder<String, String>, String, String> IMMUTABLE_MULTIMAP_BUILDER_SUPPLIER = NewImmutableMultimapsBuildersSupplier
            .newNewImmutableMultimapsBuildersSupplier();

    public static final NewImmutableMapsBuildersSupplier<? extends ImmutableMap.Builder<String, String>, String, String> IMMUTABLE_MAP_BUILDER_SUPPLIER = NewImmutableMapsBuildersSupplier
            .newNewImmutableMapsBuildersSupplier();

}
