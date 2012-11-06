package org.dapacode.tree4j;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import org.dapacode.tree4j.common.Factory;
import org.dapacode.tree4j.testutils.TreeHelper.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Map;

import static org.dapacode.tree4j.testutils.TreeHelper.withoutModifying;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(Theories.class)
// CSOFF: WhitespaceAroundCheck
@SuppressWarnings({"InstanceMethodNamingConvention", "DesignForExtension", "LocalCanBeFinal"})
// CSON: WhitespaceAroundCheck
public class MultimapTreeTest extends MutableTreeTest {
  private static final Collection<Factory<SetMultimap<String, String>>> MUTABLE_SET_MULTIMAP_FACTORIES =
      ImmutableList.<Factory<SetMultimap<String, String>>>of(
          new Factory<SetMultimap<String, String>>() {
            @Override
            public SetMultimap<String, String> get() {
              return LinkedHashMultimap.create();
            }
          }
      );

  private static final Collection<Factory<Map<String, String>>> MUTABLE_MAP_FACTORIES = ImmutableList.of(
      new Factory<Map<String, String>>() {
        @Override
        public Map<String, String> get() {
          return Maps.newHashMap();
        }
      },
      new Factory<Map<String, String>>() {
        @Override
        public Map<String, String> get() {
          return Maps.newLinkedHashMap();
        }
      }
  );

  @DataPoints
  public static MutableTree<?>[] data() {
    int count = 2 + MUTABLE_SET_MULTIMAP_FACTORIES.size() * MUTABLE_MAP_FACTORIES.size();
    MutableTree<?>[] data = new MutableTree<?>[count];
    int i = 0;

    data[i++] = MultimapTree.<String>create();

    final MultimapTree<String> delegate = MultimapTree.create();
    data[i++] = new DelegatingMutableTree<String>() {
      @Override
      protected MutableTree<String> delegate() {
        return delegate;
      }
    };

    for (Factory<SetMultimap<String, String>> mmapFactory : MUTABLE_SET_MULTIMAP_FACTORIES) {
      for (Factory<Map<String, String>> mapFactory : MUTABLE_MAP_FACTORIES) {
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
        assertThat(copyTree, is(not(sameInstance(mutableTree))));
        assertThat(copyTree, is(equalTo(mutableTree)));
      }
    });
  }
}
