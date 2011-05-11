package com.dwijnand.tree4j;

import java.util.Collection;
import java.util.Map;

/**
 * TODO revise javadoc
 * This interface defines the specification of a tree structure, also known as
 * an arborescence.
 * <p/>
 * Specifically it represents a directed acyclic graph (DAG) with the following:
 * <ul>
 * <li>a root node (unless the tree is empty)</li>
 * <li>each node has zero or more children nodes</li>
 * <li>each non-root node has a parent node</li>
 * </ul>
 * <p/>
 * Passing {@code null} where a node is expected always throws a
 * {@link NullPointerException}.
 * <p/>
 * This interface doesn't specify any methods that modify the internal state of
 * the tree- all 'modifying' methods are defined to return a new instance of the
 * tree, so this interface <em>may</em> be used as the basis of an immutable
 * object.
 * <p/>
 * See {@link ImmutableTree} for an extension to this interface that adds the
 * immutability restriction and {@link MutableTree} for an extension that adds
 * in-place modification methods.
 *
 * @param <T> the type of the nodes in the tree
 * @see ImmutableTree
 * @see MutableTree
 */
public interface Tree<T> extends Iterable<Map.Entry<T, T>> {
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
}
