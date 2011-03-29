package com.dwijnand.tree4j;

import static com.dwijnand.tree4j.test.helper.Factories.HASH_MAP_FACTORY;
import static com.dwijnand.tree4j.test.helper.Factories.LINKED_HASH_MULTIMAP_FACTORY;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import com.dwijnand.tree4j.common.Factory;
import com.dwijnand.tree4j.common.MutableMap;
import com.dwijnand.tree4j.common.MutableMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

@RunWith(Theories.class)
public class MultimapTreeTest extends MutableTreeTest {

    private static final ImmutableCollection<Factory<MutableMultimap<String, String>>> MUTABLE_MULTIMAPS_FACTORIES;

    private static final ImmutableCollection<Factory<MutableMap<String, String>>> MUTABLE_MAPS_FACTORIES;

    static {
        final ImmutableList.Builder<Factory<MutableMultimap<String, String>>> mutableMultimapsFactoriesBuilder = ImmutableList
                .builder();
        mutableMultimapsFactoriesBuilder.add(LINKED_HASH_MULTIMAP_FACTORY);
        MUTABLE_MULTIMAPS_FACTORIES = mutableMultimapsFactoriesBuilder
                .build();

        final ImmutableList.Builder<Factory<MutableMap<String, String>>> mutableMapsFactoriesBuilder = ImmutableList
                .builder();
        mutableMapsFactoriesBuilder.add(HASH_MAP_FACTORY);
        MUTABLE_MAPS_FACTORIES = mutableMapsFactoriesBuilder.build();

    }

    @DataPoints
    public static MutableTree<?>[] data() {
        final int mutableMultimapsFactoriesCount = MUTABLE_MULTIMAPS_FACTORIES
                .size();
        final int mutableMapsFactoriesCount = MUTABLE_MAPS_FACTORIES.size();
        final int mutableTreesCount = mutableMultimapsFactoriesCount
                * mutableMapsFactoriesCount;

        final MutableTree<?>[] mutableTrees = new MutableTree[mutableTreesCount];

        int i = 0;
        for (final Factory<MutableMultimap<String, String>> mutableMultimapsFactory : MUTABLE_MULTIMAPS_FACTORIES) {
            for (final Factory<MutableMap<String, String>> mutableMapsFactory : MUTABLE_MAPS_FACTORIES) {
                mutableTrees[i++] = MultimapTree.create(
                        mutableMultimapsFactory, mutableMapsFactory);
            }
        }

        return mutableTrees;
    }

    public <T extends MultimapTree<?>> MultimapTreeTest(final T tree) {
        super(tree);
    }

}
