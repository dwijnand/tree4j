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
 * A <b>guaranteed</b> compliant implementation of {@link ImmutableTree} that
 * uses {@link ImmutableMultimap} and {@link ImmutableMap}.
 * <p>
 * This class is final so all instances of it are guaranteed to be compliant,
 * and therefore immutable. See {@link Tree} for details about compliance and
 * immutability guarantees.
 *
 * @param <T> the type of the nodes in the tree
 */
public final class ImmutableMultimapTree<T> extends ImmutableTree<T> {

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
     * parent-children and child-parent relationships within the tree.
     * <p>
     * Suppliers of builders are required, as opposed to simply builders,
     * because all the 'modifying' methods actually return new instances of this
     * tree, and therefore require new builders.
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
     * Creates a new immutable multimap tree with the specified suppliers,
     * associations and root node. See
     * {@link #create(Supplier, Supplier, ImmutableMultimap, ImmutableMap, T)}
     * for details.
     *
     * @param childrenBuilderSupplier an ImmutableMultimap builder supplier
     * @param parentsBuilderSupplier an ImmutableMap builder supplier
     * @param children the parent-children associations to be used
     * @param parents the child-parent associations to be used
     * @param root the root node
     */
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

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.Tree#contains(java.lang.Object)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.Tree#getParent(java.lang.Object)
     */
    @Override
    public T getParent(final T node) {
        return parents.get(checkNotNull(node));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.TreeSpec#getChildren(java.lang.Object)
     */
    @Override
    public ImmutableCollection<T> getChildren(final T node) {
        return children.get(checkNotNull(node));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.Tree#getRoot()
     */
    @Override
    public T getRoot() {
        return root;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.ImmutableTree#withRoot(java.lang.Object)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.ImmutableTree#add(java.lang.Object,
     * java.lang.Object)
     */
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
        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderSupplier
                .get();

        childrenBuilder.putAll(children);
        parentsBuilder.putAll(parents);

        addInternal(parent, child, childrenBuilder, parentsBuilder);

        final ImmutableMultimap<T, T> children = childrenBuilder.build();
        final ImmutableMap<T, T> parents = parentsBuilder.build();

        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                children, parents, root);
    };

    /**
     * Adds a new parent/child association to the specified builders.
     *
     * @param parent the parent node
     * @param child the child node
     * @param childrenBuilder the parent-children association builder
     * @param parentsBuilder the child-parent association builder
     */
    private void addInternal(final T parent, final T child,
            final ImmutableMultimap.Builder<T, T> childrenBuilder,
            final ImmutableMap.Builder<T, T> parentsBuilder) {
        childrenBuilder.put(parent, child);
        parentsBuilder.put(child, parent);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.ImmutableTree#remove(java.lang.Object)
     */
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

        addRecursivelyExcludingNode(root, node, childrenBuilder,
                parentsBuilder);

        final ImmutableMultimap<T, T> children = childrenBuilder.build();
        final ImmutableMap<T, T> parents = parentsBuilder.build();

        return create(childrenBuilderSupplier, parentsBuilderSupplier,
                children, parents, root);
    }

    /**
     * For each child of the specified node that
     * <em>isn't the excluded node</em>, it adds a new association for the
     * specified node and its child in the parent-children and child-parent
     * builders and then invokes this method again passing the child as the
     * specified node, propagating all other arguments.
     * <p>
     * The effect of this is that, after the recursion, the builders hold all
     * the associations of this tree, except the exclude node and all of its
     * children.
     *
     * @param node a node
     * @param excludeNode the excluded node
     * @param childrenBuilder a builder of parent-child associations
     * @param parentsBuilder a builder of the child-parent associations
     */
    private void addRecursivelyExcludingNode(final T node,
            final T excludeNode,
            final ImmutableMultimap.Builder<T, T> childrenBuilder,
            final ImmutableMap.Builder<T, T> parentsBuilder) {
        final ImmutableCollection<T> nodeChildren = getChildren(node);
        for (final T child : nodeChildren) {
            if (child != excludeNode) {
                addInternal(node, child, childrenBuilder, parentsBuilder);
                addRecursivelyExcludingNode(child, excludeNode,
                        childrenBuilder, parentsBuilder);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method uses reflection to determine whether the specified object is
     * equal to this tree.
     */
    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method uses reflection to build the returned hash code.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
