package org.bitbucket.dwijnand.tree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class TreeTest {

    @DataPoints
    public static Tree<?>[] data() {
        return new Tree[] {MultimapTree.<String> create()};
    }

    @Theory
    public void containsShouldReturnFalseWhenEmpty(final Tree<String> tree) {
        assertFalse(tree.contains("node"));
    }

    @Theory
    public void containsShouldReturnTrueOnAddedNode(final Tree<String> tree) {
        tree.setRoot("root");
        tree.add("root", "node");
        assertTrue(tree.contains("node"));
    }

    @Theory
    public void containsShouldReturnTrueForSetRoot(final Tree<String> tree) {
        tree.setRoot("root");
        assertTrue(tree.contains("root"));
    }

    @Theory
    public void getParentShouldWorkCorrectly(final Tree<String> tree) {
        tree.setRoot("root");
        tree.add("root", "1");
        tree.add("1", "1-1");
        tree.add("1", "1-2");
        tree.add("1", "1-3");
        tree.add("root", "2");
        tree.add("2", "2-1");

        final String parent = tree.getParent("1-2");

        assertEquals("1", parent);
    }

    @Theory
    public void getParentShouldReturnNullForUnknownNode(final Tree<String> tree) {
        final String parent = tree.getParent("unknown node");

        assertNull(parent);
    }

    @Theory
    public void getChildrenShouldReturnEmptyCollectionOnUnknownNode(
            final Tree<String> tree) {
        final Collection<String> children = tree.getChildren("unknown node");

        assertNotNull(children);
        assertThat(0, is(children.size()));
    }

    @Theory
    public void getRootShouldReturnNullBeforeSetRoot(final Tree<String> tree) {
        assertNull(tree.getRoot());
    }

    @Theory
    public void getRootShouldReturnRootAfterSetRoot(final Tree<String> tree) {
        tree.setRoot("root");
        assertEquals("root", tree.getRoot());
    }

    @Theory
    public void setRootShouldClearTree(final Tree<String> tree) {
        tree.setRoot("first root");
        tree.add("first root", "a child");
        tree.setRoot("second root");
        assertFalse(tree.contains("a child"));
    }

    @Theory
    public void addShouldWorkCorrectly(final Tree<String> tree) {
        tree.setRoot("root");
        tree.add("root", "1");
        tree.add("1", "1-1");
        tree.add("1", "1-2");
        tree.add("1", "1-3");
        tree.add("root", "2");
        tree.add("2", "2-1");

        final Collection<String> children = tree.getChildren("1");

        assertThat(3, is(children.size()));
        assertThat(children, hasItems("1-1", "1-2", "1-3"));
    }

    @Theory
    public void addShouldReturnFalseOnReinsertingNode(final Tree<String> tree) {
        tree.setRoot("root");
        tree.add("root", "1");
        tree.add("root", "2");
        tree.add("1", "node");

        final boolean result = tree.add("2", "node");

        assertFalse(result);
        assertTrue(tree.contains("node"));
    }

    @Theory
    public void addShouldReturnFalseOnInsertingOnUnknownParent(
            final Tree<String> tree) {
        tree.setRoot("root");

        final boolean result = tree.add("unknown parent", "node");

        assertFalse(result);
        assertFalse(tree.contains("node"));
    }

    @Theory
    public void removeShouldCascadeRemove(final Tree<String> tree) {
        tree.setRoot("root");
        tree.add("root", "1");
        tree.add("root", "2");
        tree.add("root", "3");
        tree.add("1", "1-1");
        tree.add("1", "1-2");
        tree.add("2", "2-1");

        tree.remove("1");

        assertFalse(tree.contains("1"));
        final Collection<String> rootChildren = tree.getChildren("root");
        assertThat(2, is(rootChildren.size()));
        assertFalse(rootChildren.contains("1"));
        assertFalse(tree.contains("1-2"));
        assertNotSame("root", tree.getParent("1"));
    }

}
