package org.bitbucket.dwijnand.tree;

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
public class MultimapTree<T> implements Tree<T> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(MultimapTree.class);

    /**
     * The parent-children relationships of the tree.
     */
    private final Multimap<T, T> nodes;

    /**
     * The child-parent relationships of the tree.
     */
    private final Map<T, T> parents = Maps.newHashMap();

    /**
     * The root of the tree.
     */
    private T root;

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

    private MultimapTree(final Comparator<T> comparator) {
        nodes = TreeMultimap.create(comparator, comparator);
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
        return nodes.get(checkNotNull(node));
    }

    @Override
    public T getRoot() {
        return root;
    }

    @Override
    public void setRoot(final T node) {
        clear();
        root = checkNotNull(node);
    }

    @Override
    public boolean add(final T parent, final T child) {
        checkNotNull(parent);
        checkNotNull(child);
        if (!contains(parent)) {
            return false;
        }
        if (contains(child)) {
            return false;
        }
        nodes.put(parent, child);
        parents.put(child, parent);
        return true;
    }

    @Override
    public void clear() {
        nodes.clear();
        parents.clear();
    }

    @Override
    public void remove(final T node) {
        LOGGER.debug("Removing {}", node);
        if (node == root) {
            root = null;
            clear();
            return;
        }
        checkNotNull(node);

        /*
         * We make a safe copy of the children as we are recursively removing
         * each node, and if we don't keep hold of a copy we risk running into a
         * java.util.ConcurrentModificationException.
         */
        final ImmutableList<T> children = ImmutableList.copyOf(nodes.get(node));

        nodes.removeAll(node);
        nodes.get(getParent(node)).remove(node);
        parents.remove(node);

        for (final T child : children) {
            remove(child);
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
