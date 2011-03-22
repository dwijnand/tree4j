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
 * @param <T> the types of the nodes in the tree
 */
public class BaseMutableTree<T> implements MutableTree<T> {

    private final Supplier<Multimap<T, T>> childrenMultimapSupplier;

    private final Supplier<Map<T, T>> parentsMapSupplier;

    private final Multimap<T, T> children;

    private final Map<T, T> parents;

    private T root;

    public static <T> BaseMutableTree<T> create(
            final Supplier<Multimap<T, T>> childrenMultimapSupplier,
            final Supplier<Map<T, T>> parentsMapSupplier) {
        return new BaseMutableTree<T>(childrenMultimapSupplier,
                parentsMapSupplier);
    }

    public static <T> BaseMutableTree<T> copyOf(
            final BaseMutableTree<T> baseMutableTree) {
        return new BaseMutableTree<T>(baseMutableTree);
    }

    public BaseMutableTree(
            final Supplier<Multimap<T, T>> childrenMultimapSupplier,
            final Supplier<Map<T, T>> parentsMapSupplier) {
        this.childrenMultimapSupplier = childrenMultimapSupplier;
        this.parentsMapSupplier = parentsMapSupplier;
        children = childrenMultimapSupplier.get();
        parents = parentsMapSupplier.get();
    }

    public BaseMutableTree(final BaseMutableTree<T> baseMutableTree) {
        this(baseMutableTree.childrenMultimapSupplier,
                baseMutableTree.parentsMapSupplier);
        children.putAll(baseMutableTree.children);
        parents.putAll(baseMutableTree.parents);
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
    public BaseMutableTree<T> withRoot(final T node) {
        checkNotNull(node);
        final BaseMutableTree<T> baseMutableTree = createNew();
        baseMutableTree.root = node;
        return baseMutableTree;
    }

    @Override
    public BaseMutableTree<T> setRoot(final T node) {
        checkNotNull(node);
        clear();
        root = node;
        return this;
    }

    @Override
    public BaseMutableTree<T> add(final T parent, final T child) {
        final BaseMutableTree<T> baseMutableTree = copyOf(this);
        baseMutableTree.added(parent, child);
        return baseMutableTree;
    }

    @Override
    public BaseMutableTree<T> added(final T parent, final T child) {
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
    public BaseMutableTree<T> remove(final T node) {
        checkNotNull(node);
        if (node == root) {
            // optimisation
            return createNew();
        }
        final BaseMutableTree<T> baseMutableTree = copyOf(this);
        baseMutableTree.removed(node);
        return baseMutableTree;
    }

    @Override
    public BaseMutableTree<T> removed(final T node) {
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

    protected BaseMutableTree<T> createNew() {
        return new BaseMutableTree<T>(childrenMultimapSupplier,
                parentsMapSupplier);
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
