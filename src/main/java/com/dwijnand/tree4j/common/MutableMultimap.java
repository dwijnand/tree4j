package com.dwijnand.tree4j.common;

import com.google.common.collect.Multimap;

/**
 * This interface extends the {@link Multimap} interface, adding the restriction that all optional (modifying) methods are
 * implements (i.e. don't throw {@link javax.naming.OperationNotSupportedException OperationNotSupportedException}).
 *
 * @param <K> the type of keys maintained by this multimap
 * @param <V> the type of mapped values
 */
public interface MutableMultimap<K, V> extends Multimap<K, V> {
}
