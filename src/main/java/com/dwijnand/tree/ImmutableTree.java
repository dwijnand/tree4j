package com.dwijnand.tree;

/**
 * This interface extends the {@link Tree} interface, with the additional
 * restriction that the object must be immutable (i.e. <strong>no</strong>
 * method should change the internal state of the object).
 *
 * @param <T> the type of the nodes in the tree
 */
public interface ImmutableTree<T> extends Tree<T> {

    // Override methods that return self to change the return type to
    // ImmutableTree

    @Override
    public abstract ImmutableTree<T> withRoot(final T node);

    @Override
    public abstract ImmutableTree<T> add(final T parent, final T child);

    @Override
    public abstract ImmutableTree<T> remove(final T node);

}
