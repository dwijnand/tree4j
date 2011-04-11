package com.dwijnand.tree4j;

/**
 * This class defines the tests for compliance to the specifications defined in
 * the {@link ImmutableTree} interface.
 * <p/>
 * As can be read in the javadoc of the ImmutableTree, which extends
 * {@link Tree}, the only addition to the Tree interface is that the object is
 * immutable. It is, however, impossible to prove that something
 * <em>doesn't</em> happen. Therefore, this class simply extends
 * {@link TreeTest}, passing ImmutableTree implementations to those tests.
 */
public class ImmutableTreeTest extends TreeTest {

    public <T extends ImmutableTree<?>> ImmutableTreeTest(final T tree) {
        super(tree);
    }

}
