package com.dwijnand.tree.suppliers;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;

/**
 * A <b>guaranteed</b> {@link NewInstanceSupplier}-compliant implementation that
 * returns {@link ImmutableMultimap.Builder}s. *
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
public abstract class NewImmutableMultimapBuilderSupplier<T extends ImmutableMultimap.Builder<K, V>, K, V>
        extends NewInstanceSupplier<T> {

    private NewImmutableMultimapBuilderSupplier() {
    }

    public static <K, V> NewImmutableMultimapBuilderSupplier<ImmutableMultimap.Builder<K, V>, K, V> newImmutableMultimapBuilderSupplier() {
        return new NewImmutableMultimapBuilderSupplier<ImmutableMultimap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableMultimap.Builder<K, V> get() {
                return ImmutableMultimap.builder();
            }

        };
    }

    public static <K, V> NewImmutableMultimapBuilderSupplier<ImmutableListMultimap.Builder<K, V>, K, V> newImmutableListMultimapBuilderSupplier() {
        return new NewImmutableMultimapBuilderSupplier<ImmutableListMultimap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableListMultimap.Builder<K, V> get() {
                return ImmutableListMultimap.builder();
            }

        };
    }

    public static <K, V> NewImmutableMultimapBuilderSupplier<ImmutableSetMultimap.Builder<K, V>, K, V> newImmutableSetMultimapBuilderSupplier() {
        return new NewImmutableMultimapBuilderSupplier<ImmutableSetMultimap.Builder<K, V>, K, V>() {

            @Override
            public ImmutableSetMultimap.Builder<K, V> get() {
                return ImmutableSetMultimap.builder();
            }

        };
    }

}
