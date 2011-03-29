package com.dwijnand.tree4j;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import com.dwijnand.tree4j.ImmutableMultimapTree.ChildrenBuilderFactory;
import com.dwijnand.tree4j.ImmutableMultimapTree.ParentsBuildersFactory;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

@RunWith(Theories.class)
public class ImmutableMultimapTreeTest extends ImmutableTreeTest {

    private static final ImmutableCollection<ChildrenBuilderFactory<? extends ImmutableMultimap.Builder<String, String>, String>> CHILDREN_BUILDERS_FACTORIES;

    private static final ImmutableCollection<ParentsBuildersFactory<? extends ImmutableMap.Builder<String, String>, String>> PARENTS_BUILDERS_FACTORIES;

    static {
        final ImmutableList.Builder<ChildrenBuilderFactory<? extends ImmutableMultimap.Builder<String, String>, String>> childrenBuildersFactoriesBuilder = ImmutableList
                .builder();

        childrenBuildersFactoriesBuilder.add(ChildrenBuilderFactory
                .<String> newImmutableMultimapsBuildersFactory());

        CHILDREN_BUILDERS_FACTORIES = childrenBuildersFactoriesBuilder
                .build();

        final ImmutableList.Builder<ParentsBuildersFactory<? extends ImmutableMap.Builder<String, String>, String>> parentsBuildersFactoriesBuilder = ImmutableList
                .builder();

        parentsBuildersFactoriesBuilder.add(ParentsBuildersFactory
                .<String> newImmutableMapsBuildersFactory());

        PARENTS_BUILDERS_FACTORIES = parentsBuildersFactoriesBuilder.build();
    }

    @DataPoints
    public static ImmutableMultimapTree<?>[] data() {
        final int childrenBuildersFactoriesCount = CHILDREN_BUILDERS_FACTORIES
                .size();
        final int parentsBuildersFactoriesCount = PARENTS_BUILDERS_FACTORIES
                .size();
        final int immutableTreesCount = childrenBuildersFactoriesCount
                * parentsBuildersFactoriesCount;

        final ImmutableMultimapTree<?>[] immutableTrees = new ImmutableMultimapTree[immutableTreesCount];

        int i = 0;
        for (final ChildrenBuilderFactory<? extends ImmutableMultimap.Builder<String, String>, String> childrenBuilderFactory : CHILDREN_BUILDERS_FACTORIES) {
            for (final ParentsBuildersFactory<? extends ImmutableMap.Builder<String, String>, String> parentsBuildersFactory : PARENTS_BUILDERS_FACTORIES) {
                immutableTrees[i++] = ImmutableMultimapTree.create(
                        childrenBuilderFactory, parentsBuildersFactory);
            }
        }

        return immutableTrees;
    }

    public <T extends ImmutableMultimapTree<?>> ImmutableMultimapTreeTest(
            final T tree) {
        super(tree);
    }

}
