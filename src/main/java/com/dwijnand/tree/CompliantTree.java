package com.dwijnand.tree;

/**
 * A base class for <b>guaranteed</b> {@link Tree}-compliant implementations.
 * <p>
 * See {@link GuaranteedCompliance} for details about compliance and
 * immutability guarantees, noting that this class is not final and defines a
 * package-private constructor.
 *
 * @param <T> the type of the nodes in the tree
 */
public abstract class CompliantTree<T> implements Tree<T> {

    /**
     * Creates a new instance. This sole constructor is package-private so that
     * it may not be subclassed outside of this package.
     */
    CompliantTree() {
    }

    // Override methods that return self to restrict the return type to Tree

    @Override
    public abstract CompliantTree<T> withRoot(final T node);

    @Override
    public abstract CompliantTree<T> add(final T parent, final T child);

    @Override
    public abstract CompliantTree<T> remove(final T node);

}
