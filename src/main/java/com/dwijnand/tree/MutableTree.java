package com.dwijnand.tree;

/**
 * A mutable tree structure or arborescence. See {@link Tree} for more details
 * of what is intended by a tree structure.
 * <p>
 * This interface specifies methods for modifying the internal state of the tree
 * and returning itself. There is, however, no way to enforce that the
 * modification occurs on the instance itself (instead of returning a new
 * instance) and, therefore, there are no guarantees. See
 * {@link com.dwijnand.tree.mutable.Tree mutable.Tree} for a guaranteed mutable
 * tree.
 *
 * @param <T> the types of the nodes in the tree
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
     * @param child the child node
     * @return the mutable tree itself
     * @throws IllegalArgumentException if the tree doesn't contain parent node
     */
    MutableTree<T> added(T parent, T child);

    // Removing methods

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
    MutableTree<T> removed(T node);

}
