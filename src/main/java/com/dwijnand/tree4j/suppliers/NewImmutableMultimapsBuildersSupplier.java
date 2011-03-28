package com.dwijnand.tree4j.suppliers;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;

/**
 * A <b>guaranteed</b> {@link NewInstanceSupplier}-compliant implementation that
 * returns {@link ImmutableMultimap.Builder}s.
 * <p>
 * The sole constructor is private so that it may only be instantiated within
 * this class. It, therefore, defines a series of static methods, each that
 * return a new instance of a supplier of a new instance of a specific immutable
 * multimap builder implementation.
 *
 * @param <T> the type of the objects supplied
 * @param <K> the type of the keys in the multimap built
 * @param <V> the type of the values in the multimap built
 */
public abstract class NewImmutableMultimapsBuildersSupplier<T extends ImmutableMultimap.Builder<K, V>, K, V>
        extends NewInstanceSupplier<T> {

    /**
     * Creates a new instance. This sole constructor is private so that it may
     * not be subclassed outside of this class.
     */
    private NewImmutableMultimapsBuildersSupplier() {
    }

    // TODO add javadoc
    public static <K, V> NewImmutableMultimapsBuildersSupplier<ImmutableMultimap.Builder<K, V>, K, V> newNewImmutableMultimapsBuildersSupplier() {
        return new NewImmutableMultimapsBuildersSupplier<ImmutableMultimap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableMultimap.Builder<K, V> get() {
                return ImmutableMultimap.builder();
            }

        };
    }

    // TODO add javadoc
    public static <K, V> NewImmutableMultimapsBuildersSupplier<ImmutableListMultimap.Builder<K, V>, K, V> newNewImmutableListMultimapsBuildersSupplier() {
        return new NewImmutableMultimapsBuildersSupplier<ImmutableListMultimap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableListMultimap.Builder<K, V> get() {
                return ImmutableListMultimap.builder();
            }

        };
    }

    // TODO add javadoc
    public static <K, V> NewImmutableMultimapsBuildersSupplier<ImmutableSetMultimap.Builder<K, V>, K, V> newNewImmutableSetMultimapsBuildersSupplier() {
        return new NewImmutableMultimapsBuildersSupplier<ImmutableSetMultimap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableSetMultimap.Builder<K, V> get() {
                return ImmutableSetMultimap.builder();
            }

        };
    }

}
