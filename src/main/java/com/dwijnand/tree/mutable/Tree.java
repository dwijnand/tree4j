package com.dwijnand.tree.mutable;

// TODO guaranteed, constructor
public abstract class Tree<T> implements com.dwijnand.tree.MutableTree<T> {

    Tree() {
    }

    @Override
    public abstract Tree<T> withRoot(T node);

    @Override
    public abstract Tree<T> setRoot(T node);

    @Override
    public abstract Tree<T> add(final T parent, final T child);

    @Override
    public abstract Tree<T> added(T parent, T child);

    @Override
    public abstract Tree<T> remove(T node);

    @Override
    public abstract Tree<T> removed(T node);

}
