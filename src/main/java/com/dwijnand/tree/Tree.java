package com.dwijnand.tree;

/**
 * A base class for <b>guaranteed</b> {@link TreeSpec}-compliant
 * implementations.
 * <p>
 * The compliance guarantee is a best-effort guarantee, due to the fact that:
 * <ul>
 * <li>Even though this class doesn't have any public or protected constructors,
 * there is no way to completely deny the ability to subclass this class, as it
 * is still possible to define a non-compliant subclass by using the same
 * package namespace.</li>
 * <li>On the JVM it is not possible to absolutely guarantee immutability, as it
 * is always possible to gain access and modify hidden and/or final fields via
 * reflection or byte-code manipulation.</li>
 * </ul>
 * Even if there were only a private constructor, and therefore all
 * implementations, as well as all implementations of intermediate classes, were
 * nested inner classes (which would be not only messy but ugly) it still
 * wouldn't resolve the second issue. Therefore, until the state-of-affairs
 * improves, the constructor was left package-private.
 *
 * @param <T> the type of the nodes in the tree
 */
public abstract class Tree<T> implements TreeSpec<T> {

    /**
     * Creates an empty instance. This sole constructor is package-private so
     * that it may not be subclassed outside of this package.
     */
    Tree() {
    }

    // Override methods that return self to change returned type to Tree

    @Override
    public abstract TreeSpec<T> withRoot(final T node);

    @Override
    public abstract TreeSpec<T> add(final T parent, final T child);

    @Override
    public abstract TreeSpec<T> remove(final T node);

}
