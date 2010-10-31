package org.bitbucket.dwijnand.tree;

import java.util.Collection;
import java.util.Comparator;

/**
 * An object that represents a tree structure, or in graph-theory an
 * arborescence. Specifically it is an acyclic directed graph with a root node.
 * Each non-root node of the {@link Tree} has a parent and zero or more
 * children. The order of the children of each node is either natural ordering
 * or determined by a specified {@link Comparator}.
 * <p>
 * {@link Tree} will not accept {@code null} as a node in any of its methods,
 * throwing a {@link NullPointerException} if a null is passed.
 * 
 * @param <T> the type of the nodes in the tree
 */
public interface Tree<T> {

    // Query methods

    /**
     * Returns {@code true} if the {@link Tree} contains the specified node.
     * 
     * @param node node to check for
     * @return {@code true} if the tree contains the specified node.
     */
    boolean contains(Object node);

    /**
     * Returns the parent of the specified node.
     * 
     * @param node node on which to get the parent
     * @return the parent of the specified node
     */
    // TODO Define what getParent does if the Tree doesn't contain the node
    T getParent(T node);

    /**
     * Returns a collection view of the children for the specified node. If the
     * specified node doesn't have any children, an empty collection is
     * returned.
     * 
     * @param node node on which to get the children
     * @return the children of the specified node
     */
    // TODO Define what getChildren does if the Tree doesn't contain the node
    Collection<T> getChildren(T node);

    /**
     * Returns the root of the {@link Tree}.
     * 
     * @return the root of the {@link Tree}
     */
    T getRoot();

    // Adding methods

    /**
     * Removes all the nodes in the {@link Tree} and sets the specified node as
     * the root.
     * 
     * @param node node to set as the new root
     */
    void setRoot(T node);

    /**
     * Adds the specified child node to the {@link Tree}, associated to the
     * specified parent node. Returns {@code true} if the {@link Tree} changed.
     * If the {@link Tree} doesn't contain the parent node or already contains
     * the child node, returns false.
     * 
     * @param parent node on which to associate the child node
     * @param child node to add
     * @return {@code true} if the {@link Tree} changed, or {@code false} if
     *         {@link Tree} didn't contain the parent node or already contained
     *         the child node
     */
    boolean add(T parent, T child);

    // Removing methods

    /**
     * Removes all the nodes in the {@link Tree}.
     */
    void clear();

    /**
     * Removes the specified node from the {@link Tree}, as well as all of its
     * children nodes.
     * 
     * @param node node to remove
     */
    void remove(T node);

}
