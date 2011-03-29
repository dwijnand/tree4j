package com.dwijnand.tree4j;

import static com.dwijnand.tree4j.test.helpers.Factories.IMMUTABLE_MAP_BUILDER_FACTORY;
import static com.dwijnand.tree4j.test.helpers.Factories.IMMUTABLE_MULTIMAP_BUILDER_FACTORY;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

/**
 * This class defines the tests for compliance to the specifications defined in
 * the {@link ImmutableTree} interface.
 * <p>
 * As can be read in the javadoc of the ImmutableTree, which extends
 * {@link Tree}, the only addition to the Tree interface is that the object is
 * immutable. It is, however, impossible to prove that something
 * <em>doesn't</em> happen. Therefore, this class simply extends
 * {@link TreeTest}, passing ImmutableTree implementations to those tests.
 */
@RunWith(Theories.class)
public class ImmutableTreeTest extends TreeTest {

    @DataPoints
    public static ImmutableTree<?>[] data() {
        return new ImmutableTree[] {ImmutableMultimapTree.create(
                IMMUTABLE_MULTIMAP_BUILDER_FACTORY,
                IMMUTABLE_MAP_BUILDER_FACTORY)};
    }

}
