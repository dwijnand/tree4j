package org.dapacode.tree4j;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.experimental.theories.Theory;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

/**
 * This class defines the tests for compliance to the specifications defined in the {@link ImmutableTree} interface.
 * <p/>
 * As can be read in the javadoc of the ImmutableTree, which extends {@link Tree}, the only addition to the Tree interface is
 * that the object is immutable. It is, however, impossible to prove that something <em>doesn't</em> happen. Therefore, this
 * class simply extends {@link TreeTest}, passing ImmutableTree implementations to those tests.
 */
@SuppressWarnings({"FeatureEnvy", "InstanceMethodNamingConvention"})
public class ImmutableTreeTest extends TreeTest {
  public ImmutableTreeTest(final ImmutableTree<?> tree) {
    super(tree);
  }

  @Theory
  public void withRootShouldReturnATreeWithoutAPreviousNode(ImmutableTree<String> immutableTree) {
    immutableTree = immutableTree.withRoot("R");
    immutableTree = immutableTree.plus("R", "1");

    immutableTree = immutableTree.withRoot("S");

    assertFalse(immutableTree.contains("1"));
    assertTreeNotModified();
  }

  @Theory
  public void plusShouldWorkCorrectly(ImmutableTree<String> immutableTree) {
    immutableTree = immutableTree.withRoot("R");
    immutableTree = immutableTree.plus("R", "1");
    immutableTree = immutableTree.plus("R", "2");
    immutableTree = immutableTree.plus("1", "a");
    immutableTree = immutableTree.plus("1", "b");
    immutableTree = immutableTree.plus("2", "c");

    final Collection<String> children = immutableTree.getChildren("1");

    assertEquals(2, children.size());
    assertThat(children, hasItems("a", "b"));
    assertTreeNotModified();
  }

  @Theory
  public void plusShouldReturnAnIdenticalTreeOnReinsertingNode(ImmutableTree<String> immutableTree) {
    immutableTree = immutableTree.withRoot("R");
    immutableTree = immutableTree.plus("R", "1");
    immutableTree = immutableTree.plus("R", "2");
    immutableTree = immutableTree.plus("1", "a");

    final Tree<String> newTree = immutableTree.plus("R", "1");

    EqualsBuilder.reflectionEquals(immutableTree, newTree);
    assertTreeNotModified();
  }

  @Theory
  public void plusShouldThrowIllegalArgumentExceptionOnInsertingAnUnknownParent(ImmutableTree<String> immutableTree) {
    immutableTree = immutableTree.withRoot("R");

    expectedException.expect(IllegalArgumentException.class);

    immutableTree.plus("unknown parent", "node");
    assertTreeNotModified();
  }

  @Theory
  // TODO split this into smaller asserting tests
  public void minusShouldCascadeRemove(ImmutableTree<String> immutableTree) {
    immutableTree = immutableTree.withRoot("R");
    immutableTree = immutableTree.plus("R", "1");
    immutableTree = immutableTree.plus("R", "2");
    immutableTree = immutableTree.plus("R", "3");
    immutableTree = immutableTree.plus("1", "a");
    immutableTree = immutableTree.plus("1", "b");
    immutableTree = immutableTree.plus("2", "c");

    immutableTree = immutableTree.minus("1");

    assertFalse(immutableTree.contains("1"));
    final Collection<String> rootChildren = immutableTree.getChildren("R");
    assertEquals(2, rootChildren.size());
    assertFalse(rootChildren.contains("1"));
    assertFalse(immutableTree.contains("a"));

    assertTreeNotModified();
  }

  @Theory
  public void minusShouldNotThrowAConcurrentModificationException(ImmutableTree<String> immutableTree) {
    immutableTree = immutableTree.withRoot("R");
    immutableTree = immutableTree.plus("R", "1");
    immutableTree = immutableTree.plus("1", "a");
    immutableTree = immutableTree.plus("1", "b");

    immutableTree.minus("1");
    assertTreeNotModified();
  }

  @Override
  public Tree<String> withRoot(final Tree<String> tree, final String root) {
    return ((ImmutableTree<String>) tree).withRoot(root);
  }

  @Override
  public Tree<String> plus(final Tree<String> tree, final String parent, final String child) {
    return ((ImmutableTree<String>) tree).plus(parent, child);
  }

  @Override
  public Tree<String> minus(final Tree<String> tree, final String node) {
    return ((ImmutableTree<String>) tree).minus(node);
  }

  @Override
  public Tree<String> setupTreeTestData(final Tree<String> tree) {
    ImmutableTree<String> immutableTree = (ImmutableTree<String>) tree;
    immutableTree = immutableTree.withRoot("R");
    immutableTree = immutableTree.plus("R", "1");
    immutableTree = immutableTree.plus("R", "2");
    immutableTree = immutableTree.plus("1", "a");
    immutableTree = immutableTree.plus("1", "b");
    immutableTree = immutableTree.plus("2", "c");
    return immutableTree;
  }
}
