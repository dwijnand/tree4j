package com.dwijnand.tree4j;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.Collection;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class ImmutableMultimapTreeTest extends ImmutableTreeTest {
    private static final
    ImmutableCollection<ImmutableMultimapTree.ChildrenBuilderFactory<String>>
            CHILDREN_BUILDER_FACTORIES;

    private static final
    ImmutableCollection<ImmutableMultimapTree.ParentsBuilderFactory<String>>
            PARENTS_BUILDER_FACTORIES;

    static {
        final Collection<ImmutableMultimapTree.ChildrenBuilderFactory<String>>
                childrenBuilderFactories = Lists.newArrayList();

        childrenBuilderFactories.add(ImmutableMultimapTree
                .ChildrenBuilderFactory.<String>usingListMultimap());
        childrenBuilderFactories.add(ImmutableMultimapTree
                .ChildrenBuilderFactory.<String>usingSetMultimap());

        CHILDREN_BUILDER_FACTORIES =
                ImmutableList.copyOf(childrenBuilderFactories);

        final Collection<ImmutableMultimapTree.ParentsBuilderFactory<String>>
                parentsBuilderFactories = Lists.newArrayList();

        parentsBuilderFactories.add(ImmutableMultimapTree.ParentsBuilderFactory
                .<String>usingImmutableMap());
        parentsBuilderFactories.add(ImmutableMultimapTree.ParentsBuilderFactory
                .<String>usingImmutableSortedMapInNaturalOrder());
        parentsBuilderFactories.add(ImmutableMultimapTree.ParentsBuilderFactory
                .<String>usingImmutableSortedMapInReverseOrder());
        parentsBuilderFactories.add(ImmutableMultimapTree.ParentsBuilderFactory
                .<String>usingImmutableSortedMapOrderedBy(
                        Ordering.<String>natural()));

        PARENTS_BUILDER_FACTORIES =
                ImmutableList.copyOf(parentsBuilderFactories);
    }

    @DataPoints
    public static ImmutableMultimapTree<?>[] data() {
        final int count = CHILDREN_BUILDER_FACTORIES.size()
                * PARENTS_BUILDER_FACTORIES.size();

        final ImmutableMultimapTree<?>[] data =
                new ImmutableMultimapTree[count];

        int i = 0;
        for (final ImmutableMultimapTree.ChildrenBuilderFactory<String>
                childrenBuilderFactory : CHILDREN_BUILDER_FACTORIES) {
            for (final ImmutableMultimapTree.ParentsBuilderFactory<String>
                    parentsBuilderFactory : PARENTS_BUILDER_FACTORIES) {
                data[i++] = ImmutableMultimapTree.create(
                        childrenBuilderFactory, parentsBuilderFactory);
            }
        }

        return data;
    }

    public ImmutableMultimapTreeTest(final ImmutableMultimapTree<?> tree) {
        super(tree);
    }
}
