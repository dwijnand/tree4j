package org.dapacode.tree4j;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * A tree-like data structure, specifically an arborescence: a rooted, directed, acyclic graph, with all edges pointing away
 * from the root, and in which any two nodes are connected by maximum one directed path.
 * <p/>
 * <strong>Note:</strong> A {@link NullPointerException} will <em>always</em> by thrown if {@code null} is passed were a node is
 * expected.
 * <p/>
 * This interface only defines querying methods, so it <em>may</em> be used as the basis of an immutable object. See {@link
 * ImmutableTree} for an extension to this interface that adds the immutability restriction and {@link MutableTree} for an
 * extension that adds state-mutating methods.
 *
 * @param <T> the type of the nodes in the tree
 * @see ImmutableTree
 * @see MutableTree
 * @see <a href="http://en.wikipedia.org/wiki/Arborescence_(graph_theory)"> Arborescence (Wikipedia)</a>
 */
public interface Tree<T> extends Iterable<Map.Entry<T, T>> {
  /**
   * Returns the number of nodes in the tree.
   *
   * @return the number of nodes in the tree
   */
  int size();

  /**
   * Tests whether the specified node is in the tree.
   *
   * @param node a node
   * @return {@code true} if the tree contains the specified node
   */
  boolean contains(T node);

  /**
   * Retrieves the parent of the specified node.
   *
   * @param node a node of the tree
   * @return the parent of the specified node
   * @throws IllegalArgumentException if the specified node isn't contained in the tree
   */
  T getParent(T node);

  /**
   * Retrieves a collection view of the children for the specified node. The order of the children within the collection depend
   * on the implementation of the tree.
   *
   * @param node a node of the tree
   * @return the children nodes, or an empty collection if none found
   * @throws IllegalArgumentException if the specified node isn't contained in the tree
   */
  Collection<T> getChildren(T node);

  /**
   * Retrieves the root.
   *
   * @return the root, or {@code null} if no root is set
   */
  @Nullable
  T getRoot();
}
