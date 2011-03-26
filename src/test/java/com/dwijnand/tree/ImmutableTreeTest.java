package com.dwijnand.tree;

import static com.dwijnand.tree.test.helpers.Suppliers.IMMUTABLE_MAP_BUILDER_SUPPLIER;
import static com.dwijnand.tree.test.helpers.Suppliers.IMMUTABLE_MULTIMAP_BUILDER_SUPPLIER;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class ImmutableTreeTest extends TreeTest {

    @DataPoints
    public static ImmutableTree<?>[] data() {
        return new ImmutableTree[] {ImmutableMultimapTree.create(
                IMMUTABLE_MULTIMAP_BUILDER_SUPPLIER,
                IMMUTABLE_MAP_BUILDER_SUPPLIER)};
    }

}
