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

    private static final ImmutableCollection<Factory<MutableMultimap<String, String>>> MUTABLE_MULTIMAP_FACTORIES;

    private static final ImmutableCollection<Factory<MutableMap<String, String>>> MUTABLE_MAP_FACTORIES;

    static {
        MUTABLE_MULTIMAP_FACTORIES = ImmutableList
                .of(LINKED_HASH_MULTIMAP_FACTORY);
        MUTABLE_MAP_FACTORIES = ImmutableList.of(HASH_MAP_FACTORY);
    }

    @DataPoints
    public static MutableTree<?>[] data() {
        final int mutableMultimapFactoryCount = MUTABLE_MULTIMAP_FACTORIES
                .size();
        final int mutableMapFactoryCount = MUTABLE_MAP_FACTORIES.size();
        final int mutableTreeCount = mutableMultimapFactoryCount
                * mutableMapFactoryCount;

        final MutableTree<?>[] mutableTrees = new MutableTree[mutableTreeCount];

        int i = 0;
        for (final Factory<MutableMultimap<String, String>> mutableMultimapFactory : MUTABLE_MULTIMAP_FACTORIES) {
            for (final Factory<MutableMap<String, String>> mutableMapFactory : MUTABLE_MAP_FACTORIES) {
                mutableTrees[i++] = MultimapTree.create(
                        mutableMultimapFactory, mutableMapFactory);
            }
        }

        return mutableTrees;
    }

    public <T extends MultimapTree<?>> MultimapTreeTest(final T tree) {
        super(tree);
    }

}
