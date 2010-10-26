package org.bitbucket.dwijnand.tree;

import static org.junit.Assert.assertFalse;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.junit.Before;
import org.junit.Test;

public class MultimapTreeTest {

    private Tree<Node> tree;

    @Before
    public void setUp() {
        tree = MultimapTree.create();
    }

    @Test
    public void shouldntContainNewlyCreatedNode() {
        assertFalse(tree.contains(new Node("Dale", 1)));
    }

    @Test
    public void shouldCascadeRemove() {
        final Node one = new Node("one", 1);
        tree.setRoot(one);
        final Node two = new Node("two", 2);
        final Node three = new Node("tree", 3);
        tree.add(one, two);
        tree.add(one, three);

        tree.remove(two);

        assertFalse(tree.contains(two));
        assertFalse(tree.getChildren(one).contains(two));
        assertFalse(tree.contains(three));
    }

    private static class Node implements Comparable<Node> {

        public final String name;

        public final Integer value;

        public Node(final String name, final Integer value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public int compareTo(final Node o) {
            return CompareToBuilder.reflectionCompare(this, o);
        }

    }

}
