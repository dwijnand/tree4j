package org.dapacode.tree4j;

import org.dapacode.tree4j.testutils.TreeHelper;
import org.junit.experimental.theories.Theory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * This class defines the tests for compliance to the specifications defined in the {@link MutableTree} interface.
 * <p/>
 * As MutableTree extends Tree, this class extends {@link TreeTest} adding tests for the methods defined in MutableTree. It,
 * therefore, passes MutableTree implementations to the tests in TreeTest and then to the tests defined here.
 */
// CSOFF: WhitespaceAroundCheck
@SuppressWarnings({"FeatureEnvy", "InstanceMethodNamingConvention", "DesignForExtension", "LocalCanBeFinal"})
// CSON: WhitespaceAroundCheck
public class MutableTreeTest extends TreeTest<MutableTree<String>> {
  @Theory
  public void setRootShouldThrowANPEOnNullNode(MutableTree<String> mutableTree) {
    expectedException.expect(NullPointerException.class);
    mutableTree.setRoot(null);
  }

  @Theory
  public void setRootShouldSetTheRoot(MutableTree<String> mutableTree) {
    boolean changed = mutableTree.setRoot("R");
    assertTrue(changed);
    assertEquals("R", mutableTree.getRoot());
  }

  @Theory
  public void setRootShouldRemoveAllExistingNodes(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");

    mutableTree.setRoot("S");

    assertFalse(mutableTree.contains("1"));
  }

  @Theory
  public void setRootShouldntChangeAnythingWhenPassedRoot(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    boolean changed = mutableTree.setRoot("R");
    assertFalse(changed);
  }

  @Theory
  public void addShouldThrowANPEOnNullParentNode(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    expectedException.expect(NullPointerException.class);
    mutableTree.add(null, "1");
  }

  @Theory
  public void addShouldThrowANPEOnNullChildNode(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    expectedException.expect(NullPointerException.class);
    mutableTree.add("R", null);
  }

  @Theory
  public void addShouldThrowAnIAEOnUnknownParentNode(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    expectedException.expect(IllegalArgumentException.class);
    mutableTree.add("unknown node", "1");
  }

  @Theory
  public void addShouldThrowAnIAEOnAlreadyAssociatedChildNode(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");
    mutableTree.add("1", "a");
    expectedException.expect(IllegalArgumentException.class);
    mutableTree.add("a", "1");
  }

  @Theory
  public void addShouldThrowAnIAEWhenAssociatingAChildNodeToAnotherParentNode(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");
    mutableTree.add("R", "2");
    mutableTree.add("1", "a");
    expectedException.expect(IllegalArgumentException.class);
    mutableTree.add("2", "a");
  }

  @Theory
  public void addShouldNotThrowAnIAEWhenAddingTheSameAssociationTwice(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");
    mutableTree.add("1", "a");
    mutableTree.add("1", "a");
  }

  @Theory
  public void addShouldReturnFalseWhenAddingTheSameAssociationTwice(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");
    mutableTree.add("1", "a");
    boolean modified = mutableTree.add("1", "a");
    assertFalse(modified);
  }

  @Theory
  public void addShouldAddTheNodesToTheTree(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");

    mutableTree.add("R", "1");

    assertTrue(mutableTree.contains("R"));
    assertTrue(mutableTree.contains("1"));
  }

  @Theory
  public void addShouldReturnTrueOnModifyingTheTreeByAddingANewAssociation(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    assertTrue(mutableTree.add("R", "1"));
  }

  @Theory
  public void addShouldAddTheChildNodeToTheChildrenOfTheParentNode(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");

    mutableTree.add("R", "1");

    assertThat(mutableTree.getChildren("R"), hasItem("1"));
  }

  @Theory
  public void addShouldSetTheParentNodeAsTheParentOfTheChildNode(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");

    mutableTree.add("R", "1");

    assertEquals("R", mutableTree.getParent("1"));
  }

  @Theory
  public void clearShouldRemoveAllNodes(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    mutableTree.add("R", "1");

    mutableTree.clear();

    assertNull(mutableTree.getRoot());
    assertFalse(mutableTree.contains("R"));
    assertFalse(mutableTree.contains("1"));
  }

  @Theory
  public void clearShouldRemoveAllNodes2(MutableTree<String> mutableTree) {
    setupTreeTestData(mutableTree);
    mutableTree.clear();
    mutableTree.setRoot("R");
    assertEquals(0, mutableTree.getChildren("R").size());
  }

  @Theory
  public void removeShouldThrowANPEOnNullNode(MutableTree<String> mutableTree) {
    mutableTree.setRoot("R");
    expectedException.expect(NullPointerException.class);
    mutableTree.remove(null);
  }

  @Theory
  public void removeShouldThrowAIAEOnUnknownNode(MutableTree<String> mutableTree) {
    expectedException.expect(IllegalArgumentException.class);
    mutableTree.remove("unknown node");
  }

  @Theory
  public void removeShouldRemoveTheSpecifiedNodeAndAllOfItsChildren(MutableTree<String> mutableTree) {
    setupTreeTestData(mutableTree);

    mutableTree.remove("1");

    assertThat(mutableTree.getChildren("R"), not(hasItem("1")));
    assertFalse(mutableTree.contains("1"));
    assertFalse(mutableTree.contains("a"));
    assertFalse(mutableTree.contains("!"));
  }

  @Theory
  public void removeShouldReturnTrueOnModifyingTheTreeByRemovingANode(MutableTree<String> mutableTree) {
    setupTreeTestData(mutableTree);
    assertTrue(mutableTree.remove("1"));
  }

  @Theory
  public void removeShouldEmptyTheTreeOnRootNode(MutableTree<String> mutableTree) {
    setupTreeTestData(mutableTree);

    boolean changed = mutableTree.remove(mutableTree.getRoot());

    assertNull(mutableTree.getRoot());
    assertFalse(mutableTree.contains("R"));
    assertFalse(mutableTree.contains("1"));
    assertFalse(mutableTree.contains("a"));
    assertTrue(changed);
  }

  @Override
  @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
  public MutableTree<String> withRoot(MutableTree<String> mutableTree, String root) {
    mutableTree.setRoot(root);
    return mutableTree;
  }

  @Override
  @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
  public MutableTree<String> plus(MutableTree<String> mutableTree, String parent, String child) {
    mutableTree.add(parent, child);
    return mutableTree;
  }

  @Override
  @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
  public MutableTree<String> setupTreeTestData(MutableTree<String> mutableTree) {
    return TreeHelper.setupTreeTestData(mutableTree);
  }
}
