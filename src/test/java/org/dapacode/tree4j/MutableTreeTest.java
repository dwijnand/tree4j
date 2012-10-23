package org.dapacode.tree4j;

import org.junit.experimental.theories.Theory;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * This class defines the tests for compliance to the specifications defined in the {@link MutableTree} interface.
 * <p/>
 * As MutableTree extends Tree, this class extends {@link TreeTest} adding tests for the methods defined in MutableTree. It,
 * therefore, passes MutableTree implementations to the tests in TreeTest and then to the tests defined here.
 */
// TODO make methods final
// CSOFF: WhitespaceAroundCheck
@SuppressWarnings({"FeatureEnvy", "InstanceMethodNamingConvention"})
// CSON: WhitespaceAroundCheck
public class MutableTreeTest extends TreeTest {
  @Theory
  public void setRootShouldThrowANPEOnNullNode(final MutableTree<String> mutableTree) {
    expectedException.expect(NullPointerException.class);
    mutableTree.setRoot(null);
  }

  @Theory
  public void setRootShouldSetTheRoot(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    assertEquals("R", mutableTree.getRoot());
  }

  @Theory
  public void setRootShouldRemoveAllExistingNodes(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");

    mutableTree.setRoot("S");

    assertFalse(mutableTree.contains("1"));
  }

  @Theory
  public void addShouldThrowANPEOnNullParentNode(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    expectedException.expect(NullPointerException.class);
    mutableTree.add(null, "1");
  }

  @Theory
  public void addShouldThrowANPEOnNullChildNode(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    expectedException.expect(NullPointerException.class);
    mutableTree.add("R", null);
  }

  @Theory
  public void addShouldThrowAnIAEOnUnknownParentNode(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    expectedException.expect(IllegalArgumentException.class);
    mutableTree.add("unknown node", "1");
  }

  @Theory
  public void addShouldThrowAnIAEOnAlreadyAssociatedChildNode(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");
    mutableTree.add("1", "a");
    expectedException.expect(IllegalArgumentException.class);
    mutableTree.add("a", "1");
  }

  @Theory
  public void addShouldThrowAnIAEWhenAssociatingAChildNodeToAnotherParentNode(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");
    mutableTree.add("R", "2");
    mutableTree.add("1", "a");
    expectedException.expect(IllegalArgumentException.class);
    mutableTree.add("2", "a");
  }

  @Theory
  public void addShouldNotThrowAnIAEWhenAddingTheSameAssociationTwice(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");
    mutableTree.add("1", "a");
    mutableTree.add("1", "a");
  }

  @Theory
  public void addShouldReturnFalseWhenAddingTheSameAssociationTwice(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");
    mutableTree.add("1", "a");
    final boolean modified = mutableTree.add("1", "a");
    assertFalse(modified);
  }

  @Theory
  public void addShouldAddTheNodesToTheTree(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");

    mutableTree.add("R", "1");

    assertTrue(mutableTree.contains("R"));
    assertTrue(mutableTree.contains("1"));
  }

  @Theory
  public void addShouldReturnTrueOnModifyingTheTreeByAddingANewAssociation(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    assertTrue(mutableTree.add("R", "1"));
  }

  @Theory
  public void addShouldAddTheChildNodeToTheChildrenOfTheParentNode(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");

    mutableTree.add("R", "1");

    assertThat(mutableTree.getChildren("R"), hasItem("1"));
  }

  @Theory
  public void addShouldSetTheParentNodeAsTheParentOfTheChildNode(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");

    mutableTree.add("R", "1");

    assertEquals("R", mutableTree.getParent("1"));
  }

  @Theory
  public void clearShouldRemoveAllNodes(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");

    mutableTree.clear();

    assertFalse(mutableTree.contains("R"));
    assertFalse(mutableTree.contains("1"));
  }

  @Theory
  public void removeShouldThrowANPEOnNullNode(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    expectedException.expect(NullPointerException.class);
    mutableTree.remove(null);
  }

  @Theory
  public void removeShouldThrowAIAEOnUnknownNode(final MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    expectedException.expect(IllegalArgumentException.class);
    mutableTree.remove("unknown node");
  }

  @Theory
  public void removeShouldRemoveTheSpecifiedNodeAndAllOfItsChildren(final MutableTree<String> mutableTree) {
    setupTreeTestData(mutableTree);

    mutableTree.remove("1");

    assertFalse(mutableTree.contains("1"));
    assertThat(mutableTree.getChildren("R"), not(hasItem("1")));
  }

  @Theory
  public void removeShouldReturnTrueOnModifyingTheTreeByRemovingANode(final MutableTree<String> mutableTree) {
    setupTreeTestData(mutableTree);
    assertTrue(mutableTree.remove("1"));
  }

  @Override
  public Tree<String> withRoot(final Tree<String> tree, final String root) {
    ((MutableTree<String>) tree).setRoot(root);
    return tree;
  }

  @Override
  public Tree<String> plus(final Tree<String> tree, final String parent, final String child) {
    ((MutableTree<String>) tree).add(parent, child);
    return tree;
  }

  @Override
  public Tree<String> minus(final Tree<String> tree, final String node) {
    ((MutableTree<String>) tree).remove(node);
    return tree;
  }

  @Override
  public Tree<String> setupTreeTestData(final Tree<String> tree) {
    final MutableTree<String> mutableTree = (MutableTree<String>) tree;
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");
    mutableTree.add("R", "2");
    mutableTree.add("1", "a");
    mutableTree.add("1", "b");
    mutableTree.add("2", "c");
    return mutableTree;
  }
}
