package org.bitbucket.dwijnand.tree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class TreeTest {

    @DataPoints
    public static Tree<?>[] data() {
        // Use MultimapTree, but this is already tested in MutableTreeTest
        return new Tree[] {MultimapTree.<String> create()};
    }

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
    @Ignore("Need figure out how tree responds to reinserting a node")
    public void addShouldReturnFalseOnReinsertingNode(Tree<String> tree) {
        tree = tree.withRoot("root");
        tree = tree.add("root", "1").add("root", "2");
        tree = tree.add("1", "node");

        tree.add("root", "1");
        final Boolean result = null;

        assertFalse(result);
        assertTrue(tree.contains("node"));
    }

    @Theory
    @Ignore("Need figure out how tree responds to inserting on unknown node")
    public void addShouldReturnFalseOnInsertingOnUnknownParent(
            Tree<String> tree) {
        tree = tree.withRoot("root");

        tree.add("unknown parent", "node");
        final Boolean result = null;

        assertFalse(result);
        assertFalse(tree.contains("node"));
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
