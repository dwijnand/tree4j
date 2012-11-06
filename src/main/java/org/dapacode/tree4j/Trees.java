package org.dapacode.tree4j;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public final class Trees {
  private Trees() { /* Utility class */ }

  public static <T> Collection<Map.Entry<T, T>> getAssociationsDepthFirst(final Tree<T> tree) {
    return getAssociationsDepthFirst(tree, tree.getRoot());
  }

  public static <T> Collection<Map.Entry<T, T>> getAssociationsDepthFirst(final Tree<T> tree, final T node) {
    final List<Map.Entry<T, T>> associations = Lists.newArrayList(getAssociations(tree, node));
    final ListIterator<Map.Entry<T, T>> listIter = associations.listIterator();
    while (listIter.hasNext()) {
      final Map.Entry<T, T> entry = listIter.next();
      final T child = entry.getValue();
      final Collection<Map.Entry<T, T>> childrenAssociations = getAssociationsDepthFirst(tree, child);
      for (final Map.Entry<T, T> childAssociation : childrenAssociations) {
        listIter.add(childAssociation);
      }
    }
    return associations;
  }

  private static <T> FluentIterable<Map.Entry<T, T>> getAssociations(final Tree<T> tree, final T node) {
    return FluentIterable.from(tree.getChildren(node)).transform(new Function<T, Map.Entry<T, T>>() {
      @Override
      public Map.Entry<T, T> apply(final T child) {
        return new AbstractMap.SimpleEntry<T, T>(node, child);
      }
    });
  }
}
