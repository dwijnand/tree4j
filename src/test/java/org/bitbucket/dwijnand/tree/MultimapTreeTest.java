package org.bitbucket.dwijnand.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class MultimapTreeTest {

    private Tree<String> tree;

    @Before
    public void setUp() {
        tree = MultimapTree.create();
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
