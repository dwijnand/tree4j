package com.dwijnand.tree4j;

import com.dwijnand.tree4j.common.MutableMap;
import com.dwijnand.tree4j.common.MutableMaps;
import com.dwijnand.tree4j.common.MutableMultimap;
import com.dwijnand.tree4j.common.MutableMultimaps;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A {@link MutableTree} implementation which uses a mutable {@link Multimap}
 * and a mutable {@link Map}.
 *
 * @param <T> the type of the nodes in the tree
 */
public class MultimapTree<T> implements MutableTree<T> {
    /**
     * The parent-children associations of the tree.
     */
    private final MutableMultimap<T, T> children;

    /**
     * The child-parent associations of the tree.
     */
    private final MutableMap<T, T> parents;

    /**
     * The root of the tree.
     */
    private T root;

    /**
     * Creates a new multimap tree. This constructor is used directly by
     * {@link #create(MutableMultimap, MutableMap)} so look there for details.
     *
     * @param children the parent-children associations to be used
     * @param parents  the child-parent associations to be used
     */
    protected MultimapTree(final MutableMultimap<T, T> children,
                           final MutableMap<T, T> parents) {
        this.children = checkNotNull(children);
        this.parents = checkNotNull(parents);
        this.root = null;
    }

    /**
     * Creates a new multimap tree backed by an {@link ArrayListMultimap} and a
     * {@link LinkedHashMap}.
     *
     * @param <T> the type of the nodes in the tree
     * @return a new multimap tree
     */
    public static <T> MultimapTree<T> create() {
        return create(MutableMultimaps.wrap(ArrayListMultimap.<T, T>create()),
                MutableMaps.wrap(Maps.<T, T>newLinkedHashMap()));
    }

    /**
     * Creates a new multimap tree using the specified {@link Multimap} and
     * {@link Map} factories. These factories are used to obtain multimaps and
     * maps required to construct the parent-children and child-parent
     * associations within the tree.
     * <p/>
     * Factories of multimaps and maps are required, as opposed to simply a
     * multimap and a map, because some of the methods return a new instance of
     * the tree, and, therefore, require a new multimap and map.
     *
     * @param <T>      the type of the nodes in the tree
     * @param children the parent-children associations to be used
     * @param parents  the child-parent associations to be used
     * @return a new multimap tree
     */
    public static <T> MultimapTree<T> create(
            final MutableMultimap<T, T> children,
            final MutableMap<T, T> parents) {
        return new MultimapTree<T>(children, parents);
    }

    /**
     * Creates a copy of the specified tree, by creating a new tree with a copy
     * of the associations in the specified tree and the same root node.
     *
     * @param <T>  the type of the nodes in the trees
     * @param tree a tree
     * @return a new copy of the specified tree
     */
    // TODO check and test this!
    public static <T> MultimapTree<T> copyOf(final Tree<T> tree) {
        checkNotNull(tree);

        final MultimapTree<T> multimapTree = create();
        if (tree instanceof MultimapTree) {
            final MultimapTree<T> original = (MultimapTree<T>) tree;

            multimapTree.root = original.root;
            multimapTree.children.putAll(original.children);
            multimapTree.parents.putAll(original.parents);
        } else {
            multimapTree.setRoot(tree.getRoot());

            for (final Map.Entry<T, T> entry : tree) {
                multimapTree.add(entry.getKey(), entry.getValue());
            }
        }

        return multimapTree;
    }

    @Override
    public boolean contains(final T node) {
        return node != null && (node == root || parents.containsKey(node));
    }

    @Override
    public T getParent(final T node) {
        checkNotNull(node);
        checkArgument(contains(node), "The tree doesn't contain the specified"
                + " node: %s", node);
        return parents.get(node);
    }

    @Override
    public Collection<T> getChildren(final T node) {
        checkNotNull(node);
        checkArgument(contains(node), "The tree doesn't contain the specified"
                + " node: %s", node);
        return children.get(node);
    }

    @Override
    public T getRoot() {
        return root;
    }

    @Override
    public Iterator<Map.Entry<T, T>> iterator() {
        return children.entries().iterator();
    }

    @Override
    public boolean setRoot(final T node) {
        checkNotNull(node);
        if (root == node) {
            return false;
        } else {
            // clear first, then set the root, otherwise the root is cleared too
            clear();
            root = node;
            return true;
        }
    }

    @Override
    public boolean add(final T parent, final T child) {
        checkNotNull(parent);
        checkNotNull(child);
        checkArgument(contains(parent), "The tree doesn't contain the specified"
                + " parent node: %s", parent);
        checkArgument(!parents.containsKey(child), "The child node (%s) is " +
                "already associated to another node", child);

        if (parents.get(child) == parent) {
            return false;
        }

        children.put(parent, child);
        parents.put(child, parent);

        return true;
    }

    @Override
    public void clear() {
        children.clear();
        parents.clear();
        root = null;
    }

    @Override
    public boolean remove(final T node) {
        checkNotNull(node);
        checkArgument(contains(node), "The tree doesn't contain the specified"
                + " node: %s", node);

        if (node == root) {
            // optimisation
            root = null;
            clear();
            return true;
        }

        Collection<T> nodeChildren = children.get(node);
        // A copy is required here to avoid a ConcurrentModificationException
        nodeChildren = ImmutableList.copyOf(nodeChildren);

        children.removeAll(node);
        children.get(getParent(node)).remove(node);
        parents.remove(node);

        for (final T child : nodeChildren) {
            remove(child);
        }

        return true;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This method uses reflection to determine whether the specified object is
     * equal to this tree.
     */
    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This method uses reflection to build the returned hash code.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
