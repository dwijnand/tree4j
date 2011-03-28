package com.dwijnand.tree4j.common;

import java.util.Comparator;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;

/**
 * A <b>guaranteed</b> {@link Factory}-compliant implementation that returns
 * {@link ImmutableMap.Builder}s.
 * <p>
 * The sole constructor is private so that it may only be instantiated within
 * this class. It, therefore, defines a series of static methods, each that
 * return a new instance of a factory of a new instance of a specific immutable
 * map builder implementation.
 *
 * @param <T> the type of the objects created
 * @param <K> the type of the keys in the multimap built
 * @param <V> the type of the values in the multimap built
 */
public abstract class ImmutableMapsBuildersFactory<T extends ImmutableMap.Builder<K, V>, K, V>
        implements Factory<T> {

    /**
     * Creates a new instance. This sole constructor is private so that it may
     * not be subclassed outside of this class.
     */
    private ImmutableMapsBuildersFactory() {
    }

    // TODO add javadoc
    public static <K, V> ImmutableMapsBuildersFactory<ImmutableMap.Builder<K, V>, K, V> newImmutableMapsBuildersFactory() {
        return new ImmutableMapsBuildersFactory<ImmutableMap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableMap.Builder<K, V> get() {
                return ImmutableMap.builder();
            }

        };
    }

    // TODO add javadoc
    public static <K, V> ImmutableMapsBuildersFactory<ImmutableBiMap.Builder<K, V>, K, V> newImmutableBiMapsBuildersFactory() {
        return new ImmutableMapsBuildersFactory<ImmutableBiMap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableBiMap.Builder<K, V> get() {
                return ImmutableBiMap.builder();
            }

        };
    }

    // TODO add javadoc
    public static <K extends Comparable<K>, V> ImmutableMapsBuildersFactory<ImmutableSortedMap.Builder<K, V>, K, V> newImmutableSortedMapsBuildersFactoryNaturalOrder() {
        return new ImmutableMapsBuildersFactory<ImmutableSortedMap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableSortedMap.Builder<K, V> get() {
                return new ImmutableSortedMap.Builder<K, V>(
                        Ordering.natural());
            }

        };
    }

    // TODO add javadoc
    public static <K, V> ImmutableMapsBuildersFactory<ImmutableSortedMap.Builder<K, V>, K, V> newImmutableSortedMapsBuildersFactoryOrderedBy(
            final Comparator<K> comparator) {
        return new ImmutableMapsBuildersFactory<ImmutableSortedMap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableSortedMap.Builder<K, V> get() {
                return new ImmutableSortedMap.Builder<K, V>(comparator);
            }

        };
    }

    // TODO add javadoc
    public static <K extends Comparable<K>, V> ImmutableMapsBuildersFactory<ImmutableSortedMap.Builder<K, V>, K, V> newImmutableSortedMapsBuildersFactoryReverseOrder() {
        return new ImmutableMapsBuildersFactory<ImmutableSortedMap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableSortedMap.Builder<K, V> get() {
                return new ImmutableSortedMap.Builder<K, V>(Ordering
                        .natural().reverse());
            }

        };
    }

}
