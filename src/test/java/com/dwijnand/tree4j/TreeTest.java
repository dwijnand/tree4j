package com.dwijnand.tree4j;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
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
    private final <T extends Tree<?>> int calculateIdentity(final T tree) {
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
        tree = tree.add("root", "node");
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
        tree = tree.add("root", "males").add("root", "females");
        tree = tree.add("males", "Paul").add("males", "Peter")
                .add("males", "John");
        tree = tree.add("females", "Maria");

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
        tree = tree.add("God", "Jesus");
        tree = tree.withRoot("Allah");
        assertFalse(tree.contains("Muhammad"));
        assertTreeNotModified();
    }

    @Theory
    public void addShouldWorkCorrectly(Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.add("root", "males").add("root", "females");
        tree = tree.add("males", "Paul").add("males", "Peter")
                .add("males", "John");
        tree = tree.add("females", "Maria");

        final Collection<String> children = tree.getChildren("males");

        assertThat(3, is(children.size()));
        assertThat(children, hasItems("John", "Paul", "Peter"));
        assertTreeNotModified();
    }

    @Theory
    public void addShouldReturnAnIdenticalTreeOnReinsertingNode(
            Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.add("root", "males").add("root", "females");
        tree = tree.add("males", "Paul");

        final Tree<String> newTree = tree.add("root", "males");

        EqualsBuilder.reflectionEquals(tree, newTree);
        assertTreeNotModified();
    }

    @Theory
    public void addShouldThrowIllegalArgumentExceptionOnInsertingAnUnknownParent(
            Tree<String> tree) {
        tree = tree.withRoot("root");

        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage("does not contain parent node unknown parent");

        tree.add("unknown parent", "node");
        assertTreeNotModified();
    }

    @Theory
    public void removeShouldCascadeRemove(Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.add("root", "males").add("root", "females")
                .add("root", "unsure");
        tree = tree.add("males", "Peter").add("males", "Paul");
        tree = tree.add("females", "Maria");

        tree = tree.remove("males");

        assertFalse(tree.contains("males"));
        final Collection<String> rootChildren = tree.getChildren("root");
        assertThat(2, is(rootChildren.size()));
        assertFalse(rootChildren.contains("males"));
        assertFalse(tree.contains("Peter"));
        assertNotSame("root", tree.getParent("males"));
        assertTreeNotModified();
    }

    @Theory
    public void removeShouldNotThrowAConcurrentModificationException(
            Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.add("root", "males");
        tree = tree.add("males", "Peter").add("males", "Paul");

        tree.remove("males");
        assertTreeNotModified();
    }

}
