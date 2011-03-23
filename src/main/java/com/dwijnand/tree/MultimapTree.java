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
 * from another {@link MultimapTree}.
 *
 * @param <T> the type of the nodes in the tree
 */
// TODO guaranteed, constructor
public final class MultimapTree<T> extends GuaranteedMutableTree<T> {

    private final Supplier<? extends Multimap<T, T>> childrenMultimapSupplier;

    private final Supplier<? extends Map<T, T>> parentsMapSupplier;

    private final Multimap<T, T> children;

    private final Map<T, T> parents;

    private T root;

    public static <T> MultimapTree<T> create(
            final Supplier<? extends Multimap<T, T>> childrenMultimapSupplier,
            final Supplier<? extends Map<T, T>> parentsMapSupplier) {
        final Multimap<T, T> children = childrenMultimapSupplier.get();
        final Map<T, T> parents = parentsMapSupplier.get();
        return new MultimapTree<T>(childrenMultimapSupplier,
                parentsMapSupplier, children, parents, null);
    }

    public static <T> MultimapTree<T> copyOf(
            final MultimapTree<T> multimapTree) {
        final Supplier<? extends Multimap<T, T>> childrenMultimapSupplier = multimapTree.childrenMultimapSupplier;
        final Supplier<? extends Map<T, T>> parentsMapSupplier = multimapTree.parentsMapSupplier;
        final Multimap<T, T> children = childrenMultimapSupplier.get();
        final Map<T, T> parents = parentsMapSupplier.get();
        children.putAll(multimapTree.children);
        parents.putAll(multimapTree.parents);
        final T root = multimapTree.root;

        return new MultimapTree<T>(childrenMultimapSupplier,
                parentsMapSupplier, children, parents, root);
    }

    private MultimapTree(
            final Supplier<? extends Multimap<T, T>> childrenMultimapSupplier,
            final Supplier<? extends Map<T, T>> parentsMapSupplier,
            final Multimap<T, T> children, final Map<T, T> parents,
            final T root) {
        this.childrenMultimapSupplier = childrenMultimapSupplier;
        this.parentsMapSupplier = parentsMapSupplier;
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
        final MultimapTree<T> multimapTree = createNew();
        multimapTree.root = node;
        return multimapTree;
    }

    @Override
    public MultimapTree<T> setRoot(final T node) {
        checkNotNull(node);
        clear();
        root = node;
        return this;
    }

    @Override
    public MultimapTree<T> add(final T parent, final T child) {
        final MultimapTree<T> multimapTree = copyOf(this);
        multimapTree.added(parent, child);
        return multimapTree;
    }

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
            return createNew();
        }

        if (!contains(node)) {
            return this;
        }

        final MultimapTree<T> multimapTree = copyOf(this);
        multimapTree.removed(node);
        return multimapTree;
    }

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

    protected MultimapTree<T> createNew() {
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
