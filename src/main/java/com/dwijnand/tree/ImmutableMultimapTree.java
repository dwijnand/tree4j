package com.dwijnand.tree;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

/**
 * A immutable tree backed by a {@link ImmutableMultimap}.
 * 
 * @param <T> the type of the nodes in the tree
 */
public final class ImmutableMultimapTree<T> implements ImmutableTree<T> {

    /**
     * The parent-children relationships of the tree.
     */
    private final ImmutableMultimap<T, T> children;

    /**
     * The child-parent relationships of the tree.
     */
    private final ImmutableMap<T, T> parents;

    /**
     * The root of the tree.
     */
    private final T root;

    /**
     * Creates a new ImmutableMultimapTree.
     * 
     * @param <T> the type of the nodes in the new tree
     * @return a new ImmutableMultimapTree
     * @throws NullPointerException if the specified comparator is null
     */
    public static <T> ImmutableMultimapTree<T> create() {
        return new ImmutableMultimapTree<T>((T) null);
    }

    /**
     * Creates a new ImmutableMultimapTree from the specified
     * ImmutableMultimapTree.
     * 
     * @param <T> the type of the nodes in the new and specified tree
     * @param ImmutableMultimapTree the ImmutableMultimapTree
     * @return a new ImmutableMultimapTree
     */
    public static <T> ImmutableMultimapTree<T> create(
            final ImmutableMultimapTree<T> ImmutableMultimapTree) {
        return new ImmutableMultimapTree<T>(
                checkNotNull(ImmutableMultimapTree));
    }

    private static <T> ImmutableMultimapTree<T> create(final T root) {
        return new ImmutableMultimapTree<T>(root);
    }

    private static <T> ImmutableMultimapTree<T> create(
            final Multimap<T, T> children, final Map<T, T> parents,
            final T root) {
        return new ImmutableMultimapTree<T>(children, parents, root);
    }

    private ImmutableMultimapTree(final T root) {
        children = ImmutableSetMultimap.of();
        parents = ImmutableMap.of();
        this.root = root;
    }

    private ImmutableMultimapTree(
            final ImmutableMultimapTree<T> immutableMultimapTree) {
        children = ImmutableSetMultimap
                .copyOf(immutableMultimapTree.children);
        parents = ImmutableMap.copyOf(immutableMultimapTree.parents);
        root = immutableMultimapTree.root;
    }

    private ImmutableMultimapTree(final Multimap<T, T> children,
            final Map<T, T> parents, final T root) {
        this.children = ImmutableSetMultimap.copyOf(children);
        this.parents = ImmutableMap.copyOf(parents);
        this.root = root;
    }

    @Override
    public boolean contains(final T node) {
        if (node == null) {
            return false;
        } else if (node == root) {
            return true;
        } else {
            return parents.containsKey(node);
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
    public ImmutableMultimapTree<T> withRoot(final T node) {
        return create(checkNotNull(node));
    };

    @Override
    public ImmutableMultimapTree<T> add(final T parent, final T child) {
        checkNotNull(parent);
        checkNotNull(child);
        checkArgument(contains(parent),
                "%s does not contain parent node %s", getClass()
                        .getSimpleName(), parent);

        if (contains(child)) {
            return this;
        }

        final Multimap<T, T> newChildren = LinkedHashMultimap
                .create(children);
        newChildren.put(parent, child);

        final Map<T, T> newParents = Maps.newHashMap(parents);
        newParents.put(child, parent);

        return create(newChildren, newParents, root);
    };

    @Override
    public ImmutableMultimapTree<T> remove(final T node) {
        checkNotNull(node);
        if (node == root) {
            // optimisation
            return create();
        }
        final Multimap<T, T> modifiableChildren = LinkedHashMultimap
                .create(children);
        final Map<T, T> modifiableParents = Maps.newHashMap(parents);
        removeRecursively(node, modifiableChildren, modifiableParents);
        return create(modifiableChildren, modifiableParents, root);
    };

    private void removeRecursively(final T node,
            final Multimap<T, T> children, final Map<T, T> parents) {
        Collection<T> nodeChildren = children.get(node);
        // A copy is required here to avoid a ConcurrentModificationException
        nodeChildren = ImmutableList.copyOf(nodeChildren);

        children.removeAll(node);
        children.get(getParent(node)).remove(node);
        parents.remove(node);

        for (final T child : nodeChildren) {
            removeRecursively(child, children, parents);
        }
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
