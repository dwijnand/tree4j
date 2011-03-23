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
 * This class is final so all instances of it are guaranteed to be immutable.
 * See {@link GuaranteedTree} for more details on immutability guarantee.
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
     * Creates a new immutable multimap tree using the specified
     * {@link ImmutableMultimap} and {@link ImmutableMap} builder suppliers.
     * These suppliers are used to obtain builders required to constructing the
     * parent-children and child-parent relationships within this tree.
     * <p>
     * Suppliers of builders are required, as opposed to simply builders, as all
     * the 'modifying' methods actually return new instances of this tree, and
     * therefore require fresh builders.
     *
     * @param <T> the type of the nodes in the tree
     * @param childrenBuilderSupplier an ImmutableMultimap builder supplier
     * @param parentsBuilderSupplier an ImmutableMap builder supplier
     * @return a new immutable multimap tree
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

        final ImmutableMultimap<T, T> children = childrenBuilder.build();
        final ImmutableMap<T, T> parents = parentsBuilder.build();

        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                children, parents, null);
    }

    /**
     * Creates a new immutable multimap tree using the specified
     * {@link ImmutableMultimap} and {@link ImmutableMap} builder suppliers,
     * children ImmutableMultimap, parents ImmutableMap and root node. See
     * {@link #create(Supplier, Supplier)} for more details.
     * <p>
     * This method is private because it uses the specified children and parents
     * directly, without validating their correctness. Also it accepts
     * <code>null</code> as the specified root node.
     *
     * @param <T> the type of the nodes in the tree
     * @param childrenBuilderSupplier an ImmutableMultimap builder supplier
     * @param parentsBuilderSupplier an ImmutableMap builder supplier
     * @param children the parent-children associations to be used
     * @param parents the child-parent associations to be used
     * @param root the root node
     * @return a new immutable multimap tree with the specified associations and
     *         root node
     */
    private static <T> ImmutableMultimapTree<T> create(
            final Supplier<? extends ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier,
            final Supplier<? extends ImmutableMap.Builder<T, T>> parentsBuilderSupplier,
            final ImmutableMultimap<T, T> children,
            final ImmutableMap<T, T> parents, final T root) {
        return new ImmutableMultimapTree<T>(childrenBuilderSupplier,
                parentsBuilderSupplier, children, parents, root);
    }

    /**
     * Creates a copy of the specified immutable multimap tree.
     *
     * @param <T> the type of the nodes in the trees
     * @param immutableTree an immutable multimap tree
     * @return a new immutable multimap tree
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
        final ImmutableMultimap<T, T> children = childrenBuilder.build();

        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderSupplier
                .get();
        parentsBuilder.putAll(immutableTree.parents);
        final ImmutableMap<T, T> parents = parentsBuilder.build();

        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                children, parents, root);
    }

    // TODO javadoc accepts null root
    private ImmutableMultimapTree(
            final Supplier<? extends ImmutableMultimap.Builder<T, T>> childrenBuilderSupplier,
            final Supplier<? extends ImmutableMap.Builder<T, T>> parentsBuilderSupplier,
            final ImmutableMultimap<T, T> children,
            final ImmutableMap<T, T> parents, final T root) {
        this.childrenBuilderSupplier = childrenBuilderSupplier;
        this.parentsBuilderSupplier = parentsBuilderSupplier;
        this.children = children;
        this.parents = parents;
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
        checkNotNull(node);

        final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenBuilderSupplier
                .get();
        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderSupplier
                .get();

        final ImmutableMultimap<T, T> children = childrenBuilder.build();
        final ImmutableMap<T, T> parents = parentsBuilder.build();

        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                children, parents, node);
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

        final ImmutableMultimap<T, T> children = childrenBuilder.build();
        final ImmutableMap<T, T> parents = parentsBuilder.build();

        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                children, parents, root);
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

        final ImmutableMultimap<T, T> children = childrenBuilder.build();
        final ImmutableMap<T, T> parents = parentsBuilder.build();

        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                children, parents, root);
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
