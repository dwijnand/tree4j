package com.dwijnand.tree.immutable;

// TODO guaranteed, constructor
public abstract class Tree<T> implements com.dwijnand.tree.Tree<T> {

    Tree() {
    }

    @Override
    public abstract Tree<T> withRoot(T node);

    @Override
    public abstract Tree<T> add(final T parent, final T child);

    @Override
    public abstract Tree<T> remove(T node);

}
