package org.dapacode.tree4j;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * A {@link MutableTree} implementation which uses a mutable {@link com.google.common.collect.SetMultimap SetMultimap} and a
 * mutable {@link Map}.
 *
 * @param <T> the type of the nodes in the tree
 */
public final class MultimapTree<T> extends AbstractMultimapTree<T> implements MutableTree<T> {
  /** The root of the tree. */
  private T root;

  /**
   * Creates a new multimap tree. This constructor is used directly by {@link #create(SetMultimap, Map)} so look there for
   * details.
   *
   * @param children the parent-children associations to be used
   * @param parents the child-parent associations to be used
   */
  protected MultimapTree(final SetMultimap<T, T> children, final Map<T, T> parents) {
    super(checkNotNull(children), checkNotNull(parents));
  }

  /**
   * Creates a new multimap tree backed by an {@link LinkedHashMultimap} and a {@link java.util.LinkedHashMap LinkedHashMap}.
   *
   * @param <T> the type of the nodes in the tree
   * @return a new multimap tree
   */
  public static <T> MultimapTree<T> create() {
    return create(LinkedHashMultimap.<T, T>create(), Maps.<T, T>newLinkedHashMap());
  }

  /**
   * Creates a new multimap tree using the specified {@link com.google.common.collect.Multimap Multimap} and {@link Map}
   * factories. These factories are used to obtain multimaps and maps required to construct the parent-children and child-parent
   * associations within the tree.
   * <p/>
   * Factories of multimaps and maps are required, as opposed to simply a multimap and a map, because some of the methods return
   * a new instance of the tree, and, therefore, require a new multimap and map.
   *
   * @param <T> the type of the nodes in the tree
   * @param children the parent-children associations to be used
   * @param parents the child-parent associations to be used
   * @return a new multimap tree
   */
  public static <T> MultimapTree<T> create(final SetMultimap<T, T> children, final Map<T, T> parents) {
    return new MultimapTree<T>(children, parents);
  }

  /**
   * Creates a copy of the specified tree, by creating a new tree with a copy of the associations in the specified tree and the
   * same root node.
   *
   * @param <T> the type of the nodes in the trees
   * @param tree a tree
   * @return a new copy of the specified tree
   */
  // TODO check and test this!
  public static <T> MultimapTree<T> copyOf(final Tree<T> tree) {
    checkNotNull(tree);

    final MultimapTree<T> multimapTree = create();
    if (tree instanceof MultimapTree) { // Optimisation
      final MultimapTree<T> original = (MultimapTree<T>) tree;

      multimapTree.root = original.root;
      multimapTree.children.putAll(original.children);
      multimapTree.parents.putAll(original.parents);
    } else {
      multimapTree.setRoot(tree.getRoot());

      Collection<Map.Entry<T, T>> entriesDepthFirst = Trees.getEntriesDepthFirst(tree);
      for (Map.Entry<T, T> entry : entriesDepthFirst) {
        multimapTree.add(entry.getKey(), entry.getValue());
      }
    }

    return multimapTree;
  }

    @Override
  public T getRoot() {
    return root;
  }

  @Override
  public boolean setRoot(final T node) {
    checkNotNull(node);
    if (root == node) {
      return false;
    } else {
      // clear first, then set the root, otherwise the root is cleared too
      clear();
      root = node;
      return true;
    }
  }

  @Override
  public boolean add(final T parent, final T child) {
    checkNotNull(parent);
    checkNotNull(child);
    checkArgument(contains(parent), "The tree doesn't contain the specified parent node: %s", parent);

    T childParent = parents.get(child);
    if (childParent == parent) {
      return false;
    }

    checkArgument(childParent == null, "The child node (%s) is already associated to another node", child);

    children.put(parent, child);
    parents.put(child, parent);

    return true;
  }

  @Override
  public void clear() {
    children.clear();
    parents.clear();
    root = null;
  }

  @Override
  public boolean remove(final T node) {
    checkNotNull(node);
    checkArgument(contains(node), "The tree doesn't contain the specified node: %s", node);

    if (node == root) { // optimisation
      clear();
      return true;
    }

    Collection<T> nodeChildren = children.get(node);
    nodeChildren = ImmutableList.copyOf(nodeChildren); // Required to avoid a ConcurrentModificationException

    children.removeAll(node);
    children.get(getParent(node)).remove(node);
    parents.remove(node);

    for (final T child : nodeChildren) {
      remove(child);
    }

    return true;
  }

  /**
   * {@inheritDoc}
   * <p/>
   * This method uses reflection to determine whether the specified object is equal to this tree.
   */
  @Override
  public boolean equals(final Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  /**
   * {@inheritDoc}
   * <p/>
   * This method uses reflection to build the returned hash code.
   */
  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
