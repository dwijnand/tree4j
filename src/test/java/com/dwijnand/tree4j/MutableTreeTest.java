package com.dwijnand.tree4j;

import java.util.Collection;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.experimental.theories.Theory;

/**
 * This class defines the tests for compliance to the specifications defined in
 * the {@link MutableTree} interface.
 * <p/>
 * As MutableTree extends Tree, this class extends {@link TreeTest} adding tests
 * for the methods defined in MutableTree. It, therefore, passes MutableTree
 * implementations to the tests in TreeTest and then to the tests defined here.
 */
// TODO make methods final
public class MutableTreeTest extends TreeTest {

    public <T extends MutableTree<?>> MutableTreeTest(final T tree) {
        super(tree);
    }

    @Theory
    public void setRootShouldSetTheRoot(final MutableTree<String> mutableTree) {
        mutableTree.setRoot("root");

        assertEquals("root", mutableTree.getRoot());
    }

    @Theory
    public void setRootShouldRemoveAllExistingNodes(
            final MutableTree<String> mutableTree) {
        mutableTree.setRoot("root");
        mutableTree.add("root", "child");

        mutableTree.setRoot("new root");

        assertFalse(mutableTree.contains("child"));
    }

    @Theory
    public void addShouldAddTheParentChildAssociation(
            final MutableTree<String> multimapTree) {
        multimapTree.setRoot("root");

        multimapTree.add("root", "child");

        assertTrue(multimapTree.contains("root"));
        assertTrue(multimapTree.contains("child"));
        assertTrue(multimapTree.getChildren("root").contains("child"));
    }

    @Theory
    public void addShouldThrowAnIllegalArgumentExceptionOnUnknownParent(
            final MutableTree<String> multimapTree) {
        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage("does not contain parent node unknown parent");

        multimapTree.add("unknown parent", "child");
    }

    @Theory
    public void clearShouldRemoveAllNodes(
            final MutableTree<String> multimapTree) {
        multimapTree.setRoot("root");
        multimapTree.add("root", "child");

        multimapTree.clear();

        assertFalse(multimapTree.contains("root"));
        assertFalse(multimapTree.contains("child"));
    }

    @Theory
    public void removeShouldRemoveTheSpecifiedNodeAndAllOfItsChildren(
            final MutableTree<String> multimapTree) {
        multimapTree.setRoot("root");
        multimapTree.add("root", "males").add("root", "females")
                .add("root", "unsure");
        multimapTree.add("males", "Paul").add("males", "Peter");
        multimapTree.add("females", "Maria");

        multimapTree.remove("males");

        assertFalse(multimapTree.contains("males"));
        final Collection<String> rootChildren = multimapTree
                .getChildren("root");
        assertThat(2, is(rootChildren.size()));
        assertFalse(rootChildren.contains("males"));
        assertFalse(multimapTree.contains("Paul"));
        assertNotSame("root", multimapTree.getParent("males"));
    }

}
