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
    public Tree<T> withRoot(T node) {
        return delegate().withRoot(node);
    }

    @Override
    public Tree<T> add(T parent, T child) {
        return delegate().add(parent, child);
    }

    @Override
    public Tree<T> remove(T node) {
        return delegate().remove(node);
    }
}
