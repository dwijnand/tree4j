package com.dwijnand.tree4j;

import java.util.Collection;

public abstract class ForwardingMutableTree<T> extends ForwardingTree<T>
        implements MutableTree<T> {

    protected ForwardingMutableTree() {
    }

    @Override
    protected abstract MutableTree<T> delegate();

    @Override
    public boolean contains(T node) {
        return delegate().contains(node);
    }

    @Override
    public T getParent(T node) {
        return delegate().getParent(node);
    }

    @Override
    public Collection<T> getChildren(T node) {
        return delegate().getChildren(node);
    }

    @Override
    public T getRoot() {
        return delegate().getRoot();
    }

    @Override
    public MutableTree<T> withRoot(T node) {
        return delegate().withRoot(node);
    }

    @Override
    public MutableTree<T> setRoot(T node) {
        return delegate().setRoot(node);
    }

    @Override
    public MutableTree<T> plus(T parent, T child) {
        return delegate().plus(parent, child);
    }

    @Override
    public MutableTree<T> add(T parent, T child) {
        return delegate().add(parent, child);
    }

    @Override
    public void clear() {
        delegate().clear();
    }

    @Override
    public MutableTree<T> minus(T node) {
        return delegate().minus(node);
    }

    @Override
    public MutableTree<T> remove(T node) {
        return delegate().remove(node);
    }
}
