package org.bitbucket.dwijnand.tree;

/**
 * A mutable tree structure or arborescence.
 * 
 * @param <T> the types of the nodes in the tree
 */
public interface MutableTree<T> extends Tree<T> {

    /**
     * Sets the specified node as the root and removes all existing nodes.
     * 
     * @param node the node to set as the new root
     * @return the mutable tree itself
     */
    MutableTree<T> setRoot(T node);

    /**
     * Adds a new parent/child association.
     * 
     * @param parent the parent node
     * @param child the child node
     * @return the mutable tree itself
     */
    // TODO decide what add does when MutableTree doesn't contain parent node
    MutableTree<T> added(T parent, T child);

    // Removing methods

    /**
     * Removes all the nodes in the tree.
     */
    void clear();

    /**
     * Removes the specified node and all of its children nodes from the tree.
     * 
     * @param node the node
     * @return the mutable tree itself
     */
    MutableTree<T> removed(T node);

}
