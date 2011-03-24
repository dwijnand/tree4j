package com.dwijnand.tree.suppliers;

import com.google.common.base.Supplier;

/**
 * This interface extends the {@link Supplier} interface, restricting it to a
 * supplier of only new instances of the appropriate type.
 *
 * @param <T> the type of the objects supplied
 */
public interface NewInstanceSupplierSpec<T> extends Supplier<T> {

    // Override javadoc without inheriting to restrict
    /**
     * Retrieves a new instance of the appropriate type.
     *
     * @return an new instance
     */
    @Override
    T get();

}