package com.dwijnand.tree4j;

import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.experimental.theories.Theory;
import static org.junit.matchers.JUnitMatchers.hasItems;

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
    public MutableTreeTest(final MutableTree<?> tree) {
        super(tree);
    }

    @Theory
    @Override
    public void containsShouldReturnTrueOnAddedNode(final Tree<String> tree) {
        final MutableTree<String> mutableTree = (MutableTree<String>) tree;
        mutableTree.setRoot("R");
        mutableTree.add("R", "1");
        assertTrue(mutableTree.contains("1"));
        assertTreeNotModified();
    }

    @Theory
    @Override
    public void containsShouldReturnTrueForSetRoot(final Tree<String> tree) {
        final MutableTree<String> mutableTree = (MutableTree<String>) tree;
        mutableTree.setRoot("R");
        assertTrue(mutableTree.contains("R"));
        assertTreeNotModified();
    }

    @Theory
    @Override
    public void getParentShouldReturnTheExpectedNode(final Tree<String> tree) {
        final MutableTree<String> mutableTree = (MutableTree<String>) tree;
        mutableTree.setRoot("R");
        mutableTree.add("R", "1");
        mutableTree.add("R", "2");
        mutableTree.add("1", "a");
        mutableTree.add("1", "b");
        mutableTree.add("2", "c");

        final String parent = mutableTree.getParent("a");

        assertEquals("1", parent);
        assertTreeNotModified();
    }

    @Theory
    @Override
    public void getChildrenShouldReturnTheExpectedNodes(
            final Tree<String> tree) {
        final MutableTree<String> mutableTree = (MutableTree<String>) tree;
        mutableTree.setRoot("R");
        mutableTree.add("R", "1");
        mutableTree.add("R", "2");
        mutableTree.add("1", "a");
        mutableTree.add("1", "b");
        mutableTree.add("2", "c");

        final Collection<String> children = mutableTree.getChildren("1");

        assertEquals(2, children.size());
        assertThat(children, hasItems("a", "b"));
    }

    @Theory
    @Override
    public void getChildrenShouldReturnAnEmptyCollectionOnALeafNode(
            final Tree<String> tree) {
        final MutableTree<String> mutableTree = (MutableTree<String>) tree;
        mutableTree.setRoot("R");
        mutableTree.add("R", "1");
        mutableTree.add("R", "2");
        mutableTree.add("1", "a");
        mutableTree.add("1", "b");
        mutableTree.add("2", "c");

        final Collection<String> children = mutableTree.getChildren("c");

        assertNotNull(children);
        assertEquals(0, children.size());
    }

    @Theory
    @Override
    public void getRootShouldReturnSetRoot(final Tree<String> tree) {
        final MutableTree<String> mutableTree = (MutableTree<String>) tree;
        mutableTree.setRoot("R");
        assertEquals("R", mutableTree.getRoot());
        assertTreeNotModified();
    }

    @Theory
    public void setRootShouldClearTree(final MutableTree<String> mutableTree) {
        mutableTree.setRoot("R");
        mutableTree.add("R", "1");
        mutableTree.setRoot("S");
        assertFalse(mutableTree.contains("1"));
    }

    @Theory
    public void setRootShouldSetTheRoot(final MutableTree<String> mutableTree) {
        mutableTree.setRoot("R");

        assertEquals("R", mutableTree.getRoot());
    }

    @Theory
    public void setRootShouldRemoveAllExistingNodes(
            final MutableTree<String> mutableTree) {
        mutableTree.setRoot("R");
        mutableTree.add("R", "1");

        mutableTree.setRoot("S");

        assertFalse(mutableTree.contains("1"));
    }

    @Theory
    public void addShouldAddTheParentChildAssociation(
            final MutableTree<String> multimapTree) {
        multimapTree.setRoot("R");

        multimapTree.add("R", "1");

        assertTrue(multimapTree.contains("R"));
        assertTrue(multimapTree.contains("1"));
        assertTrue(multimapTree.getChildren("R").contains("1"));
    }

    @Theory
    public void addShouldThrowAnIllegalArgumentExceptionOnUnknownParent(
            final MutableTree<String> multimapTree) {
        expectedException.expect(IllegalArgumentException.class);
        multimapTree.add("unknown parent", "child");
    }

    @Theory
    public void clearShouldRemoveAllNodes(
            final MutableTree<String> multimapTree) {
        multimapTree.setRoot("R");
        multimapTree.add("R", "1");

        multimapTree.clear();

        assertFalse(multimapTree.contains("R"));
        assertFalse(multimapTree.contains("1"));
    }

    @Theory
    // TODO split this into smaller asserting tests
    public void removeShouldRemoveTheSpecifiedNodeAndAllOfItsChildren(
            final MutableTree<String> multimapTree) {
        multimapTree.setRoot("R");
        multimapTree.add("R", "1");
        multimapTree.add("R", "2");
        multimapTree.add("R", "3");
        multimapTree.add("1", "a");
        multimapTree.add("1", "b");
        multimapTree.add("2", "c");

        multimapTree.remove("1");

        assertFalse(multimapTree.contains("1"));
        final Collection<String> rootChildren = multimapTree
                .getChildren("R");
        assertEquals(2, rootChildren.size());
        assertFalse(rootChildren.contains("1"));
        assertFalse(multimapTree.contains("1"));
    }
}
