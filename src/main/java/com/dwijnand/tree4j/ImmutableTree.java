package com.dwijnand.tree4j;

/**
 * TODO revise javadoc
 * This interface extends the {@link Tree} interface, with the additional
 * restriction that the object must be immutable (i.e. <strong>no</strong>
 * method should change the internal state of the object).
 *
 * @param <T> the type of the nodes in the tree
 */
public interface ImmutableTree<T> extends Tree<T> {
    /**
     * Creates a new tree with the specified node set as the root.
     *
     * @param node a node
     * @return a new tree with the specified node set as the root
     */
    ImmutableTree<T> withRoot(T node);

    /**
     * Creates a new tree containing the same root and associations of this tree
     * as well as a new association for the specified parent and child nodes,
     * unless this tree doesn't contain the parent node, in which case it throws
     * an {@link IllegalArgumentException}.
     *
     * @param parent the parent node
     * @param child  the child node
     * @return a new tree containing all the associations of this tree, the same
     *         node set as root and the new parent/child association
     * @throws IllegalArgumentException if this tree doesn't contain the parent
     *                                  node
     */
    ImmutableTree<T> plus(T parent, T child);

    /**
     * Creates a new tree containing the same root and associations of this
     * tree, excluding the the specified node and its children nodes,
     * recursively. If this tree doesn't contain the specified node, this tree
     * is returned.
     *
     * @param node a node
     * @return a new tree without the specified node and its children
     */
    ImmutableTree<T> minus(T node);
}
