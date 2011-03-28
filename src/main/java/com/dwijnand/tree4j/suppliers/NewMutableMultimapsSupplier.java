package com.dwijnand.tree4j.suppliers;

import java.util.Comparator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

/**
 * A <b>guaranteed</b> {@link NewInstanceSupplier}-compliant implementation that
 * returns mutable {@link Multimap}s.
 * <p>
 * The sole constructor is private so that it may only be instantiated within
 * this class. It, therefore, defines a series of static methods, each that
 * return a new instance of a supplier of a new instance of a specific mutable
 * multimap implementation.
 *
 * @param <T> the type of the objects supplied
 * @param <K> the type of the keys in the multimap supplied
 * @param <V> the type of the values in the multimap supplied
 */
public abstract class NewMutableMultimapsSupplier<T extends Multimap<K, V>, K, V>
        extends NewInstanceSupplier<T> {

    /**
     * Creates a new instance. This sole constructor is private so that it may
     * not be subclassed outside of this class.
     */
    private NewMutableMultimapsSupplier() {
    }

    // TODO add javadoc
    public static <K, V> NewMutableMultimapsSupplier<ArrayListMultimap<K, V>, K, V> newNewArrayListMultimapSupplier() {
        return new NewMutableMultimapsSupplier<ArrayListMultimap<K, V>, K, V>() {

            @Override
            public ArrayListMultimap<K, V> get() {
                return ArrayListMultimap.create();
            }

        };
    };

    // TODO add javadoc
    public static <K, V> NewMutableMultimapsSupplier<HashMultimap<K, V>, K, V> newNewHashMultimapSupplier() {
        return new NewMutableMultimapsSupplier<HashMultimap<K, V>, K, V>() {

            @Override
            public HashMultimap<K, V> get() {
                return HashMultimap.create();
            }

        };
    };

    /**
     * Creates a new NewMutableMultimapsSupplier of {@link LinkedHashMultimap}s
     * of the specified key and value types.
     *
     * @param <K> the type of the keys in the multimap
     * @param <V> the type of the values in the multimap
     * @return a new NewMutableMultimapsSupplier
     */
    public static <K, V> NewMutableMultimapsSupplier<LinkedHashMultimap<K, V>, K, V> newNewLinkedHashMultimapSupplier() {
        return new NewMutableMultimapsSupplier<LinkedHashMultimap<K, V>, K, V>() {

            @Override
            public LinkedHashMultimap<K, V> get() {
                return LinkedHashMultimap.create();
            }

        };
    };

    // TODO add javadoc
    public static <K extends Comparable<K>, V extends Comparable<V>> NewMutableMultimapsSupplier<TreeMultimap<K, V>, K, V> newNewTreeMultimapSupplier() {
        return new NewMutableMultimapsSupplier<TreeMultimap<K, V>, K, V>() {

            @Override
            public TreeMultimap<K, V> get() {
                return TreeMultimap.create();
            }

        };
    };

    // TODO add javadoc
    public static <K, V> NewMutableMultimapsSupplier<TreeMultimap<K, V>, K, V> newNewTreeMultimapSupplier(
            final Comparator<? super K> keyComparator,
            final Comparator<? super V> valueComparator) {
        return new NewMutableMultimapsSupplier<TreeMultimap<K, V>, K, V>() {

            @Override
            public TreeMultimap<K, V> get() {
                return TreeMultimap.create(keyComparator, valueComparator);
            }

        };
    };

}
