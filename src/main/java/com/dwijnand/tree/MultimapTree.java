package com.dwijnand.tree;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

/**
 * A mutable tree backed by a {@link Multimap}.
 * 
 * @param <T> the type of the nodes in the tree
 */
public final class MultimapTree<T> implements MutableTree<T> {

    /**
     * The parent-children relationships of the tree.
     */
    private final Multimap<T, T> children;

    /**
     * The child-parent relationships of the tree.
     */
    private final Map<T, T> parents;

    /**
     * The root of the tree.
     */
    private T root;

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
     * Creates a new MultimapTree from the specified MultimapTree.
     * 
     * @param <T> the type of the nodes in the new and specified tree
     * @param multimapTree the MultimapTree
     * @return a new MultimapTree
     */
    public static <T> MultimapTree<T> create(
            final MultimapTree<T> multimapTree) {
        return new MultimapTree<T>(checkNotNull(multimapTree));
    }

    private MultimapTree() {
        children = LinkedHashMultimap.create();
        parents = Maps.newHashMap();
    }

    private MultimapTree(final MultimapTree<T> multimapTree) {
        children = LinkedHashMultimap.create(multimapTree.children);
        parents = Maps.newHashMap(multimapTree.parents);
        root = multimapTree.root;
    }

    @Override
    public boolean contains(final Object o) {
        if (o == null) {
            return false;
        } else if (o == root) {
            return true;
        } else {
            return parents.containsKey(o);
        }
    }

    @Override
    public T getParent(final T node) {
        return parents.get(checkNotNull(node));
    }

    @Override
    public Collection<T> getChildren(final T node) {
        return children.get(checkNotNull(node));
    }

    @Override
    public T getRoot() {
        return root;
    }

    @Override
    public MultimapTree<T> withRoot(final T node) {
        checkNotNull(node);
        final MultimapTree<T> multimapTree = create();
        multimapTree.root = node;
        return multimapTree;
    };

    @Override
    public MultimapTree<T> setRoot(final T node) {
        clear();
        root = checkNotNull(node);
        return this;
    }

    @Override
    public MultimapTree<T> add(final T parent, final T child) {
        final MultimapTree<T> multimapTree = create(this);
        multimapTree.added(parent, child);
        return multimapTree;
    };

    @Override
    public MultimapTree<T> added(final T parent, final T child) {
        checkNotNull(parent);
        checkNotNull(child);
        checkArgument(contains(parent),
                "%s does not contain parent node %s", getClass()
                        .getSimpleName(), parent);
        if (contains(child)) {
            return this;
        }
        children.put(parent, child);
        parents.put(child, parent);
        return this;
    }

    @Override
    public void clear() {
        children.clear();
        parents.clear();
        root = null;
    }

    @Override
    public MultimapTree<T> remove(final T node) {
        checkNotNull(node);
        if (node == root) {
            // optimisation
            return create();
        }
        final MultimapTree<T> multimapTree = create(this);
        multimapTree.removed(node);
        return multimapTree;
    };

    @Override
    public MultimapTree<T> removed(final T node) {
        checkNotNull(node);
        if (node == root) {
            // optimisation
            root = null;
            clear();
            return this;
        }

        Collection<T> nodeChildren = children.get(node);
        // A copy is required here to avoid a ConcurrentModificationException
        nodeChildren = ImmutableList.copyOf(nodeChildren);

        children.removeAll(node);
        children.get(getParent(node)).remove(node);
        parents.remove(node);

        for (final T child : nodeChildren) {
            removed(child);
        }

        return this;
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
