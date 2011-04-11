package com.dwijnand.tree4j;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
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
        final ImmutableList.Builder<ImmutableMultimapTree.ChildrenBuilderFactory<String>>
                childrenBuilderFactoryBuilder = ImmutableList.builder();

        childrenBuilderFactoryBuilder.add(
                ImmutableMultimapTree.ChildrenBuilderFactory.<String>usingListMultimap());
        childrenBuilderFactoryBuilder.add(
                ImmutableMultimapTree.ChildrenBuilderFactory.<String>usingSetMultimap());

        CHILDREN_BUILDER_FACTORIES =
                childrenBuilderFactoryBuilder.build();

        final ImmutableList.Builder<ImmutableMultimapTree.ParentsBuilderFactory<String>>
                parentsBuilderFactoryBuilder = ImmutableList.builder();

        parentsBuilderFactoryBuilder.add(ImmutableMultimapTree.ParentsBuilderFactory
                .<String>usingImmutableMap());
        parentsBuilderFactoryBuilder.add(ImmutableMultimapTree.ParentsBuilderFactory
                .<String>usingImmutableBiMap());
        parentsBuilderFactoryBuilder.add(ImmutableMultimapTree.ParentsBuilderFactory
                .<String>usingImmutableSortedMapInNaturalOrder());
        parentsBuilderFactoryBuilder.add(ImmutableMultimapTree.ParentsBuilderFactory
                .<String>usingImmutableSortedMapInReverseOrder());
        parentsBuilderFactoryBuilder.add(ImmutableMultimapTree.ParentsBuilderFactory
                .<String>usingImmutableSortedMapOrderedBy(Ordering.<String>natural()));

        PARENTS_BUILDER_FACTORIES = parentsBuilderFactoryBuilder.build();
    }

    @DataPoints
    public static ImmutableMultimapTree<?>[] data() {
        final int childrenBuilderFactoryCount =
                CHILDREN_BUILDER_FACTORIES.size();
        final int parentsBuilderFactoryCount =
                PARENTS_BUILDER_FACTORIES.size();
        final int immutableTreeCount =
                childrenBuilderFactoryCount * parentsBuilderFactoryCount;

        final ImmutableMultimapTree<?>[] immutableTrees =
                new ImmutableMultimapTree[immutableTreeCount];

        int i = 0;
        for (final ImmutableMultimapTree.ChildrenBuilderFactory<String>
                childrenBuilderFactory : CHILDREN_BUILDER_FACTORIES) {
            for (final ImmutableMultimapTree.ParentsBuilderFactory<String>
                    parentsBuilderFactory : PARENTS_BUILDER_FACTORIES) {
                immutableTrees[i++] = ImmutableMultimapTree.create(
                        childrenBuilderFactory, parentsBuilderFactory);
            }
        }

        return immutableTrees;
    }

    public <T extends ImmutableMultimapTree<?>> ImmutableMultimapTreeTest(
            final T tree) {
        super(tree);
    }

}
