package com.dwijnand.tree4j.common;

import java.util.Map;

/**
 * This interface extends the {@link Map} interface, adding the restriction that
 * all optional (modifying) methods are implements (i.e. don't throw
 * {@link javax.naming.OperationNotSupportedException
 * OperationNotSupportedException}).
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public interface MutableMap<K, V> extends Map<K, V> {
}
