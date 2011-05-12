package com.dwijnand.tree4j;

import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public MutableTreeTest(final MutableTree<?> tree) {
        super(tree);
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
            final MutableTree<String> mutableTree) {
        mutableTree.setRoot("R");

        mutableTree.add("R", "1");

        assertTrue(mutableTree.contains("R"));
        assertTrue(mutableTree.contains("1"));
        assertTrue(mutableTree.getChildren("R").contains("1"));
    }

    @Theory
    public void addShouldThrowAnIllegalArgumentExceptionOnUnknownParent(
            final MutableTree<String> mutableTree) {
        expectedException.expect(IllegalArgumentException.class);
        mutableTree.add("unknown parent", "child");
    }

    @Theory
    public void clearShouldRemoveAllNodes(
            final MutableTree<String> mutableTree) {
        mutableTree.setRoot("R");
        mutableTree.add("R", "1");

        mutableTree.clear();

        assertFalse(mutableTree.contains("R"));
        assertFalse(mutableTree.contains("1"));
    }

    @Theory
    // TODO split this into smaller asserting tests
    public void removeShouldRemoveTheSpecifiedNodeAndAllOfItsChildren(
            final MutableTree<String> mutableTree) {
        mutableTree.setRoot("R");
        mutableTree.add("R", "1");
        mutableTree.add("R", "2");
        mutableTree.add("R", "3");
        mutableTree.add("1", "a");
        mutableTree.add("1", "b");
        mutableTree.add("2", "c");

        mutableTree.remove("1");

        assertFalse(mutableTree.contains("1"));
        final Collection<String> rootChildren = mutableTree
                .getChildren("R");
        assertEquals(2, rootChildren.size());
        assertFalse(rootChildren.contains("1"));
        assertFalse(mutableTree.contains("1"));
    }

    @Override
    public Tree<String> withRoot(final Tree<String> tree, final String root) {
        ((MutableTree<String>) tree).setRoot(root);
        return tree;
    }

    @Override
    public Tree<String> plus(final Tree<String> tree, final String parent,
                             final String child) {
        ((MutableTree<String>) tree).add(parent, child);
        return tree;
    }

    @Override
    public Tree<String> minus(final Tree<String> tree, final String node) {
        ((MutableTree<String>) tree).remove(node);
        return tree;
    }

    @Override
    public Tree<String> setupTreeTestData(final Tree<String> tree) {
        return setupTreeTestData((MutableTree<String>) tree);
    }

    private static MutableTree<String> setupTreeTestData(
            final MutableTree<String> mutableTree) {
        mutableTree.setRoot("R");
        mutableTree.add("R", "1");
        mutableTree.add("R", "2");
        mutableTree.add("1", "a");
        mutableTree.add("1", "b");
        mutableTree.add("2", "c");
        return mutableTree;
    }
}
