package org.dapacode.tree4j;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(Theories.class)
// CSOFF: WhitespaceAroundCheck
@SuppressWarnings({"InstanceMethodNamingConvention", "DesignForExtension", "LocalCanBeFinal"})
// CSON: WhitespaceAroundCheck
public class ImmutableMultimapTreeTest extends ImmutableTreeTest {
  private static final Collection<ImmutableMultimapTree.ChildrenMaker<String>> CHILDREN_MAKERS =
      ImmutableList.<ImmutableMultimapTree.ChildrenMaker<String>>builder()
          .add(ImmutableMultimapTree.ChildrenMaker.<String>usingSetMultimap())
          .build();

  private static final Collection<ImmutableMultimapTree.ParentsMaker<String>> PARENTS_MAKERS =
      ImmutableList.<ImmutableMultimapTree.ParentsMaker<String>>builder()
          .add(ImmutableMultimapTree.ParentsMaker.<String>usingImmutableMap())
          .add(ImmutableMultimapTree.ParentsMaker.<String>usingImmutableSortedMapInNaturalOrder())
          .add(ImmutableMultimapTree.ParentsMaker.<String>usingImmutableSortedMapInReverseOrder())
          .add(ImmutableMultimapTree.ParentsMaker.<String>usingImmutableSortedMapOrderedBy(Ordering.<String>natural()))
          .build();

  @DataPoints
  public static ImmutableTree<?>[] data() {
    int count = 2 + CHILDREN_MAKERS.size() * PARENTS_MAKERS.size();
    ImmutableTree<?>[] data = new ImmutableTree<?>[count];
    int i = 0;

    data[i++] = ImmutableMultimapTree.<String>create();

    final ImmutableMultimapTree<String> delegate = ImmutableMultimapTree.create();
    data[i++] = new DelegatingImmutableTree<String>() {
      @Override
      protected ImmutableTree<String> delegate() {
        return delegate;
      }
    };

    for (ImmutableMultimapTree.ChildrenMaker<String> childrenMaker : CHILDREN_MAKERS) {
      for (ImmutableMultimapTree.ParentsMaker<String> parentsMaker : PARENTS_MAKERS) {
        data[i++] = ImmutableMultimapTree.create(childrenMaker, parentsMaker);
      }
    }

    return data;
  }

  @Theory
  public void copyOfShouldReturnAnEqualButNotSameTree(ImmutableTree<String> immutableTree) {
    immutableTree = setupTreeTestData(immutableTree);

    withoutModifying(immutableTree, new Test<ImmutableTree<String>>() {
      @Override
      public void apply(ImmutableTree<String> immutableTree) {
        ImmutableMultimapTree<String> copyTree = ImmutableMultimapTree.copyOf(immutableTree);
        assertThat(copyTree, is(not(sameInstance(immutableTree))));
        assertThat(copyTree, is(equalTo(immutableTree)));
      }
    });
  }
}
