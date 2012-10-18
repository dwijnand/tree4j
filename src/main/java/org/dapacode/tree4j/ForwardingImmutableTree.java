package org.dapacode.tree4j;

import java.util.Collection;

// TODO document this class entirely
public abstract class ForwardingImmutableTree<T> extends ForwardingTree<T> implements ImmutableTree<T> {
  protected ForwardingImmutableTree() {}

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
