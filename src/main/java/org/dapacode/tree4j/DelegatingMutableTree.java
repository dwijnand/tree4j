package org.dapacode.tree4j;

import java.util.Collection;

/**
 * An mutable tree which delegates all its method calls to another mutable tree. Subclasses should override one or more methods
 * to modify the behavior of the backing mutable tree as desired per the
 * <a href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * @param <T> the type of the nodes in the tree
 */
@SuppressWarnings("DesignForExtension") // Methods are specifically designed to be overridden with their functionality ignored
public abstract class DelegatingMutableTree<T> extends DelegatingTree<T> implements MutableTree<T> {
  protected DelegatingMutableTree() {}

  @Override
  protected abstract MutableTree<T> delegate();

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
  public boolean setRoot(final T node) {
    return delegate().setRoot(node);
  }

  @Override
  public boolean add(final T parent, final T child) {
    return delegate().add(parent, child);
  }

  @Override
  public void clear() {
    delegate().clear();
  }

  @Override
  public boolean remove(final T node) {
    return delegate().remove(node);
  }
}
