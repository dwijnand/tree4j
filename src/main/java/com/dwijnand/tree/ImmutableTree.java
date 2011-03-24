package com.dwijnand.tree;

/**
 * A base class that extends {@link CompliantTree} and is <b>guaranteed</b> to
 * be immutable.
 * <p>
 * See {@link GuaranteedCompliance} for details about compliance and
 * immutability guarantees, noting that this class is not final and defines a
 * package-private constructor.
 *
 * @param <T> the type of the nodes in the tree
 */
public abstract class ImmutableTree<T> extends CompliantTree<T> {

    /**
     * Creates a new instance. This sole constructor is package-private so that
     * it may not be subclassed outside of this package.
     */
    ImmutableTree() {
    }

    // Override methods that return self to change the return type to
    // ImmutableTree

    @Override
    public abstract ImmutableTree<T> withRoot(final T node);

    @Override
    public abstract ImmutableTree<T> add(final T parent, final T child);

    @Override
    public abstract ImmutableTree<T> remove(final T node);

}
