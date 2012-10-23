package org.dapacode.tree4j;

import com.google.common.collect.ForwardingObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

// TODO document this class entirely
public abstract class DelegatingTree<T> extends ForwardingObject implements Tree<T> {
  protected DelegatingTree() {}

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

  @Override
  public Iterator<Map.Entry<T, T>> iterator() {
    return delegate().iterator();
  }
}
