package com.dwijnand.tree;

import java.util.Collection;

/**
 * A tree structure or arborescence.
 * <p>
 * Specifically it is an directed acyclic graph with a root node with zero or
 * more children and where each non-root node has a parent and zero or more
 * children.
 * <p>
 * Passing {@code null} where a node is expected always throws a
 * {@link NullPointerException}.
 * <p>
 * This interface specifies that 'modifying' methods actually return a new
 * instance of the tree, making an instance of this type immutable. There
 * aren't, however, any guarantees for this as there is no way to enforce these
 * constraints.
 * <p>
 * See {@link com.dwijnand.tree.immutable.Tree immutable.Tree} for a guaranteed
 * immutable tree. Also, see {@link MutableTree} for in-place mutating methods.
 *
 * @param <T> the type of the nodes in the tree
 * @see com.dwijnand.tree.immutable.Tree immutable.Tree
 * @see MutableTree
 */
public interface Tree<T> {

    // Query methods

    /**
     * Tests whether the specified node is in the tree.
     *
     * @param node a node
     * @return {@code true} if the tree contains the specified node, false
     *         otherwise.
     */
    boolean contains(T node);

    /**
     * Retrieves the parent of the specified node.
     *
     * @param node a node
     * @return the parent of the specified node, or {@code null} if the tree
     *         doesn't contain the specified node
     */
    T getParent(T node);

    /**
     * Retrieves a collection view of the children for the specified node. The
     * order of the children within the collection depend on the implementation
     * of the tree.
     *
     * @param node a node
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
     * @param node a node
     * @return a new tree with the specified node set as the root
     */
    Tree<T> withRoot(T node);

    /**
     * Creates a new tree containing the same root and associations of this tree
     * as well as a new association for the specified parent and child nodes,
     * unless this tree doesn't contain the parent node, in which case it throws
     * an {@link IllegalArgumentException}.
     *
     * @param parent the parent node
     * @param child the child node
     * @return a new tree containing all the associations of this tree, the same
     *         node set as root and the new parent/child association
     * @throws IllegalArgumentException if this tree doesn't contain the parent
     *             node
     */
    Tree<T> add(T parent, T child);

    // Removing methods

    /**
     * Creates a new tree containing the same root and associations of this
     * tree, excluding the the specified node and its children nodes,
     * recursively. If this tree doesn't contain the specified node, this tree
     * is returned.
     *
     * @param node a node
     * @return a new tree without the specified node and its children
     */
    Tree<T> remove(T node);

}
