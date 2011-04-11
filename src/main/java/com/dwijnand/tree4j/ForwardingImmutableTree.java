package com.dwijnand.tree4j;

import java.util.Collection;

public abstract class ForwardingImmutableTree<T> extends ForwardingTree<T>
        implements ImmutableTree<T> {

    protected ForwardingImmutableTree() {
    }

    @Override
    protected abstract ImmutableTree<T> delegate();

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
    public ImmutableTree<T> withRoot(T node) {
        return delegate().withRoot(node);
    }

    @Override
    public ImmutableTree<T> add(T parent, T child) {
        return delegate().add(parent, child);
    }

    @Override
    public ImmutableTree<T> remove(T node) {
        return delegate().remove(node);
    }
}
