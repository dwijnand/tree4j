package com.dwijnand.tree;

import static com.dwijnand.tree.test.helpers.Suppliers.HASH_MAP_SUPPLIER;
import static com.dwijnand.tree.test.helpers.Suppliers.LINKED_HASH_MULTIMAP_SUPPLIER;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class MutableTreeSpecTest extends TreeSpecTest {

    @DataPoints
    public static MutableTreeSpec<?>[] data() {
        return new MutableTreeSpec[] {MultimapTree.create(
                LINKED_HASH_MULTIMAP_SUPPLIER, HASH_MAP_SUPPLIER)};
    }

    @Theory
    public void setRootShouldSetTheRoot(
            final MutableTreeSpec<String> mutableTree) {
        mutableTree.setRoot("root");

        assertEquals("root", mutableTree.getRoot());
    }

    @Theory
    public void setRootShouldRemoveAllExistingNodes(
            final MutableTreeSpec<String> mutableTree) {
        mutableTree.setRoot("root");
        mutableTree.added("root", "child");

        mutableTree.setRoot("new root");

        assertFalse(mutableTree.contains("child"));
    }

    @Theory
    public void addedShouldAddTheParentChildAssociation(
            final MutableTreeSpec<String> multimapTree) {
        multimapTree.setRoot("root");

        multimapTree.added("root", "child");

        assertTrue(multimapTree.contains("root"));
        assertTrue(multimapTree.contains("child"));
        assertTrue(multimapTree.getChildren("root").contains("child"));
    }

    @Theory
    public void addedShouldThrowAnIllegalArgumentExceptionOnUnknownParent(
            final MutableTreeSpec<String> multimapTree) {
        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage("does not contain parent node unknown parent");

        multimapTree.added("unknown parent", "child");
    }

    @Theory
    public void clearShouldRemoveAllNodes(
            final MutableTreeSpec<String> multimapTree) {
        multimapTree.setRoot("root");
        multimapTree.added("root", "child");

        multimapTree.clear();

        assertFalse(multimapTree.contains("root"));
        assertFalse(multimapTree.contains("child"));
    }

    @Theory
    public void removedShouldRemoveTheSpecifiedNodeAndAllOfItsChildren(
            final MutableTreeSpec<String> multimapTree) {
        multimapTree.setRoot("root");
        multimapTree.added("root", "1").added("root", "2")
                .added("root", "3");
        multimapTree.added("1", "1-1").added("1", "1-2");
        multimapTree.added("2", "2-1");

        multimapTree.removed("1");

        assertFalse(multimapTree.contains("1"));
        final Collection<String> rootChildren = multimapTree
                .getChildren("root");
        assertThat(2, is(rootChildren.size()));
        assertFalse(rootChildren.contains("1"));
        assertFalse(multimapTree.contains("1-2"));
        assertNotSame("root", multimapTree.getParent("1"));
    }

}
