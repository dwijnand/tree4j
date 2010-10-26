package org.bitbucket.dwijnand.tree;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class MultimapTreeTest {

    private Tree<String> tree;

    @Before
    public void setUp() {
        tree = MultimapTree.create();
    }

    @Test
    public void shouldntContainNewlyCreatedNode() {
        assertFalse(tree.contains("new node"));
    }

    @Test
    public void shouldCascadeRemove() {
        tree.setRoot("one");
        tree.add("one", "two");
        tree.add("one", "three");

        tree.remove("two");

        assertFalse(tree.contains("two"));
        assertFalse(tree.getChildren("one").contains("two"));
        assertFalse(tree.contains("three"));
    }

}
