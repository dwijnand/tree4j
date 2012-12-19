package org.dapacode.tree4j;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * An immutable tree which delegates all its method calls to another immutable tree. Subclasses should override one or more
 * methods to modify the behavior of the backing immutable tree as desired per the
 * <a href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * @param <T> the type of the nodes in the tree
 */
@SuppressWarnings("DesignForExtension") // Methods are specifically designed to be overridden with their functionality ignored
public abstract class DelegatingImmutableTree<T> extends DelegatingTree<T> implements ImmutableTree<T> {
  protected DelegatingImmutableTree() {}

  @Override
  protected abstract ImmutableTree<T> delegate();

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
  @Nullable
  public T getRoot() {
    return delegate().getRoot();
  }

  @Override
  public ImmutableTree<T> withRoot(final T node) {
    return delegate().withRoot(node);
  }

  @Override
  public ImmutableTree<T> added(final T parent, final T child) {
    return delegate().added(parent, child);
  }

  @Override
  public ImmutableTree<T> removed(final T node) {
    return delegate().removed(node);
  }
}
