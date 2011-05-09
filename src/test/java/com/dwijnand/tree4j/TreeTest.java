package com.dwijnand.tree4j;

import java.util.Collection;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;

/**
 * This class defines the tests for compliance to the specifications defined in
 * the {@link Tree} interface. It is used as the base class for the test classes
 * for the mutable and immutable extensions of this interface.
 */
// TODO make methods final
public class TreeTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private final Tree<?> tree;

    private final int beforeHashCode;

    public <T extends Tree<?>> TreeTest(final T tree) {
        this.tree = tree;
        beforeHashCode = calculateIdentity(tree);
    }

    protected final void assertTreeNotModified() {
        assertEquals(beforeHashCode, calculateIdentity(tree));
    }

    // TODO consider identifying the tree with a checksum or digest
    private <T extends Tree<?>> int calculateIdentity(final T tree) {
        return HashCodeBuilder.reflectionHashCode(tree);
    }

    @Theory
    public void containsShouldReturnFalseWhenEmpty(final Tree<String> tree) {
        assertFalse(tree.contains("node"));
        assertTreeNotModified();
    }

    @Theory
    public void containsShouldReturnTrueOnAddedNode(Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.plus("root", "node");
        assertTrue(tree.contains("node"));
        assertTreeNotModified();
    }

    @Theory
    public void containsShouldReturnTrueForSetRoot(Tree<String> tree) {
        tree = tree.withRoot("root");
        assertTrue(tree.contains("root"));
        assertTreeNotModified();
    }

    @Theory
    public void getParentShouldWorkCorrectly(Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.plus("root", "males").plus("root", "females");
        tree = tree.plus("males", "Paul").plus("males", "Peter")
                .plus("males", "John");
        tree = tree.plus("females", "Maria");

        final String parent = tree.getParent("Peter");

        assertEquals("males", parent);
        assertTreeNotModified();
    }

    @Theory
    public void getParentShouldReturnNullForUnknownNode(
            final Tree<String> tree) {
        final String parent = tree.getParent("unknown node");

        assertNull(parent);
        assertTreeNotModified();
    }

    @Theory
    public void getChildrenShouldReturnEmptyCollectionOnUnknownNode(
            final Tree<String> tree) {
        final Collection<String> children = tree.getChildren("unknown node");

        assertThat(0, is(children.size()));
        assertTreeNotModified();
    }

    @Theory
    public void getRootShouldReturnNullBeforeSetRoot(final Tree<String> tree) {
        assertNull(tree.getRoot());
        assertTreeNotModified();
    }

    @Theory
    public void getRootShouldReturnSetRoot(Tree<String> tree) {
        tree = tree.withRoot("root");
        assertEquals("root", tree.getRoot());
        assertTreeNotModified();
    }

    @Theory
    public void setRootShouldClearTree(Tree<String> tree) {
        tree = tree.withRoot("God");
        tree = tree.plus("God", "Jesus");
        tree = tree.withRoot("Allah");
        assertFalse(tree.contains("Muhammad"));
        assertTreeNotModified();
    }

    @Theory
    public void plusShouldWorkCorrectly(Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.plus("root", "males").plus("root", "females");
        tree = tree.plus("males", "Paul").plus("males", "Peter")
                .plus("males", "John");
        tree = tree.plus("females", "Maria");

        final Collection<String> children = tree.getChildren("males");

        assertThat(3, is(children.size()));
        assertThat(children, hasItems("John", "Paul", "Peter"));
        assertTreeNotModified();
    }

    @Theory
    public void plusShouldReturnAnIdenticalTreeOnReinsertingNode(
            Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.plus("root", "males").plus("root", "females");
        tree = tree.plus("males", "Paul");

        final Tree<String> newTree = tree.plus("root", "males");

        EqualsBuilder.reflectionEquals(tree, newTree);
        assertTreeNotModified();
    }

    @Theory
    public void plusShouldThrowIllegalArgumentExceptionOnInsertingAnUnknownParent(
            Tree<String> tree) {
        tree = tree.withRoot("root");

        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage("does not contain parent node unknown parent");

        tree.plus("unknown parent", "node");
        assertTreeNotModified();
    }

    @Theory
    public void minusShouldCascadeRemove(Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.plus("root", "males").plus("root", "females")
                .plus("root", "unsure");
        tree = tree.plus("males", "Peter").plus("males", "Paul");
        tree = tree.plus("females", "Maria");

        tree = tree.minus("males");

        assertFalse(tree.contains("males"));
        final Collection<String> rootChildren = tree.getChildren("root");
        assertThat(2, is(rootChildren.size()));
        assertFalse(rootChildren.contains("males"));
        assertFalse(tree.contains("Peter"));
        assertNotSame("root", tree.getParent("males"));
        assertTreeNotModified();
    }

    @Theory
    public void minusShouldNotThrowAConcurrentModificationException(
            Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.plus("root", "males");
        tree = tree.plus("males", "Peter").plus("males", "Paul");

        tree.minus("males");
        assertTreeNotModified();
    }

}
