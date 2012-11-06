package org.dapacode.tree4j.testutils;

import org.dapacode.tree4j.ImmutableTree;
import org.dapacode.tree4j.MutableTree;

@SuppressWarnings("FeatureEnvy")
public final class TreeHelper {
  private TreeHelper() {}

  public static ImmutableTree<String> setupTreeTestData(final ImmutableTree<String> originalImmutableTree) {
    ImmutableTree<String> immutableTree = originalImmutableTree;
    immutableTree = immutableTree.withRoot("R");
    immutableTree = immutableTree.added("R", "1");
    immutableTree = immutableTree.added("R", "2");
    immutableTree = immutableTree.added("1", "a");
    immutableTree = immutableTree.added("1", "b");
    immutableTree = immutableTree.added("2", "c");
    immutableTree = immutableTree.added("a", "!");
    return immutableTree;
  }

  public static MutableTree<String> setupTreeTestData(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");
    mutableTree.add("R", "2");
    mutableTree.add("1", "a");
    mutableTree.add("1", "b");
    mutableTree.add("2", "c");
    mutableTree.add("a", "!");
    return mutableTree;
  }
}
