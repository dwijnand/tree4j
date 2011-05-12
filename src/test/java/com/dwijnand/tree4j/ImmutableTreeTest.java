package com.dwijnand.tree4j;

import java.util.Collection;
import org.apache.commons.lang.builder.EqualsBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.experimental.theories.Theory;

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
    public ImmutableTreeTest(final ImmutableTree<?> tree) {
        super(tree);
    }

    @Theory
    @Override
    public void containsShouldReturnTrueOnAddedNode(final Tree<String> tree) {
        ImmutableTree<String> immutableTree = (ImmutableTree<String>) tree;
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.plus("R", "1");
        assertTrue(immutableTree.contains("1"));
        assertTreeNotModified();
    }

    @Theory
    @Override
    public void containsShouldReturnTrueForSetRoot(final Tree<String> tree) {
        ImmutableTree<String> immutableTree = (ImmutableTree<String>) tree;
        immutableTree = immutableTree.withRoot("R");
        assertTrue(immutableTree.contains("R"));
        assertTreeNotModified();
    }

    @Theory
    @Override
    public void getParentShouldWorkCorrectly(final Tree<String> tree) {
        ImmutableTree<String> immutableTree = (ImmutableTree<String>) tree;
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.plus("R", "1");
        immutableTree = immutableTree.plus("R", "2");
        immutableTree = immutableTree.plus("1", "a");
        immutableTree = immutableTree.plus("1", "b");
        immutableTree = immutableTree.plus("2", "c");

        final String parent = immutableTree.getParent("b");

        assertEquals("1", parent);
        assertTreeNotModified();
    }

    @Theory
    @Override
    public void getRootShouldReturnSetRoot(final Tree<String> tree) {
        ImmutableTree<String> immutableTree = (ImmutableTree<String>) tree;
        immutableTree = immutableTree.withRoot("R");
        assertEquals("R", immutableTree.getRoot());
        assertTreeNotModified();
    }

    @Theory
    @Override
    public void setRootShouldClearTree(final Tree<String> tree) {
        ImmutableTree<String> immutableTree = (ImmutableTree<String>) tree;
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.plus("R", "1");
        immutableTree = immutableTree.withRoot("S");
        assertFalse(immutableTree.contains("1"));
        assertTreeNotModified();
    }

    @Theory
    public void plusShouldWorkCorrectly(ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.plus("R", "1");
        immutableTree = immutableTree.plus("R", "2");
        immutableTree = immutableTree.plus("1", "a");
        immutableTree = immutableTree.plus("1", "b");
        immutableTree = immutableTree.plus("2", "c");

        final Collection<String> children = immutableTree.getChildren("1");

        assertThat(2, is(children.size()));
        assertThat(children, hasItems("a", "b"));
        assertTreeNotModified();
    }

    @Theory
    public void plusShouldReturnAnIdenticalTreeOnReinsertingNode(
            ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.plus("R", "1");
        immutableTree = immutableTree.plus("R", "2");
        immutableTree = immutableTree.plus("1", "a");

        final Tree<String> newTree = immutableTree.plus("R", "1");

        EqualsBuilder.reflectionEquals(immutableTree, newTree);
        assertTreeNotModified();
    }

    @Theory
    public void plusShouldThrowIllegalArgumentExceptionOnInsertingAnUnknownParent(
            ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("R");

        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage("The tree doesn't contain the specified parent" +
                        " node: unknown parent");

        immutableTree.plus("unknown parent", "node");
        assertTreeNotModified();
    }

    @Theory
    public void minusShouldCascadeRemove(ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.plus("R", "1");
        immutableTree = immutableTree.plus("R", "2");
        immutableTree = immutableTree.plus("R", "3");
        immutableTree = immutableTree.plus("1", "a");
        immutableTree = immutableTree.plus("1", "b");
        immutableTree = immutableTree.plus("2", "c");

        immutableTree = immutableTree.minus("1");

        assertFalse(immutableTree.contains("1"));
        final Collection<String> rootChildren = immutableTree.getChildren("R");
        assertThat(2, is(rootChildren.size()));
        assertFalse(rootChildren.contains("1"));
        assertFalse(immutableTree.contains("a"));
        assertNotSame("R", immutableTree.getParent("1"));
        assertTreeNotModified();
    }

    @Theory
    public void minusShouldNotThrowAConcurrentModificationException(
            ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.plus("R", "1");
        immutableTree = immutableTree.plus("1", "a");
        immutableTree = immutableTree.plus("1", "b");

        immutableTree.minus("1");
        assertTreeNotModified();
    }
}
