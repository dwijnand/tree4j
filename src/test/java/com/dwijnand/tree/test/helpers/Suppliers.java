package com.dwijnand.tree.test.helpers;

import java.util.HashMap;

import com.google.common.base.Supplier;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;

public final class Suppliers {

    private Suppliers() {
        // Utility class
    }

    public static final Supplier<LinkedHashMultimap<String, String>> LINKED_HASH_MULTIMAP_SUPPLIER = new Supplier<LinkedHashMultimap<String, String>>() {

        @Override
        public LinkedHashMultimap<String, String> get() {
            return LinkedHashMultimap.create();
        }

    };

    public static final Supplier<HashMap<String, String>> HASH_MAP_SUPPLIER = new Supplier<HashMap<String, String>>() {

        @Override
        public HashMap<String, String> get() {
            return Maps.newHashMap();
        }

    };

}
