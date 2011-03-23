package com.dwijnand.tree.immutable;

/**
 * A base class for implementing <b>guaranteed</b> immutable tree structures.
 * See {@link com.dwijnand.tree.Tree tree.Tree} for more details on what is
 * intended by tree structure.
 * <p>
 * This class is not final, however, it doesn't have any public or protected
 * constructors and, therefore, it cannot be subclasses outside of its package.
 * <p>
 * There is, however, no way to deny external clients the ability to subclass
 * this class, as it is still possible to simply use the same package namespace
 * as this class to define a subclass that isn't compliant. Also, on the JVM
 * it's just not possible to absolutely guarantee immutability, as it is always
 * possible to gain access and modify hidden and final fields via reflection
 * and/or byte-code manipulation. However, this is the best that can be done
 * with the current state of affairs, along with a suite of tests to verify
 * compatibility with the specifications.
 *
 * @param <T> the type of the nodes in the tree
 */
public abstract class Tree<T> implements com.dwijnand.tree.Tree<T> {

    Tree() {
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method always returns an {@link Tree immutable.Tree} instead of the
     * more general {@link Tree}.
     */
    @Override
    public abstract Tree<T> withRoot(T node);

    /**
     * {@inheritDoc}
     * <p>
     * This method always returns an {@link Tree immutable.Tree} instead of the
     * more general {@link Tree}.
     */
    @Override
    public abstract Tree<T> add(final T parent, final T child);

    /**
     * {@inheritDoc}
     * <p>
     * This method always returns an {@link Tree immutable.Tree} instead of the
     * more general {@link Tree}.
     */
    @Override
    public abstract Tree<T> remove(T node);

}
