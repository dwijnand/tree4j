package org.dapacode.tree4j;

/**
 * An extension of the {@link Tree} interface with methods for returning modified copies of the tree. It also specifies
 * immutability, and, as such it, <strong>no</strong> subclasses or sub-interface may define or implement methods that change
 * the internal state of the tree (its root node and associations).
 * <p/>
 * <strong>Note:</strong> A {@link NullPointerException} will <em>always</em> by thrown if {@code null} is passed were a node is
 * expected. Also, any method which would return an <em>identical</em> copy of the tree, returns the tree instead.
 *
 * @param <T> the type of the nodes in the tree
 */
public interface ImmutableTree<T> extends Tree<T> {
  /**
   * Creates a new tree with the specified node set as the root, unless the specified node is already the root, in which case it
   * returns itself.
   *
   * @param node a node
   * @return a new tree with the specified root node or itself
   */
  ImmutableTree<T> withRoot(T node);

  /**
   * Creates a copy of this tree with additionally the specified parent-child association.
   *
   * @param parent the parent node, an existing node of the tree
   * @param child the child node, a node not already associated to another node, which isn't the specified parent node
   * @return a modified copy of this tree containing the new parent-child association
   * @throws IllegalArgumentException if the specified node isn't contained in the tree
   */
  ImmutableTree<T> plus(T parent, T child);

  /**
   * Creates a copy of this tree with without the specified mode and all of its children nodes, recursively.
   *
   * @param node a node of the tree
   * @return a modified copy of this tree without the specified node and its children
   * @throws IllegalArgumentException if the specified node isn't contained in the tree
   */
  ImmutableTree<T> minus(T node);
}
