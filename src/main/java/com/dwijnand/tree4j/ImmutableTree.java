package com.dwijnand.tree4j;

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
    ImmutableTree<T> withRoot(T node);

    @Override
    ImmutableTree<T> plus(T parent, T child);

    @Override
    ImmutableTree<T> minus(T node);

}
