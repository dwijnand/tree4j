package com.dwijnand.tree;

/**
 * A base class for implementing <b>guaranteed</b> mutable tree structures. See
 * {@link TreeSpec} for more details on what is intended by tree structure.
 * <p>
 * By guaranteed it is implied that the implementation of the methods defined in
 * {@link TreeSpec} <em>never</em> modify the internal state of the tree, but
 * rather return a new copy of this instance with the effect of the
 * modification.
 * <p>
 * This class is not final, however, it doesn't have any public or protected
 * constructors and, therefore, it cannot be subclasses outside of its package.
 * <p>
 * See {@link Tree} for more details on the immutability guarantee.
 *
 * @param <T> the type of the nodes in the tree
 */
public abstract class GuaranteedMutableTree<T> implements MutableTreeSpec<T> {

    /**
     * Creates an empty instance. This sole constructor is package-private so
     * that it may not be subclassed outside of this package.
     */
    GuaranteedMutableTree() {
    }

    // Override methods that return self to restrict the return type to
    // GuaranteedMutableTree

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
