package com.dwijnand.tree4j;

import static com.dwijnand.tree4j.test.helpers.Factories.IMMUTABLE_MAP_BUILDER_FACTORY;
import static com.dwijnand.tree4j.test.helpers.Factories.IMMUTABLE_MULTIMAP_BUILDER_FACTORY;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class ImmutableTreeTest extends TreeTest {

    @DataPoints
    public static ImmutableTree<?>[] data() {
        return new ImmutableTree[] {ImmutableMultimapTree.create(
                IMMUTABLE_MULTIMAP_BUILDER_FACTORY,
                IMMUTABLE_MAP_BUILDER_FACTORY)};
    }

}
