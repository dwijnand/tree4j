package com.dwijnand.tree;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.base.Supplier;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

/**
 * A mutable tree backed by a {@link Multimap}.
 * 
 * @param <T> the type of the nodes in the tree
 */
public final class MultimapTree<T> extends BaseMutableTree<T> {

    /**
     * Creates a new MultimapTree.
     * 
     * @param <T> the type of the nodes in the new tree
     * @return a new MultimapTree
     */
    public static <T> MultimapTree<T> create() {
        return new MultimapTree<T>();
    }

    /**
     * Creates a copy the specified MultimapTree.
     * 
     * @param <T> the type of the nodes in the new and specified tree
     * @param multimapTree the MultimapTree
     * @return a new MultimapTree
     */
    public static <T> MultimapTree<T> copyOf(
            final MultimapTree<T> multimapTree) {
        return new MultimapTree<T>(checkNotNull(multimapTree));
    }

    public MultimapTree() {
        super(new Supplier<Multimap<T, T>>() {

            @Override
            public Multimap<T, T> get() {
                return LinkedHashMultimap.create();
            }

        }, new Supplier<Map<T, T>>() {

            @Override
            public Map<T, T> get() {
                return Maps.newHashMap();
            }

        });
    }

    private MultimapTree(final MultimapTree<T> multimapTree) {
        super(multimapTree);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
