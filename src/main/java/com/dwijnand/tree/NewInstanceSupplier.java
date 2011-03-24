package com.dwijnand.tree;

/**
 * A base class for <b>guaranteed</b> {@link NewInstanceSupplierSpec}-compliant
 * implementations. See {@link Tree} for details about compliance and
 * immutability guarantees
 *
 * @param <T> the type of the objects supplied
 */
public abstract class NewInstanceSupplier<T> implements
        NewInstanceSupplierSpec<T> {

    /**
     * Creates a new instance. This sole constructor is package-private so that
     * it may not be subclassed outside of this package.
     */
    NewInstanceSupplier() {
    }

}
