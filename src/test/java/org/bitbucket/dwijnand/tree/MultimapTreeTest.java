package org.bitbucket.dwijnand.tree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class MultimapTreeTest {

    private Tree<String> tree;

    @Before
    public void setUp() {
        tree = MultimapTree.create();
    }

    @Test
    public void shouldContainAddedNode() {
        tree.setRoot("root");
        tree.add("root", "node");
        assertTrue(tree.contains("node"));
    }

    @Test
    public void shouldContainTheSetRoot() {
        tree.setRoot("root");
        assertTrue(tree.contains("root"));
    }

    @Test
    public void shouldHaveANullRootBeforeSetRoot() {
        assertNull(tree.getRoot());
    }

    @Test
    public void shouldHaveReturnSetRootOnGetRoot() {
        tree.setRoot("root");
        assertEquals("root", tree.getRoot());
    }

    @Test
    public void shouldClearTreeAfterSetRoot() {
        tree.setRoot("first root");
        tree.add("first root", "a child");
        tree.setRoot("second root");
        assertFalse(tree.contains("a child"));
    }

    @Test
    public void shouldntContainNewlyCreatedNode() {
        assertFalse(tree.contains("new node"));
    }

    @Test
    public void shouldReturnCorrectChildren() {
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

    @Test
    public void shouldCascadeRemove() {
        tree.setRoot("one");
        tree.add("one", "two");
        tree.add("two", "three");

        tree.remove("two");

        assertFalse(tree.contains("two"));
        assertFalse(tree.getChildren("one").contains("two"));
        assertFalse(tree.contains("three"));
    }

}
