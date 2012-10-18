package com.dwijnand.tree4j;

import java.util.Collection;

// TODO document this class entirely
public abstract class ForwardingMutableTree<T> extends ForwardingTree<T> implements MutableTree<T> {
  protected ForwardingMutableTree() {}

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
