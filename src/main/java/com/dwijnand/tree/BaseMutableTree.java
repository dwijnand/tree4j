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
 * A base implementation of a mutable tree, using {@link Map} and
 * {@link Multimap}.
 * <p>
 * An instance in constructed using the specified multimap and map suppliers, or
 * from another {@link BaseMutableTree}.
 * 
 * @param <T> the type of the nodes in the tree
 * @param <U> the type of the multimap to hold the parent-child relationship
 * @param <V> the type of the map to hold the child-parent relationship
 */
public class BaseMutableTree<T, U extends Multimap<T, T>, V extends Map<T, T>>
        implements MutableTree<T> {

    private final Supplier<U> childrenMultimapSupplier;

    private final Supplier<V> parentsMapSupplier;

    private final U children;

    private final V parents;

    private T root;

    public static <T, U extends Multimap<T, T>, V extends Map<T, T>> BaseMutableTree<T, U, V> create(
            final Supplier<U> childrenMultimapSupplier,
            final Supplier<V> parentsMapSupplier) {
        final U children = childrenMultimapSupplier.get();
        final V parents = parentsMapSupplier.get();
        return new BaseMutableTree<T, U, V>(childrenMultimapSupplier,
                parentsMapSupplier, children, parents);
    }

    public static <T, U extends Multimap<T, T>, V extends Map<T, T>> BaseMutableTree<T, U, V> copyOf(
            final BaseMutableTree<T, U, V> baseMutableTree) {

        final Supplier<U> childrenMultimapSupplier = baseMutableTree.childrenMultimapSupplier;
        final Supplier<V> parentsMapSupplier = baseMutableTree.parentsMapSupplier;
        final U children = childrenMultimapSupplier.get();
        final V parents = parentsMapSupplier.get();
        children.putAll(baseMutableTree.children);
        parents.putAll(baseMutableTree.parents);

        return new BaseMutableTree<T, U, V>(childrenMultimapSupplier,
                parentsMapSupplier, children, parents);
    }

    public BaseMutableTree(final Supplier<U> childrenMultimapSupplier,
            final Supplier<V> parentsMapSupplier, final U children,
            final V parents) {
        this.childrenMultimapSupplier = childrenMultimapSupplier;
        this.parentsMapSupplier = parentsMapSupplier;
        this.children = children;
        this.parents = parents;
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
    public BaseMutableTree<T, U, V> withRoot(final T node) {
        checkNotNull(node);
        final BaseMutableTree<T, U, V> baseMutableTree = createNew();
        baseMutableTree.root = node;
        return baseMutableTree;
    }

    @Override
    public BaseMutableTree<T, U, V> setRoot(final T node) {
        checkNotNull(node);
        clear();
        root = node;
        return this;
    }

    @Override
    public BaseMutableTree<T, U, V> add(final T parent, final T child) {
        final BaseMutableTree<T, U, V> baseMutableTree = copyOf(this);
        baseMutableTree.added(parent, child);
        return baseMutableTree;
    }

    @Override
    public BaseMutableTree<T, U, V> added(final T parent, final T child) {
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
    public BaseMutableTree<T, U, V> remove(final T node) {
        checkNotNull(node);
        if (node == root) {
            // optimisation
            return createNew();
        }
        final BaseMutableTree<T, U, V> baseMutableTree = copyOf(this);
        baseMutableTree.removed(node);
        return baseMutableTree;
    }

    @Override
    public BaseMutableTree<T, U, V> removed(final T node) {
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

    protected BaseMutableTree<T, U, V> createNew() {
        return create(childrenMultimapSupplier, parentsMapSupplier);
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
