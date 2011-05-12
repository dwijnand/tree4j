package com.dwijnand.tree4j;

/**
 * An extension of the {@link Tree} interface with added state-mutating methods.
 * <p/>
 * <strong>Note:</strong><br/>
 * A {@link NullPointerException} will <em>always</em> by thrown if
 * <code>null</code> is passed were a node is expected, while an
 * {@link IllegalArgumentException} will by thrown when a node which isn't
 * contained in the tree, is passed to {@link #add(Object, Object)} or
 * {@link #remove(Object)}.
 *
 * @param <T> the type of the nodes in the tree
 */
public interface MutableTree<T> extends Tree<T> {
    /**
     * Sets the specified node as the root of the tree.
     *
     * @param node a node
     * @return {@code true} if the tree was modified (the root node was changed)
     */
    boolean setRoot(T node);

    /**
     * Adds a new parent/child association to the tree.
     *
     * @param parent the parent node, an existing node of the tree
     * @param child  the child node, a node not already associated to another
     *               node, which isn't the specified parent node // TODO test this
     * @return {@code true} if the tree was modified (new association was added)
     */
    boolean add(T parent, T child);

    /**
     * Removes all the nodes in the tree.
     */
    void clear();

    /**
     * Removes the specified node and all of its children nodes from the tree.
     *
     * @param node a node of the tree
     * @return @{code true} if the tree was modified (the node and its children
     *         nodes were removed)
     */
    boolean remove(T node);
}
