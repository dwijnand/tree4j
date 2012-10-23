package org.dapacode.tree4j;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import java.util.Collection;

@RunWith(Theories.class)
public class ImmutableMultimapTreeTest extends ImmutableTreeTest {
  private static final Collection<ImmutableMultimapTree.ChildrenMaker<String>> CHILDREN_MAKERS;
  private static final Collection<ImmutableMultimapTree.ParentsMaker<String>> PARENTS_MAKERS;

  static {
    final Collection<ImmutableMultimapTree.ChildrenMaker<String>> cms = Lists.newArrayList();

    cms.add(ImmutableMultimapTree.ChildrenMaker.<String>usingSetMultimap());

    CHILDREN_MAKERS = ImmutableList.copyOf(cms);

    final Collection<ImmutableMultimapTree.ParentsMaker<String>> pms = Lists.newArrayList();

    pms.add(ImmutableMultimapTree.ParentsMaker.<String>usingImmutableMap());
    pms.add(ImmutableMultimapTree.ParentsMaker.<String>usingImmutableSortedMapInNaturalOrder());
    pms.add(ImmutableMultimapTree.ParentsMaker.<String>usingImmutableSortedMapInReverseOrder());
    pms.add(ImmutableMultimapTree.ParentsMaker.<String>usingImmutableSortedMapOrderedBy(Ordering.<String>natural()));

    PARENTS_MAKERS = ImmutableList.copyOf(pms);
  }

  @DataPoints
  public static ImmutableTree<?>[] data() {
    final int count = 2 + CHILDREN_MAKERS.size() * PARENTS_MAKERS.size();
    final ImmutableTree<?>[] data = new ImmutableTree<?>[count];
    int i = 0;

    data[i++] = ImmutableMultimapTree.<String>create();

    final ImmutableMultimapTree<String> delegate = ImmutableMultimapTree.create();
    data[i++] = new DelegatingImmutableTree<String>() {
      @Override
      protected ImmutableTree<String> delegate() {
        return delegate;
      }
    };

    for (final ImmutableMultimapTree.ChildrenMaker<String> childrenMaker : CHILDREN_MAKERS) {
      for (final ImmutableMultimapTree.ParentsMaker<String> parentsMaker : PARENTS_MAKERS) {
        data[i++] = ImmutableMultimapTree.create(childrenMaker, parentsMaker);
      }
    }

    return data;
  }
}
