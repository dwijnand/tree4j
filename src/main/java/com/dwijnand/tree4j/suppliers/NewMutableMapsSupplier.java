package com.dwijnand.tree4j.suppliers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.Maps;

/**
 * A <b>guaranteed</b> {@link NewInstanceSupplier}-compliant implementation that
 * returns mutable {@link Map}s.
 * <p>
 * The sole constructor is private so that it may only be instantiated within
 * this class. It, therefore, defines a series of static methods, each that
 * return a new instance of a supplier of a new instance of a specific mutable
 * map implementation.
 *
 * @param <T> the type of the objects supplied
 * @param <K> the type of the keys in the multimap supplied
 * @param <V> the type of the values in the multimap supplied
 */
public abstract class NewMutableMapsSupplier<T extends Map<K, V>, K, V>
        extends NewInstanceSupplier<T> {

    /**
     * Creates a new instance. This sole constructor is private so that it may
     * not be subclassed outside of this class.
     */
    private NewMutableMapsSupplier() {
    }

    /**
     * Creates a new NewMutableMapsSupplier of {@link HashMap}s of the specified
     * key and value types.
     *
     * @param <K> the type of the keys in the map
     * @param <V> the type of the values in the map
     * @return a new NewMutableMapsSupplier
     */
    public static <K, V> NewMutableMapsSupplier<HashMap<K, V>, K, V> newNewHashMapSupplier() {
        return new NewMutableMapsSupplier<HashMap<K, V>, K, V>() {

            @Override
            public HashMap<K, V> get() {
                return Maps.newHashMap();
            }

        };
    };

    // TODO add javadoc
    public static <K, V> NewMutableMapsSupplier<HashMap<K, V>, K, V> newNewHashMapWithExpectedSizeSupplier(
            final int expectedSize) {
        return new NewMutableMapsSupplier<HashMap<K, V>, K, V>() {

            @Override
            public HashMap<K, V> get() {
                return Maps.newHashMapWithExpectedSize(expectedSize);
            }

        };
    };

    // TODO add javadoc
    public static <K extends Comparable<K>, V> NewMutableMapsSupplier<TreeMap<K, V>, K, V> newNewTreeMapSupplier() {
        return new NewMutableMapsSupplier<TreeMap<K, V>, K, V>() {

            @Override
            public TreeMap<K, V> get() {
                return Maps.newTreeMap();
            }

        };
    };

    // TODO add javadoc
    public static <C, K extends C, V> NewMutableMapsSupplier<TreeMap<K, V>, K, V> newNewTreeMapSupplier(
            final Comparator<C> comparator) {
        return new NewMutableMapsSupplier<TreeMap<K, V>, K, V>() {

            @Override
            public TreeMap<K, V> get() {
                return Maps.newTreeMap(comparator);
            }

        };
    };

    // TODO add javadoc
    public static <K, V> NewMutableMapsSupplier<LinkedHashMap<K, V>, K, V> newNewLinkedHashMapSupplier() {
        return new NewMutableMapsSupplier<LinkedHashMap<K, V>, K, V>() {

            @Override
            public LinkedHashMap<K, V> get() {
                return Maps.newLinkedHashMap();
            }

        };
    };

}
