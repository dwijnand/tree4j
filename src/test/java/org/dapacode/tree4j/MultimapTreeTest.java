package org.dapacode.tree4j;

import com.google.common.collect.ImmutableList;
import org.dapacode.tree4j.common.Factory;
import org.dapacode.tree4j.common.MutableMap;
import org.dapacode.tree4j.common.MutableMultimap;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import java.util.Collection;

import static org.dapacode.tree4j.testutils.Factories.HASH_MAP_FACTORY;
import static org.dapacode.tree4j.testutils.Factories.LINKED_HASH_MAP_FACTORY;
import static org.dapacode.tree4j.testutils.Factories.LINKED_HASH_MULTIMAP_FACTORY;

@RunWith(Theories.class)
public class MultimapTreeTest extends MutableTreeTest {
  private static final Collection<Factory<MutableMultimap<String, String>>> MUTABLE_MULTIMAP_FACTORIES =
      ImmutableList.of(LINKED_HASH_MULTIMAP_FACTORY);
  private static final Collection<Factory<MutableMap<String, String>>> MUTABLE_MAP_FACTORIES =
      ImmutableList.of(HASH_MAP_FACTORY, LINKED_HASH_MAP_FACTORY);

  @DataPoints
  public static MutableTree<?>[] data() {
    final int count = 1 + MUTABLE_MULTIMAP_FACTORIES.size() * MUTABLE_MAP_FACTORIES.size();
    final MutableTree<?>[] data = new MutableTree<?>[count];

    int i = 0;
    data[i++] = MultimapTree.create();
    for (final Factory<MutableMultimap<String, String>> mmmapFactory : MUTABLE_MULTIMAP_FACTORIES) {
      for (final Factory<MutableMap<String, String>> mmapFactory : MUTABLE_MAP_FACTORIES) {
        data[i++] = MultimapTree.create(mmmapFactory.get(), mmapFactory.get());
      }
    }

    return data;
  }

  public MultimapTreeTest(final MultimapTree<?> tree) {
    super(tree);
  }
}
