package org.dapacode.tree4j;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.SetMultimap;
import org.dapacode.tree4j.common.Factory;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Map;

import static org.dapacode.tree4j.testutils.Factories.HASH_MAP_FACTORY;
import static org.dapacode.tree4j.testutils.Factories.LINKED_HASH_MAP_FACTORY;
import static org.dapacode.tree4j.testutils.Factories.LINKED_HASH_MULTIMAP_FACTORY;
import static org.junit.Assert.*;

@RunWith(Theories.class)
// CSOFF: WhitespaceAroundCheck
@SuppressWarnings({"InstanceMethodNamingConvention", "DesignForExtension", "LocalCanBeFinal"})
// CSON: WhitespaceAroundCheck
public class MultimapTreeTest extends MutableTreeTest {
  private static final Collection<Factory<SetMultimap<String, String>>> MUTABLE_SETMULTIMAP_FACTORIES =
      ImmutableList.of(LINKED_HASH_MULTIMAP_FACTORY);
  private static final Collection<Factory<Map<String, String>>> MUTABLE_MAP_FACTORIES =
      ImmutableList.of(HASH_MAP_FACTORY, LINKED_HASH_MAP_FACTORY);

  @DataPoints
  public static MutableTree<?>[] data() {
    final int count = 2 + MUTABLE_SETMULTIMAP_FACTORIES.size() * MUTABLE_MAP_FACTORIES.size();
    final MutableTree<?>[] data = new MutableTree<?>[count];
    int i = 0;

    data[i++] = MultimapTree.<String>create();

    final MultimapTree<String> delegate = MultimapTree.create();
    data[i++] = new DelegatingMutableTree<String>() {
      @Override
      protected MutableTree<String> delegate() {
        return delegate;
      }
    };

    for (final Factory<SetMultimap<String, String>> mmapFactory : MUTABLE_SETMULTIMAP_FACTORIES) {
      for (final Factory<Map<String, String>> mapFactory : MUTABLE_MAP_FACTORIES) {
        data[i++] = MultimapTree.create(mmapFactory.get(), mapFactory.get());
      }
    }

    return data;
  }

  @Theory
  public void copyOfShouldReturnAnEqualButNotSameTree(MutableTree<String> mutableTree) {
    setupTreeTestData(mutableTree);

    withoutModifying(mutableTree, new Test<MutableTree<String>>() {
      @Override
      public void apply(MutableTree<String> mutableTree) {
        MutableTree<String> copyTree = MultimapTree.copyOf(mutableTree);
        assertNotSame(mutableTree, copyTree);
        assertEquals(mutableTree, copyTree);
      }
    });
  }
}
