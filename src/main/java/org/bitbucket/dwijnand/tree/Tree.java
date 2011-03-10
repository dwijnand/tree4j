package org.bitbucket.dwijnand.tree;

import java.util.Collection;

/**
 * A tree structure or arborescence.
 * <p>
 * Specifically it is an directed acyclic graph with a root node. Each non-root
 * node of the tree has a parent and zero or more children. The order of the
 * children of each node is either natural ordering or determined by a specified
 * {@link java.util.Comparator Comparator} .
 * <p>
 * Passing {@code null} where a node is expected always throws a
 * {@link NullPointerException}.
 *
 * @param <T> the type of the nodes in the tree
 */
public interface Tree<T> {

    // Query methods

    /**
     * Tests whether the specified node is in the tree.
     *
     * @param node the node
     * @return {@code true} if the tree contains the specified node, false
     *         otherwise.
     */
    boolean contains(Object node);

    /**
     * Retrieves the parent of the specified node.
     *
     * @param node the node on which to get the parent
     * @return the parent of the specified node, or {@code null} if the tree
     *         doesn't contain the specified node
     */
    T getParent(T node);

    /**
     * Retrieves a collection view of the children for the specified node.
     *
     * @param node the node on which to get the children
     * @return the children of the specified node, or an empty collection if
     *         none found
     */
    Collection<T> getChildren(T node);

    /**
     * Retrieves the root.
     *
     * @return the root, or {@code null} if no root is set
     */
    T getRoot();

    // Adding methods

    /**
     * Creates a new tree with the specified node set as the root.
     *
     * @param node the node
     * @return a new tree with the specified node set as root
     */
    Tree<T> withRoot(T node);

    /**
     * Creates a new tree containing the new parent/child association, all the
     * parent/child associations of this tree and the same node set as root,
     * unless the tree doesn't contain the parent node, in which case it throws
     * an {@link IllegalArgumentException}.
     *
     * @param parent the parent node
     * @param child the child node
     * @return a new tree containing all the associations of this tree, the same
     *         node set as root and the parent/child association
     * @throws IllegalArgumentException if tree doesn't contain parent node
     */
    Tree<T> add(T parent, T child);

    // Removing methods

    /**
     * Creates a new tree without the specified node and all of its children
     * nodes.
     *
     * @param node the node
     * @return a new tree without the specified node and its children
     */
    Tree<T> remove(T node);

}
