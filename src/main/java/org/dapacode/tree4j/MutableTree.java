package org.dapacode.tree4j;

/**
 * An extension of the {@link Tree} interface with added state-mutating methods.
 * <p/>
 * <strong>Note:</strong> A {@link NullPointerException} will <em>always</em> by thrown if {@code null} is passed were a node is
 * expected.
 *
 * @param <T> the type of the nodes in the tree
 */
public interface MutableTree<T> extends Tree<T> {
  /**
   * Sets the specified node as the root of the tree, removing all previous nodes unless the specified node already was the root
   * node.
   *
   * @param node a node
   * @return {@code true} if the tree was modified (the root node was changed)
   */
  boolean setRoot(T node);

  /**
   * Adds a new parent/child association to the tree.
   *
   * @param parent the parent node, an existing node of the tree
   * @param child the child node, a node not already associated to another node, which isn't the specified parent node
   * @return {@code true} if the tree was modified (new association was added)
   * @throws IllegalArgumentException if the specified node isn't contained in the tree
   */
  boolean add(T parent, T child);

  /** Removes all the nodes in the tree. */
  void clear();

  /**
   * Removes the specified node and all of its children nodes from the tree.
   *
   * @param node a node of the tree
   * @return @{code true} if the tree was modified (the node and its children nodes were removed)
   * @throws IllegalArgumentException if the specified node isn't contained in the tree
   */
  boolean remove(T node);
}
