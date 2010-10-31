package org.bitbucket.dwijnand.tree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public void getChildrenShouldReturnCorrectChildren(final Tree<String> tree) {
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
    public void removeShouldCascadeRemove(final Tree<String> tree) {
        tree.setRoot("one");
        tree.add("one", "two");
        tree.add("two", "three");

        tree.remove("two");

        assertFalse(tree.contains("two"));
        assertFalse(tree.getChildren("one").contains("two"));
        assertFalse(tree.contains("three"));
    }

}
