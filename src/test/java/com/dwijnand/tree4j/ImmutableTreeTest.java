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
        immutableTree = immutableTree.withRoot("root");
        immutableTree = immutableTree.plus("root", "node");
        assertTrue(immutableTree.contains("node"));
        assertTreeNotModified();
    }

    @Theory
    @Override
    public void containsShouldReturnTrueForSetRoot(final Tree<String> tree) {
        ImmutableTree<String> immutableTree = (ImmutableTree<String>) tree;
        immutableTree = immutableTree.withRoot("root");
        assertTrue(immutableTree.contains("root"));
        assertTreeNotModified();
    }

    @Theory
    @Override
    public void getParentShouldWorkCorrectly(final Tree<String> tree) {
        ImmutableTree<String> immutableTree = (ImmutableTree<String>) tree;
        immutableTree = immutableTree.withRoot("root");
        immutableTree = immutableTree.plus("root", "males");
        immutableTree = immutableTree.plus("root", "females");
        immutableTree = immutableTree.plus("males", "Paul");
        immutableTree = immutableTree.plus("males", "Peter");
        immutableTree = immutableTree.plus("males", "John");
        immutableTree = immutableTree.plus("females", "Maria");

        final String parent = immutableTree.getParent("Peter");

        assertEquals("males", parent);
        assertTreeNotModified();
    }

    @Theory
    @Override
    public void getRootShouldReturnSetRoot(final Tree<String> tree) {
        ImmutableTree<String> immutableTree = (ImmutableTree<String>) tree;
        immutableTree = immutableTree.withRoot("root");
        assertEquals("root", immutableTree.getRoot());
        assertTreeNotModified();
    }

    @Theory
    @Override
    public void setRootShouldClearTree(final Tree<String> tree) {
        ImmutableTree<String> immutableTree = (ImmutableTree<String>) tree;
        immutableTree = immutableTree.withRoot("God");
        immutableTree = immutableTree.plus("God", "Jesus");
        immutableTree = immutableTree.withRoot("Allah");
        assertFalse(immutableTree.contains("Muhammad"));
        assertTreeNotModified();
    }

    @Theory
    public void plusShouldWorkCorrectly(ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("root");
        immutableTree = immutableTree.plus("root", "males");
        immutableTree = immutableTree.plus("root", "females");
        immutableTree = immutableTree.plus("males", "Paul");
        immutableTree = immutableTree.plus("males", "Peter");
        immutableTree = immutableTree.plus("males", "John");
        immutableTree = immutableTree.plus("females", "Maria");

        final Collection<String> children = immutableTree.getChildren("males");

        assertThat(3, is(children.size()));
        assertThat(children, hasItems("John", "Paul", "Peter"));
        assertTreeNotModified();
    }

    @Theory
    public void plusShouldReturnAnIdenticalTreeOnReinsertingNode(
            ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("root");
        immutableTree = immutableTree.plus("root", "males");
        immutableTree = immutableTree.plus("root", "females");
        immutableTree = immutableTree.plus("males", "Paul");

        final Tree<String> newTree = immutableTree.plus("root", "males");

        EqualsBuilder.reflectionEquals(immutableTree, newTree);
        assertTreeNotModified();
    }

    @Theory
    public void plusShouldThrowIllegalArgumentExceptionOnInsertingAnUnknownParent(
            ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("root");

        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage("does not contain parent node unknown parent");

        immutableTree.plus("unknown parent", "node");
        assertTreeNotModified();
    }

    @Theory
    public void minusShouldCascadeRemove(ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("root");
        immutableTree = immutableTree.plus("root", "males");
        immutableTree = immutableTree.plus("root", "females");
        immutableTree = immutableTree.plus("root", "unsure");
        immutableTree = immutableTree.plus("males", "Peter");
        immutableTree = immutableTree.plus("males", "Paul");
        immutableTree = immutableTree.plus("females", "Maria");

        immutableTree = immutableTree.minus("males");

        assertFalse(immutableTree.contains("males"));
        final Collection<String> rootChildren =
                immutableTree.getChildren("root");
        assertThat(2, is(rootChildren.size()));
        assertFalse(rootChildren.contains("males"));
        assertFalse(immutableTree.contains("Peter"));
        assertNotSame("root", immutableTree.getParent("males"));
        assertTreeNotModified();
    }

    @Theory
    public void minusShouldNotThrowAConcurrentModificationException(
            ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("root");
        immutableTree = immutableTree.plus("root", "males");
        immutableTree = immutableTree.plus("males", "Peter").plus("males", "Paul");

        immutableTree.minus("males");
        assertTreeNotModified();
    }
}
