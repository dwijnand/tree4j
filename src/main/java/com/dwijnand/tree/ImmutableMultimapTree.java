package com.dwijnand.tree;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

/**
 * A guaranteed immutable tree structure which uses {@link ImmutableMultimap}.
 * See {@link Tree} for more details on what is intended by tree structure.
 * <p>
 * This class is final and has private constructors, so any instance of it is
 * guaranteed to be immutable.
 *
 * @param <T> the type of the nodes in the tree
 */
public final class ImmutableMultimapTree<T> extends GuaranteedTree<T> {

    /**
     * The supplier of immutable multimap builder instances, used to build the
     * parent-children relationships of a new multimap tree.
     */
    private final Supplier<? extends ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier;

    /**
     * The supplier of immutable map builder instances, used to build the
     * child-parent relationships of a new multimap tree.
     */
    private final Supplier<? extends ImmutableMap.Builder<T, T>> parentsBuilderSupplier;

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
     * Creates a new multimap tree using the specified {@link ImmutableMultimap}
     * and {@link ImmutableMap} builder suppliers. These suppliers are used to
     * obtain builders required to constructing the parent-children and
     * child-parent relationships within this tree.
     * <p>
     * Suppliers of builders are required, as opposed to simply builders, as all
     * the 'modifying' methods actually return new instances of this tree, and
     * therefore require fresh builders.
     *
     * @param <T> the type of the nodes in the tree
     * @param childrenBuilderSupplier an {@link ImmutableMultimap} builder
     *            supplier
     * @param parentsBuilderSupplier an {@link ImmutableMap} builder supplier
     * @return a new multimap tree
     */
    public static <T> ImmutableMultimapTree<T> create(
            final Supplier<? extends ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier,
            final Supplier<? extends ImmutableMap.Builder<T, T>> parentsBuilderSupplier) {
        checkNotNull(childrenBuilderSupplier);
        checkNotNull(parentsBuilderSupplier);
        final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenBuilderSupplier
                .get();
        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderSupplier
                .get();
        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                childrenBuilder, parentsBuilder, null);
    }

    private static <T> ImmutableMultimapTree<T> create(
            final Supplier<? extends ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier,
            final Supplier<? extends ImmutableMap.Builder<T, T>> parentsBuilderSupplier,
            final T root) {
        final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenBuilderSupplier
                .get();
        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderSupplier
                .get();
        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                childrenBuilder, parentsBuilder, root);
    }

    /**
     * Creates a copy of the specified ImmutableTree.
     *
     * @param <T> the type of the nodes in the new and specified tree
     * @param immutableTree the ImmutableTree
     * @return a new ImmutableTree
     */
    public static <T> ImmutableMultimapTree<T> copyOf(
            final ImmutableMultimapTree<T> immutableTree) {

        checkNotNull(immutableTree);

        final Supplier<? extends ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier = immutableTree.childrenBuilderSupplier;
        final Supplier<? extends ImmutableMap.Builder<T, T>> parentsBuilderSupplier = immutableTree.parentsBuilderSupplier;
        final T root = immutableTree.root;

        final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenBuilderSupplier
                .get();
        childrenBuilder.putAll(immutableTree.children);

        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderSupplier
                .get();
        parentsBuilder.putAll(immutableTree.parents);

        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                childrenBuilder, parentsBuilder, root);
    }

    private static <T> ImmutableMultimapTree<T> create(
            final Supplier<? extends ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier,
            final Supplier<? extends ImmutableMap.Builder<T, T>> parentsBuilderSupplier,
            final ImmutableMultimap.Builder<T, T> childrenBuilder,
            final ImmutableMap.Builder<T, T> parentsBuilder, final T root) {
        return new ImmutableMultimapTree<T>(childrenBuilderSupplier,
                parentsBuilderSupplier, childrenBuilder, parentsBuilder,
                root);
    }

    private ImmutableMultimapTree(
            final Supplier<? extends ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier,
            final Supplier<? extends ImmutableMap.Builder<T, T>> parentsBuilderSupplier,
            final ImmutableMultimap.Builder<T, T> childrenBuilder,
            final ImmutableMap.Builder<T, T> parentsBuilder, final T root) {
        this.childrenBuilderSupplier = childrenBuilderSupplier;
        this.parentsBuilderSupplier = parentsBuilderSupplier;
        this.children = childrenBuilder.build();
        this.parents = parentsBuilder.build();
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
    public ImmutableCollection<T> getChildren(final T node) {
        return children.get(checkNotNull(node));
    }

    @Override
    public T getRoot() {
        return root;
    }

    @Override
    public ImmutableMultimapTree<T> withRoot(final T node) {
        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                checkNotNull(node));
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
    public ImmutableMultimapTree<T> remove(final T node) {
        checkNotNull(node);
        if (node == root) {
            // optimisation
            return create(childrenBuilderSupplier, parentsBuilderSupplier);
        }

        if (!contains(node)) {
            return this;
        }

        final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenBuilderSupplier
                .get();
        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderSupplier
                .get();

        addRecursivelyFilteringNode(node, childrenBuilder, parentsBuilder,
                root);

        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                childrenBuilder, parentsBuilder, root);
    }

    private void addRecursivelyFilteringNode(final T excludeNode,
            final ImmutableMultimap.Builder<T, T> childrenBuilder,
            final ImmutableMap.Builder<T, T> parentsBuilder, final T node) {
        final ImmutableCollection<T> nodeChildren = getChildren(node);
        for (final T child : nodeChildren) {
            if (child != excludeNode) {
                addInternal(childrenBuilder, parentsBuilder, node, child);
                addRecursivelyFilteringNode(excludeNode, childrenBuilder,
                        parentsBuilder, child);
            }
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
