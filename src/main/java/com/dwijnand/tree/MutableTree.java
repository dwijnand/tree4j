package com.dwijnand.tree;

/**
 * A base class that extends {@link Tree} and implements and <b>guarantees</b>
 * compliance with {@link MutableTreeSpec}. See {@link Tree} for details about
 * compliance and immutability guarantees.
 *
 * @param <T> the type of the nodes in the tree
 */
public abstract class MutableTree<T> extends Tree<T> implements
        MutableTreeSpec<T> {

    /**
     * Creates a new instance. This sole constructor is package-private so that
     * it may not be subclassed outside of this package.
     */
    MutableTree() {
    }

    // Override methods that return self to restrict the return type to
    // GuaranteedMutableTree

    @Override
    public abstract MutableTree<T> withRoot(T node);

    @Override
    public abstract MutableTree<T> setRoot(T node);

    @Override
    public abstract MutableTree<T> add(final T parent, final T child);

    @Override
    public abstract MutableTree<T> added(T parent, T child);

    @Override
    public abstract MutableTree<T> remove(T node);

    @Override
    public abstract MutableTree<T> removed(T node);

}
