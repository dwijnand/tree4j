package com.dwijnand.tree4j.common;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;

/**
 * A <b>guaranteed</b> {@link Factory}-compliant implementation that returns
 * {@link ImmutableMultimap.Builder}s.
 * <p>
 * The sole constructor is private so that it may only be instantiated within
 * this class. It, therefore, defines a series of static methods, each that
 * return a new instance of a factory of a new instance of a specific immutable
 * multimap builder implementation.
 *
 * @param <T> the type of the objects created
 * @param <K> the type of the keys in the multimap built
 * @param <V> the type of the values in the multimap built
 */
public abstract class ImmutableMultimapsBuildersFactory<T extends ImmutableMultimap.Builder<K, V>, K, V>
        implements Factory<T> {

    /**
     * Creates a new instance. This sole constructor is private so that it may
     * not be subclassed outside of this class.
     */
    private ImmutableMultimapsBuildersFactory() {
    }

    // TODO add javadoc
    public static <K, V> ImmutableMultimapsBuildersFactory<ImmutableMultimap.Builder<K, V>, K, V> newImmutableMultimapsBuildersFactory() {
        return new ImmutableMultimapsBuildersFactory<ImmutableMultimap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableMultimap.Builder<K, V> get() {
                return ImmutableMultimap.builder();
            }

        };
    }

    // TODO add javadoc
    public static <K, V> ImmutableMultimapsBuildersFactory<ImmutableListMultimap.Builder<K, V>, K, V> newImmutableListMultimapsBuildersFactory() {
        return new ImmutableMultimapsBuildersFactory<ImmutableListMultimap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableListMultimap.Builder<K, V> get() {
                return ImmutableListMultimap.builder();
            }

        };
    }

    // TODO add javadoc
    public static <K, V> ImmutableMultimapsBuildersFactory<ImmutableSetMultimap.Builder<K, V>, K, V> newImmutableSetMultimapsBuildersFactory() {
        return new ImmutableMultimapsBuildersFactory<ImmutableSetMultimap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableSetMultimap.Builder<K, V> get() {
                return ImmutableSetMultimap.builder();
            }

        };
    }

}
