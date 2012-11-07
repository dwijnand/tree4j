package org.dapacode.tree4j;

import com.google.common.collect.SetMultimap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

public abstract class AbstractMultimapTree<T> implements Tree<T> {
  /** The parent-children associations of the tree. */
  protected final SetMultimap<T, T> children;

  /** The child-parent associations of the tree. */
  protected final Map<T, T> parents;

  protected AbstractMultimapTree(final SetMultimap<T, T> children, final Map<T, T> parents) {
    this.children = children;
    this.parents = parents;
  }

  @Override
  public final int size() {
    return getRoot() == null ? 0 : 1 + children.size();
  }

  @Override
  public final boolean contains(final T node) {
    checkNotNull(node);
    return node == getRoot() || parents.containsKey(node);
  }

  @Override
  public final T getParent(final T node) {
    checkNotNull(node);
    checkArgument(contains(node), "The tree doesn't contain the specified node: %s", node);
    return parents.get(node);
  }

  protected final Collection<T> getChildren0(final T node) {
    checkNotNull(node);
    checkArgument(contains(node), "The tree doesn't contain the specified node: %s", node);
    return children.get(node);
  }

  @Override
  public final Iterator<Map.Entry<T, T>> iterator() {
    return children.entries().iterator();
  }

  @Override
  public final boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || !(obj instanceof AbstractMultimapTree)) {
      return false;
    }

    final AbstractMultimapTree<?> that = (AbstractMultimapTree<?>) obj;

    final T thisRoot = getRoot();
    final Object thatRoot = that.getRoot();

    return (thisRoot == null ? thatRoot == null : thisRoot.equals(thatRoot))
        && children.equals(that.children) && parents.equals(that.parents);
  }

  @Override
  public final int hashCode() {
    final T root = getRoot();
    int result = root == null ? 0 : root.hashCode();
    result = 31 * result + children.hashCode();
    result = 31 * result + parents.hashCode();
    return result;
  }
}
