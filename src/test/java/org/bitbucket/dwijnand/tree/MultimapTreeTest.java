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
    public void emptyTreeShouldntContainNewNode() {
        assertFalse(tree.contains(new Node("Dale", 1)));
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
