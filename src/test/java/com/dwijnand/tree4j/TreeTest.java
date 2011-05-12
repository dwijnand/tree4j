package com.dwijnand.tree4j;

import com.dwijnand.tree4j.testutils.ObjectHashes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import org.junit.Rule;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;

/**
 * This class defines the tests for compliance to the specifications defined in
 * the {@link Tree} interface. It is used as the base class for the test classes
 * for the mutable and immutable extensions of this interface.
 */
// TODO make methods final
public abstract class TreeTest {
    @Rule
    @SuppressWarnings({"PublicField"})
    public ExpectedException expectedException = ExpectedException.none();

    private final Tree<?> tree;

    private final long beforeHash;

    public TreeTest(final Tree<?> tree) {
        this.tree = tree;
        beforeHash = calculateIdentity(tree);
    }

    protected final void assertTreeNotModified() {
        assertEquals(beforeHash, calculateIdentity(tree));
    }

    private long calculateIdentity(final Tree<?> tree) {
        return ObjectHashes.getCRCChecksum(tree);
    }

    @Theory
    public void containsShouldReturnFalseWhenEmpty(final Tree<String> tree) {
        assertFalse(tree.contains("node"));
        assertTreeNotModified();
    }

    public abstract void containsShouldReturnTrueOnAddedNode(Tree<String> tree);

    public abstract void containsShouldReturnTrueForSetRoot(Tree<String> tree);

    public abstract void getParentShouldWorkCorrectly(Tree<String> tree);

    @Theory
    public void getParentShouldThrowAnIAEOnUnknownNode(
            final Tree<String> tree) {
        expectedException.expect(IllegalArgumentException.class);
        tree.getParent("unknown node");
    }

    @Theory
    public void getChildrenShouldThrowAnIAEOnUnknownNode(
            final Tree<String> tree) {
        expectedException.expect(IllegalArgumentException.class);
        tree.getChildren("unknown node");
    }

    @Theory
    public void getRootShouldReturnNullBeforeSetRoot(final Tree<String> tree) {
        assertNull(tree.getRoot());
        assertTreeNotModified();
    }

    public abstract void getRootShouldReturnSetRoot(Tree<String> tree);

    public abstract void setRootShouldClearTree(Tree<String> tree);
}
