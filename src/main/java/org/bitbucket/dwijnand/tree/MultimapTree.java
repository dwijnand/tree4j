package org.bitbucket.dwijnand.tree;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

/**
 * A mutable {@link Tree} backed by a {@link Multimap}.
 * 
 * @param <T> the type of the nodes in the tree
 */
public final class MultimapTree<T> implements MutableTree<T> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(MultimapTree.class);

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
     * The comparator used for the ordering of the Multimap keys and values.
     */
    private final Comparator<T> comparator;

    /**
     * Creates a {@link MultimapTree} whose nodes are ordered according to the
     * natural ordering of type T.
     * 
     * @param <T> the type of the nodes in the new tree
     * @return a new {@link MultimapTree}
     */
    public static <T extends Comparable<T>> MultimapTree<T> create() {
        return new MultimapTree<T>(Ordering.<T> natural());
    }

    /**
     * Creates a {@link MultimapTree} whose nodes are ordered according to the
     * specified {@link Comparator}.
     * 
     * @param <T> the type of the nodes in the new tree
     * @param comparator the
     * @return a new {@link MultimapTree}
     * @throws NullPointerException if the specified comparator is null
     */
    public static <T> MultimapTree<T> create(final Comparator<T> comparator) {
        return new MultimapTree<T>(checkNotNull(comparator));
    }

    // TODO add javadoc for create-copy method
    public static <T> MultimapTree<T> create(
            final MultimapTree<T> multimapTree) {
        return new MultimapTree<T>(checkNotNull(multimapTree));
    }

    private MultimapTree(final Comparator<T> comparator) {
        this.comparator = comparator;
        children = TreeMultimap.create(comparator, comparator);
        parents = Maps.newHashMap();
    }

    private MultimapTree(final MultimapTree<T> multimapTree) {
        comparator = multimapTree.comparator;
        children = TreeMultimap.create(comparator, comparator);
        children.putAll(multimapTree.children);
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
        final T root = checkNotNull(node);
        final MultimapTree<T> multimapTree = new MultimapTree<T>(comparator);
        multimapTree.root = root;
        return multimapTree;
    };

    @Override
    public MultimapTree<T> setRoot(final T node) {
        root = checkNotNull(node);
        clear();
        return this;
    }

    @Override
    public MultimapTree<T> add(final T parent, final T child) {
        final MultimapTree<T> multimapTree = new MultimapTree<T>(this);
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
    }

    @Override
    public MultimapTree<T> remove(final T node) {
        final MultimapTree<T> multimapTree = new MultimapTree<T>(this);
        multimapTree.removed(node);
        return multimapTree;
    };

    @Override
    public MultimapTree<T> removed(final T node) {
        checkNotNull(node);
        LOGGER.debug("Removing node [{}]", node);
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
