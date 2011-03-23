package com.dwijnand.tree;

/**
 * This interface extends the {@link TreeSpec} interface with in-place
 * modification methods, making it the specification of a mutable tree.
 * <p>
 * See {@link GuaranteedMutableTree} for a guaranteed mutable tree.
 *
 * @param <T> the type of the nodes in the tree
 * @see GuaranteedMutableTree
 */
// TODO add details about specification compliance tests
public interface MutableTreeSpec<T> extends TreeSpec<T> {

    // Override to restrict return type to MutableTreeSpec's
    @Override
    MutableTreeSpec<T> withRoot(T node);

    /**
     * Sets the specified node as the root and removes all existing nodes.
     *
     * @param node a node
     * @return the mutable tree itself
     */
    MutableTreeSpec<T> setRoot(T node);

    // Override to restrict return type to MutableTreeSpec's
    @Override
    MutableTreeSpec<T> add(T parent, T child);

    /**
     * Adds a new parent/child association, unless the tree doesn't contain the
     * parent node, in which case it throws an {@link IllegalArgumentException}.
     *
     * @param parent the parent node
     * @param child the child node
     * @return the mutable tree itself
     * @throws IllegalArgumentException if the tree doesn't contain parent node
     */
    MutableTreeSpec<T> added(T parent, T child);

    // Removing methods

    /**
     * Removes all the nodes in the tree.
     */
    void clear();

    // Override to restrict return type to MutableTreeSpec's
    @Override
    MutableTreeSpec<T> remove(T node);

    /**
     * Removes the specified node and all of its children nodes from the tree.
     *
     * @param node a node
     * @return the mutable tree itself
     */
    MutableTreeSpec<T> removed(T node);

}
