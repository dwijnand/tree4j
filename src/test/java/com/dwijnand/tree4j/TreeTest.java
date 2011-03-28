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

import org.junit.Rule;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;

public class TreeTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Theory
    public void containsShouldReturnFalseWhenEmpty(final Tree<String> tree) {
        assertFalse(tree.contains("node"));
    }

    @Theory
    public void containsShouldReturnTrueOnAddedNode(Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.add("root", "node");
        assertTrue(tree.contains("node"));
    }

    @Theory
    public void containsShouldReturnTrueForSetRoot(Tree<String> tree) {
        tree = tree.withRoot("root");
        assertTrue(tree.contains("root"));
    }

    @Theory
    public void getParentShouldWorkCorrectly(Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.add("root", "1").add("root", "2");
        tree = tree.add("1", "1-1").add("1", "1-2").add("1", "1-3");
        tree = tree.add("2", "2-1");

        final String parent = tree.getParent("1-2");

        assertEquals("1", parent);
    }

    @Theory
    public void getParentShouldReturnNullForUnknownNode(
            final Tree<String> tree) {
        final String parent = tree.getParent("unknown node");

        assertNull(parent);
    }

    @Theory
    public void getChildrenShouldReturnEmptyCollectionOnUnknownNode(
            final Tree<String> tree) {
        final Collection<String> children = tree.getChildren("unknown node");

        assertThat(0, is(children.size()));
    }

    @Theory
    public void getRootShouldReturnNullBeforeSetRoot(final Tree<String> tree) {
        assertNull(tree.getRoot());
    }

    @Theory
    public void getRootShouldReturnSetRoot(Tree<String> tree) {
        tree = tree.withRoot("root");
        assertEquals("root", tree.getRoot());
    }

    @Theory
    public void setRootShouldClearTree(Tree<String> tree) {
        tree = tree.withRoot("first root");
        tree = tree.add("first root", "a child");
        tree = tree.withRoot("second root");
        assertFalse(tree.contains("a child"));
    }

    @Theory
    public void addShouldWorkCorrectly(Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.add("root", "1").add("root", "2");
        tree = tree.add("1", "1-1").add("1", "1-2").add("1", "1-3");
        tree = tree.add("2", "2-1");

        final Collection<String> children = tree.getChildren("1");

        assertThat(3, is(children.size()));
        assertThat(children, hasItems("1-1", "1-2", "1-3"));
    }

    @Theory
    public void addShouldReturnAnIdenticalTreeOnReinsertingNode(
            Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.add("root", "1").add("root", "2");
        tree = tree.add("1", "node");

        final Tree<String> newTree = tree.add("root", "1");

        assertEquals(tree, newTree);
    }

    @Theory
    public void addShouldThrowIllegalArgumentExceptionOnInsertingAnUnknownParent(
            Tree<String> tree) {
        tree = tree.withRoot("root");

        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage("does not contain parent node unknown parent");

        tree.add("unknown parent", "node");
    }

    @Theory
    public void removeShouldCascadeRemove(Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.add("root", "1").add("root", "2").add("root", "3");
        tree = tree.add("1", "1-1").add("1", "1-2");
        tree = tree.add("2", "2-1");

        tree = tree.remove("1");

        assertFalse(tree.contains("1"));
        final Collection<String> rootChildren = tree.getChildren("root");
        assertThat(2, is(rootChildren.size()));
        assertFalse(rootChildren.contains("1"));
        assertFalse(tree.contains("1-2"));
        assertNotSame("root", tree.getParent("1"));
    }

    @Theory
    public void removeShouldNotThrowAConcurrentModificationException(
            Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.add("root", "1");
        tree = tree.add("1", "1-1").add("1", "1-2");

        tree.remove("1");
    }
}
