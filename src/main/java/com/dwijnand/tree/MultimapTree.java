package com.dwijnand.tree;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

/**
 * A <b>guaranteed</b> {@link MutableTree}-compliant implementation which uses
 * {@link Multimap} and {@link Map}.
 * <p>
 * This class is final so all instances of it are guaranteed to be compliant.
 * See {@link Tree} for details about compliance and immutability guarantees.
 *
 * @param <T> the type of the nodes in the tree
 */
public final class MultimapTree<T> extends MutableTree<T> {

    /**
     * The supplier of mutable multimap instances, used to hold the
     * parent-children relationships of the tree.
     */
    private final NewMutableMultimapsSupplier<? extends Multimap<T, T>, T, T> childrenMultimapSupplier;

    /**
     * The supplier of mutable map instances, used to hold the child-parent
     * relationships of the tree.
     */
    private final NewMutableMapsSupplier<? extends Map<T, T>, T, T> parentsMapSupplier;

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
     * Creates a new multimap tree using the specified {@link Multimap} and
     * {@link Map} suppliers. These suppliers are used to obtain multimaps and
     * maps required to construct the parent-children and child-parent
     * relationships within the tree.
     * <p>
     * Suppliers of multimaps and maps are required, as opposed to simply a
     * multimap and a map, because all the 'modifying' methods actually return
     * new instances of this tree, and therefore require a new multimap and map.
     *
     * @param <T> the type of the nodes in the tree
     * @param childrenMultimapSupplier a Multimap supplier
     * @param parentsMapSupplier a Map supplier
     * @return a new multimap tree
     */
    public static <T> MultimapTree<T> create(
            final NewMutableMultimapsSupplier<? extends Multimap<T, T>, T, T> childrenMultimapSupplier,
            final NewMutableMapsSupplier<? extends Map<T, T>, T, T> parentsMapSupplier) {
        checkNotNull(childrenMultimapSupplier);
        checkNotNull(parentsMapSupplier);

        final Multimap<T, T> children = childrenMultimapSupplier.get();
        final Map<T, T> parents = parentsMapSupplier.get();
        return new MultimapTree<T>(childrenMultimapSupplier,
                parentsMapSupplier, children, parents, null);
    }

    /**
     * Creates a copy of the specified multimap tree, by creating a new tree
     * with a copy of the associations in the specified tree and the same root
     * node.
     *
     * @param <T> the type of the nodes in the trees
     * @param multimapTree a multimap tree
     * @return a new copy of the specified multimap tree
     */
    public static <T> MultimapTree<T> copyOf(
            final MultimapTree<T> multimapTree) {
        checkNotNull(multimapTree);

        final NewMutableMultimapsSupplier<? extends Multimap<T, T>, T, T> childrenMultimapSupplier = multimapTree.childrenMultimapSupplier;
        final NewMutableMapsSupplier<? extends Map<T, T>, T, T> parentsMapSupplier = multimapTree.parentsMapSupplier;

        final Multimap<T, T> children = childrenMultimapSupplier.get();
        final Map<T, T> parents = parentsMapSupplier.get();

        children.putAll(multimapTree.children);
        parents.putAll(multimapTree.parents);

        return new MultimapTree<T>(childrenMultimapSupplier,
                parentsMapSupplier, children, parents, multimapTree.root);
    }

    /**
     * Creates a new multimap tree with the specified suppliers, associations
     * and root node. See {@link #create(Supplier, Supplier)} for more details.
     * <p>
     * This sole constructor is private so that it may not be instantiated
     * outside of this class.
     *
     * @param childrenMultimapSupplier a Multimap supplier
     * @param parentsMapSupplier a Map supplier
     * @param children the parent-children associations to be used
     * @param parents the child-parent associations to be used
     * @param root the root node
     */
    private MultimapTree(
            final NewMutableMultimapsSupplier<? extends Multimap<T, T>, T, T> childrenMultimapSupplier,
            final NewMutableMapsSupplier<? extends Map<T, T>, T, T> parentsMapSupplier,
            final Multimap<T, T> children, final Map<T, T> parents,
            final T root) {
        this.childrenMultimapSupplier = childrenMultimapSupplier;
        this.parentsMapSupplier = parentsMapSupplier;
        this.children = children;
        this.parents = parents;
        this.root = root;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.TreeSpec#contains(java.lang.Object)
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
     * @see com.dwijnand.tree.TreeSpec#getParent(java.lang.Object)
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
    public Collection<T> getChildren(final T node) {
        return children.get(checkNotNull(node));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.TreeSpec#getRoot()
     */
    @Override
    public T getRoot() {
        return root;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.MutableTree#withRoot(java.lang.Object)
     */
    @Override
    public MultimapTree<T> withRoot(final T node) {
        checkNotNull(node);
        final MultimapTree<T> multimapTree = create(
                childrenMultimapSupplier, parentsMapSupplier);
        multimapTree.root = node;
        return multimapTree;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.MutableTree#setRoot(java.lang.Object)
     */
    @Override
    public MultimapTree<T> setRoot(final T node) {
        checkNotNull(node);
        clear();
        root = node;
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.MutableTree#add(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public MultimapTree<T> add(final T parent, final T child) {
        checkNotNull(parent);
        checkNotNull(child);
        checkArgument(contains(parent),
                "%s does not contain parent node %s", getClass()
                        .getSimpleName(), parent);

        if (contains(child)) {
            return this;
        }

        final MultimapTree<T> multimapTree = copyOf(this);
        multimapTree.addedInternal(parent, child);
        return multimapTree;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.MutableTree#added(java.lang.Object,
     * java.lang.Object)
     */
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

        addedInternal(parent, child);
        return this;
    }

    /**
     * Adds a new parent/child association to the tree.
     *
     * @param parent the parent node
     * @param child the child node
     */
    private void addedInternal(final T parent, final T child) {
        children.put(parent, child);
        parents.put(child, parent);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.MutableTreeSpec#clear()
     */
    @Override
    public void clear() {
        children.clear();
        parents.clear();
        root = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.MutableTree#remove(java.lang.Object)
     */
    @Override
    public MultimapTree<T> remove(final T node) {
        checkNotNull(node);

        if (node == root) {
            // optimisation
            return create(childrenMultimapSupplier, parentsMapSupplier);
        }

        if (!contains(node)) {
            return this;
        }

        final MultimapTree<T> multimapTree = copyOf(this);
        multimapTree.removed(node);
        return multimapTree;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.MutableTree#removed(java.lang.Object)
     */
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
