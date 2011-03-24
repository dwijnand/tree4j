package com.dwijnand.tree;

/**
 * A base class for <b>guaranteed</b> {@link MutableTree}-compliant
 * implementations that extends.{@link CompliantTree}.
 * <p>
 * See {@link GuaranteedCompliance} for details about compliance and
 * immutability guarantees, noting that this class is not final and defines a
 * package-private constructor.
 *
 * @param <T> the type of the nodes in the tree
 */
public abstract class CompliantMutableTree<T> extends CompliantTree<T>
        implements MutableTree<T> {

    /**
     * Creates a new instance. This sole constructor is package-private so that
     * it may not be subclassed outside of this package.
     */
    CompliantMutableTree() {
    }

    // Override methods that return self to restrict the return type to
    // GuaranteedMutableTree

    @Override
    public abstract CompliantMutableTree<T> withRoot(T node);

    @Override
    public abstract CompliantMutableTree<T> setRoot(T node);

    @Override
    public abstract CompliantMutableTree<T> add(final T parent, final T child);

    @Override
    public abstract CompliantMutableTree<T> added(T parent, T child);

    @Override
    public abstract CompliantMutableTree<T> remove(T node);

    @Override
    public abstract CompliantMutableTree<T> removed(T node);

}
