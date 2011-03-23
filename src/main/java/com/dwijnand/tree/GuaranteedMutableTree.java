package com.dwijnand.tree;


// TODO guaranteed, constructor
public abstract class GuaranteedMutableTree<T> implements MutableTree<T> {

    GuaranteedMutableTree() {
    }

    @Override
    public abstract GuaranteedMutableTree<T> withRoot(T node);

    @Override
    public abstract GuaranteedMutableTree<T> setRoot(T node);

    @Override
    public abstract GuaranteedMutableTree<T> add(final T parent,
            final T child);

    @Override
    public abstract GuaranteedMutableTree<T> added(T parent, T child);

    @Override
    public abstract GuaranteedMutableTree<T> remove(T node);

    @Override
    public abstract GuaranteedMutableTree<T> removed(T node);

}
