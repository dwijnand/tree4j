package com.dwijnand.tree4j;

/**
 * This interface extends the {@link Tree} interface with in-place modification
 * methods, making it the specification of a mutable tree.
 *
 * @param <T> the type of the nodes in the tree
 */
public interface MutableTree<T> extends Tree<T> {
    /**
     * Sets the specified node as the root and removes all existing nodes.
     *
     * @param node a node
     * @return the mutable tree itself
     */
    MutableTree<T> setRoot(T node);

    /**
     * Adds a new parent/child association, unless the tree doesn't contain the
     * parent node, in which case it throws an {@link IllegalArgumentException}.
     *
     * @param parent the parent node
     * @param child  the child node
     * @return the mutable tree itself
     * @throws IllegalArgumentException if the tree doesn't contain parent node
     */
    MutableTree<T> add(T parent, T child);

    /**
     * Removes all the nodes in the tree.
     */
    void clear();

    /**
     * Removes the specified node and all of its children nodes from the tree.
     *
     * @param node a node
     * @return the mutable tree itself
     */
    MutableTree<T> remove(T node);
}
