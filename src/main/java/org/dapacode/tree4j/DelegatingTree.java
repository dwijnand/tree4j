package org.dapacode.tree4j;

import com.google.common.collect.ForwardingObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * A tree which delegates all its method calls to another tree. Subclasses should override one or more methods to modify the
 * behavior of the backing tree as desired per the <a href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator
 * pattern</a>.
 *
 * @param <T> the type of the nodes in the tree
 */
@SuppressWarnings("DesignForExtension") // Methods are specifically designed to be overridden with their functionality ignored
public abstract class DelegatingTree<T> extends ForwardingObject implements Tree<T> {
  protected DelegatingTree() {}

  @Override
  protected abstract Tree<T> delegate();

  @Override
  public int size() {
    return delegate().size();
  }

  @Override
  public boolean contains(final T node) {
    return delegate().contains(node);
  }

  @Override
  public T getParent(final T node) {
    return delegate().getParent(node);
  }

  @Override
  public Collection<T> getChildren(final T node) {
    return delegate().getChildren(node);
  }

  @Override
  public T getRoot() {
    return delegate().getRoot();
  }

  @Override
  public Iterator<Map.Entry<T, T>> iterator() {
    return delegate().iterator();
  }

  @Override
  public boolean equals(final Object obj) {
    return obj == this || delegate().equals(obj);
  }

  @Override
  public int hashCode() {
    return delegate().hashCode();
  }
}
