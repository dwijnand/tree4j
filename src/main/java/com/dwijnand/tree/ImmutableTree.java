package com.dwijnand.tree;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

/**
 * A immutable tree structure or arborescence.
 * 
 * @param <T> the types of the nodes in the tree
 */
public class ImmutableTree<T> implements Tree<T> {

    private final Supplier<ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier;

    private final Supplier<ImmutableMap.Builder<T, T>> parentsBuilderSupplier;

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

    public static <T> ImmutableTree<T> create(
            final Supplier<ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier,
            final Supplier<ImmutableMap.Builder<T, T>> parentsBuilderSupplier) {
        return create(checkNotNull(childrenBuilderSupplier),
                checkNotNull(parentsBuilderSupplier), null);
    }

    private static <T> ImmutableTree<T> create(
            final Supplier<ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier,
            final Supplier<ImmutableMap.Builder<T, T>> parentsBuilderSupplier,
            final T root) {
        // accepts null for root
        return new ImmutableTree<T>(childrenBuilderSupplier,
                parentsBuilderSupplier, root);
    }

    private static <T> ImmutableTree<T> create(
            final Supplier<ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier,
            final Supplier<ImmutableMap.Builder<T, T>> parentsBuilderSupplier,
            final ImmutableMultimap.Builder<T, T> childrenBuilder,
            final ImmutableMap.Builder<T, T> parentsBuilder, final T root) {
        return new ImmutableTree<T>(childrenBuilderSupplier,
                parentsBuilderSupplier, childrenBuilder, parentsBuilder,
                root);
    }

    /**
     * Creates a copy of the specified ImmutableTree.
     * 
     * @param <T> the type of the nodes in the new and specified tree
     * @param immutableTree the ImmutableTree
     * @return a new ImmutableTree
     */
    public static <T> ImmutableTree<T> copyOf(
            final ImmutableTree<T> immutableTree) {
        return new ImmutableTree<T>(checkNotNull(immutableTree));
    }

    public ImmutableTree(
            final Supplier<ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier,
            final Supplier<ImmutableMap.Builder<T, T>> parentsBuilderSupplier) {
        this.childrenBuilderSupplier = checkNotNull(childrenBuilderSupplier);
        this.parentsBuilderSupplier = checkNotNull(parentsBuilderSupplier);
        children = childrenBuilderSupplier.get().build();
        parents = parentsBuilderSupplier.get().build();
        root = null;
    }

    private ImmutableTree(
            final Supplier<ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier,
            final Supplier<ImmutableMap.Builder<T, T>> parentsBuilderSupplier,
            final T root) {
        this.childrenBuilderSupplier = childrenBuilderSupplier;
        this.parentsBuilderSupplier = parentsBuilderSupplier;
        this.children = childrenBuilderSupplier.get().build();
        this.parents = parentsBuilderSupplier.get().build();
        this.root = root;
    }

    private ImmutableTree(
            final Supplier<ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier,
            final Supplier<ImmutableMap.Builder<T, T>> parentsBuilderSupplier,
            final ImmutableMultimap.Builder<T, T> childrenBuilder,
            final ImmutableMap.Builder<T, T> parentsBuilder, final T root) {
        this.childrenBuilderSupplier = childrenBuilderSupplier;
        this.parentsBuilderSupplier = parentsBuilderSupplier;
        this.children = childrenBuilder.build();
        this.parents = parentsBuilder.build();
        this.root = root;
    }

    private ImmutableTree(final ImmutableTree<T> immutableTree) {
        childrenBuilderSupplier = immutableTree.childrenBuilderSupplier;
        parentsBuilderSupplier = immutableTree.parentsBuilderSupplier;
        root = immutableTree.root;

        final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenBuilderSupplier
                .get();
        childrenBuilder.putAll(immutableTree.children);
        children = childrenBuilder.build();

        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderSupplier
                .get();
        parentsBuilder.putAll(immutableTree.parents);
        parents = parentsBuilder.build();
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
    public ImmutableCollection<T> getChildren(final T node) {
        return children.get(checkNotNull(node));
    }

    @Override
    public T getRoot() {
        return root;
    }

    @Override
    public ImmutableTree<T> withRoot(final T node) {
        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                checkNotNull(node));
    };

    @Override
    public ImmutableTree<T> add(final T parent, final T child) {
        checkNotNull(parent);
        checkNotNull(child);
        checkArgument(contains(parent),
                "%s does not contain parent node %s", getClass()
                        .getSimpleName(), parent);

        if (contains(child)) {
            return this;
        }

        final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenBuilderSupplier
                .get();
        childrenBuilder.putAll(children);

        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderSupplier
                .get();
        parentsBuilder.putAll(parents);

        addInternal(childrenBuilder, parentsBuilder, parent, child);

        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                childrenBuilder, parentsBuilder, root);
    };

    private void addInternal(
            final ImmutableMultimap.Builder<T, T> childrenBuilder,
            final ImmutableMap.Builder<T, T> parentsBuilder, final T parent,
            final T child) {
        childrenBuilder.put(parent, child);
        parentsBuilder.put(child, parent);
    }

    @Override
    public ImmutableTree<T> remove(final T node) {
        checkNotNull(node);
        if (node == root) {
            // optimisation
            return create(childrenBuilderSupplier, parentsBuilderSupplier);
        }
        final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenBuilderSupplier
                .get();
        childrenBuilder.putAll(children);

        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderSupplier
                .get();
        parentsBuilder.putAll(parents);

        removeRecursively(node, childrenBuilder, parentsBuilder);

        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                childrenBuilder, parentsBuilder, root);
    }

    private void removeRecursively(final T node,
            final ImmutableMultimap.Builder<T, T> childrenBuilder,
            final ImmutableMap.Builder<T, T> parentsBuilder) {

        final ImmutableMultimap<T, T> children = childrenBuilder.build();
        ImmutableCollection<T> nodeChildren = children.get(node);
        // A copy is required here to avoid a ConcurrentModificationException
        nodeChildren = ImmutableList.copyOf(nodeChildren);

        // TODO make this work
// childrenBuilder.removeAll(node);
// childrenBuilder.get(getParent(node)).remove(node);
// parentsBuilder.remove(node);

        for (final T child : nodeChildren) {
            removeRecursively(child, childrenBuilder, parentsBuilder);
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
