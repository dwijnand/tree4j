package org.dapacode.tree4j;

import org.dapacode.tree4j.testutils.ObjectHashes;
import org.junit.Rule;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

/**
 * This class defines the tests for compliance to the specifications defined in the {@link Tree} interface. It is used as the
 * base class for the test classes for the mutable and immutable extensions of this interface.
 */
// TODO make methods final
// CSOFF: WhitespaceAroundCheck
@SuppressWarnings({"InstanceMethodNamingConvention", "NonBooleanMethodNameMayNotStartWithQuestion"})
// CSON: WhitespaceAroundCheck
public abstract class TreeTest {
  @Rule
  @SuppressWarnings("PublicField")
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

  private static long calculateIdentity(final Tree<?> tree) {
    return ObjectHashes.getCRCChecksum(tree);
  }

  @Theory
  public void containsShouldReturnFalseWhenEmpty(final Tree<String> tree) {
    assertFalse(tree.contains("node"));
    assertTreeNotModified();
  }

  @Theory
  public void containsShouldThrowANPEOnNullNode(final Tree<String> tree) {
    expectedException.expect(NullPointerException.class);
    tree.contains(null);
  }

  @Theory
  public void containsShouldReturnTrueOnAddedNode(Tree<String> tree) {
    tree = withRoot(tree, "R");
    tree = plus(tree, "R", "1");
    assertTrue(tree.contains("1"));
    assertTreeNotModified();
  }

  @Theory
  public void containsShouldReturnTrueForSetRoot(Tree<String> tree) {
    tree = withRoot(tree, "R");
    assertTrue(tree.contains("R"));
    assertTreeNotModified();
  }

  @Theory
  public void getParentShouldThrowANPEOnNullNode(final Tree<String> tree) {
    expectedException.expect(NullPointerException.class);
    tree.getParent(null);
  }

  @Theory
  public void getParentShouldThrowAnIAEOnUnknownNode(final Tree<String> tree) {
    expectedException.expect(IllegalArgumentException.class);
    tree.getParent("unknown node");
  }

  @Theory
  public void getParentShouldReturnTheExpectedNode(Tree<String> tree) {
    tree = setupTreeTestData(tree);

    final String parent = tree.getParent("a");

    assertEquals("1", parent);
    assertTreeNotModified();
  }

  @Theory
  public void getChildrenShouldThrowANPEOnNullNode(final Tree<String> tree) {
    expectedException.expect(NullPointerException.class);
    tree.getChildren(null);
  }

  @Theory
  public void getChildrenShouldThrowAnIAEOnUnknownNode(final Tree<String> tree) {
    expectedException.expect(IllegalArgumentException.class);
    tree.getChildren("unknown node");
  }

  @Theory
  public void getChildrenShouldReturnTheExpectedNodes(Tree<String> tree) {
    tree = setupTreeTestData(tree);

    final Collection<String> children = tree.getChildren("1");

    assertEquals(2, children.size());
    assertThat(children, hasItems("a", "b"));
  }

  @Theory
  public void getChildrenShouldReturnAnEmptyCollectionOnALeafNode(Tree<String> tree) {
    tree = setupTreeTestData(tree);

    final Collection<String> children = tree.getChildren("c");

    assertNotNull(children);
    assertEquals(0, children.size());
  }

  @Theory
  public void getRootShouldReturnNullBeforeSetRoot(final Tree<String> tree) {
    assertNull(tree.getRoot());
    assertTreeNotModified();
  }

  @Theory
  public void getRootShouldReturnSetRoot(Tree<String> tree) {
    tree = withRoot(tree, "R");
    assertEquals("R", tree.getRoot());
    assertTreeNotModified();
  }

  public abstract Tree<String> withRoot(Tree<String> tree, String root);

  public abstract Tree<String> plus(Tree<String> tree, String parent, String child);

  public abstract Tree<String> minus(Tree<String> tree, String node);

  public abstract Tree<String> setupTreeTestData(Tree<String> tree);
}
