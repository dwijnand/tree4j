package com.dwijnand.tree.immutable;

import com.google.common.collect.ImmutableCollection;

public abstract class Tree<T> implements com.dwijnand.tree.Tree<T> {

    @Override
    public abstract ImmutableCollection<T> getChildren(T node);

    @Override
    public abstract Tree<T> withRoot(T node);

    @Override
    public abstract Tree<T> add(final T parent, final T child);

    @Override
    public abstract Tree<T> remove(T node);

}
