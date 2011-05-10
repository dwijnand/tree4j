package com.dwijnand.tree4j;

import com.google.common.collect.ForwardingObject;
import java.util.Collection;

public abstract class ForwardingTree<T> extends ForwardingObject
        implements Tree<T> {

    protected ForwardingTree() {
    }

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
    public Tree<T> withRoot(final T node) {
        return delegate().withRoot(node);
    }

    @Override
    public Tree<T> plus(final T parent, final T child) {
        return delegate().plus(parent, child);
    }

    @Override
    public Tree<T> minus(final T node) {
        return delegate().minus(node);
    }
}
