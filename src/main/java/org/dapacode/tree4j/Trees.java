package org.dapacode.tree4j;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public final class Trees {
  private Trees() { /* Utility class */ }

  public static <T> Collection<Map.Entry<T, T>> getEntriesDepthFirst(final Tree<T> tree) {
    ArrayList<Map.Entry<T, T>> entries = getChildrenEntries(tree, tree.getRoot());
    ListIterator<Map.Entry<T, T>> entryListIter = entries.listIterator();
    while (entryListIter.hasNext()) {
      Map.Entry<T, T> entry = entryListIter.next();
      List<Map.Entry<T, T>> childrenEntries = getChildrenEntries(tree, entry.getValue());
      for (Map.Entry<T, T> childEntry : childrenEntries) {
        entryListIter.add(childEntry);
      }
    }
    return entries;
  }

  private static <T> ArrayList<Map.Entry<T, T>> getChildrenEntries(final Tree<T> tree, final T parent) {
    return Lists.newArrayList(
        FluentIterable.from(tree.getChildren(parent)).transform(new Function<T, Map.Entry<T, T>>() {
          @Override
          public Pair<T, T> apply(final T child) {
            return ImmutablePair.of(parent, child);
          }
        }));
  }
}
