package com.dwijnand.tree.suppliers;

import java.util.Comparator;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;

/**
 * A <b>guaranteed</b> {@link NewInstanceSupplier}-compliant implementation that
 * returns {@link ImmutableMap.Builder}s.
 * <p>
 * The sole constructor is private so that it may only be instantiated within
 * this class. It, therefore, defines a series of static methods, each that
 * return a new instance of a supplier of a new instance of a specific immutable
 * map builder implementation.
 *
 * @param <T> the type of the objects supplied
 * @param <K> the type of the keys in the multimap built
 * @param <V> the type of the values in the multimap built
 */
public abstract class NewImmutableMapsBuildersSupplier<T extends ImmutableMap.Builder<K, V>, K, V>
        extends NewInstanceSupplier<T> {

    /**
     * Creates a new instance. This sole constructor is private so that it may
     * not be subclassed outside of this class.
     */
    private NewImmutableMapsBuildersSupplier() {
    }

    // TODO add javadoc
    public static <K, V> NewImmutableMapsBuildersSupplier<ImmutableMap.Builder<K, V>, K, V> newNewImmutableMapsBuildersSupplier() {
        return new NewImmutableMapsBuildersSupplier<ImmutableMap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableMap.Builder<K, V> get() {
                return ImmutableMap.builder();
            }

        };
    }

    // TODO add javadoc
    public static <K, V> NewImmutableMapsBuildersSupplier<ImmutableBiMap.Builder<K, V>, K, V> newNewImmutableBiMapsBuildersSupplier() {
        return new NewImmutableMapsBuildersSupplier<ImmutableBiMap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableBiMap.Builder<K, V> get() {
                return ImmutableBiMap.builder();
            }

        };
    }

    // TODO add javadoc
    public static <K extends Comparable<K>, V> NewImmutableMapsBuildersSupplier<ImmutableSortedMap.Builder<K, V>, K, V> newNewImmutableSortedMapsBuildersSupplierNaturalOrder() {
        return new NewImmutableMapsBuildersSupplier<ImmutableSortedMap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableSortedMap.Builder<K, V> get() {
                return new ImmutableSortedMap.Builder<K, V>(
                        Ordering.natural());
            }

        };
    }

    // TODO add javadoc
    public static <K, V> NewImmutableMapsBuildersSupplier<ImmutableSortedMap.Builder<K, V>, K, V> newNewImmutableSortedMapsBuildersSupplierOrderedBy(
            final Comparator<K> comparator) {
        return new NewImmutableMapsBuildersSupplier<ImmutableSortedMap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableSortedMap.Builder<K, V> get() {
                return new ImmutableSortedMap.Builder<K, V>(comparator);
            }

        };
    }

    // TODO add javadoc
    public static <K extends Comparable<K>, V> NewImmutableMapsBuildersSupplier<ImmutableSortedMap.Builder<K, V>, K, V> newNewImmutableSortedMapsBuildersSupplierReverseOrder() {
        return new NewImmutableMapsBuildersSupplier<ImmutableSortedMap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableSortedMap.Builder<K, V> get() {
                return new ImmutableSortedMap.Builder<K, V>(Ordering
                        .natural().reverse());
            }

        };
    }

}
