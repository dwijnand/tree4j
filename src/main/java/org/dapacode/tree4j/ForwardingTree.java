package org.dapacode.tree4j;

import com.google.common.collect.ForwardingObject;

import java.util.Collection;

// TODO document this class entirely
public abstract class ForwardingTree<T> extends ForwardingObject implements Tree<T> {
  protected ForwardingTree() {}

  @Override
  protected abstract Tree<T> delegate();

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
}
