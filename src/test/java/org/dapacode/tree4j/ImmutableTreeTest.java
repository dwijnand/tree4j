package org.dapacode.tree4j;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.dapacode.tree4j.testutils.TreeHelper;
import org.junit.experimental.theories.Theory;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItems;

/**
 * This class defines the tests for compliance to the specifications defined in the {@link ImmutableTree} interface.
 * <p/>
 * As can be read in the javadoc of the ImmutableTree, which extends {@link Tree}, the only addition to the Tree interface is
 * that the object is immutable. It is, however, impossible to prove that something <em>doesn't</em> happen. Therefore, this
 * class simply extends {@link TreeTest}, passing ImmutableTree implementations to those tests.
 */
// CSOFF: WhitespaceAroundCheck
@SuppressWarnings({"FeatureEnvy", "InstanceMethodNamingConvention", "DesignForExtension", "LocalCanBeFinal"})
// CSON: WhitespaceAroundCheck
public class ImmutableTreeTest extends TreeTest<ImmutableTree<String>> {
  @Theory
  public void withRootShouldReturnATreeWithoutAPreviousNode(ImmutableTree<String> immutableTree) {
    withoutModifying(immutableTree, new Test<ImmutableTree<String>>() {
      @Override
      public void apply(ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.added("R", "1");

        immutableTree = immutableTree.withRoot("S");

        assertFalse(immutableTree.contains("1"));
      }
    });
  }

  @Theory
  public void addedShouldWorkCorrectly(ImmutableTree<String> immutableTree) {
    withoutModifying(immutableTree, new Test<ImmutableTree<String>>() {
      @Override
      public void apply(ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.added("R", "1");
        immutableTree = immutableTree.added("R", "2");
        immutableTree = immutableTree.added("1", "a");
        immutableTree = immutableTree.added("1", "b");
        immutableTree = immutableTree.added("2", "c");

        Collection<String> children = immutableTree.getChildren("1");

        assertEquals(2, children.size());
        assertThat(children, hasItems("a", "b"));
      }
    });
  }

  @Theory
  public void addedShouldReturnAnIdenticalTreeOnReinsertingNode(ImmutableTree<String> immutableTree) {
    withoutModifying(immutableTree, new Test<ImmutableTree<String>>() {
      @Override
      public void apply(ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.added("R", "1");
        immutableTree = immutableTree.added("R", "2");
        immutableTree = immutableTree.added("1", "a");

        Tree<String> newTree = immutableTree.added("R", "1");

        assertSame(immutableTree, newTree);
        EqualsBuilder.reflectionEquals(immutableTree, newTree);
      }
    });
  }

  @Theory
  public void addedShouldThrowIllegalArgumentExceptionOnInsertingAnUnknownParent(ImmutableTree<String> immutableTree) {
    withoutModifying(immutableTree, new Test<ImmutableTree<String>>() {
      @Override
      public void apply(ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("R");

        expectedException.expect(IllegalArgumentException.class);

        immutableTree.added("unknown parent", "node");
      }
    });
  }

  @Theory
  public void addedShouldThrowIllegalArgumentExceptionOnInsertingAnAlreadyAssociatedChild(
      ImmutableTree<String> immutableTree) {
    withoutModifying(immutableTree, new Test<ImmutableTree<String>>() {
      @Override
      public void apply(ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.added("R", "1");
        immutableTree = immutableTree.added("1", "a");

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The child node (1) is already associated to another node");

        immutableTree.added("a", "1");
      }
    });
  }

  @Theory
  // TODO split this into smaller asserting tests
  public void removedShouldCascadeRemove(ImmutableTree<String> immutableTree) {
    withoutModifying(immutableTree, new Test<ImmutableTree<String>>() {
      @Override
      public void apply(ImmutableTree<String> immutableTree) {
        immutableTree = setupTreeTestData(immutableTree);

        immutableTree = immutableTree.removed("2");

        assertFalse(immutableTree.contains("2"));
        Collection<String> rootChildren = immutableTree.getChildren("R");
        assertEquals(1, rootChildren.size());
        assertFalse(rootChildren.contains("2"));
        assertFalse(immutableTree.contains("c"));
        assertTrue(immutableTree.contains("a"));
        assertTrue(immutableTree.contains("!"));
      }
    });
  }

  @Theory
  public void removedShouldNotThrowAConcurrentModificationException(ImmutableTree<String> immutableTree) {
    withoutModifying(immutableTree, new Test<ImmutableTree<String>>() {
      @Override
      public void apply(ImmutableTree<String> immutableTree) {
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.added("R", "1");
        immutableTree = immutableTree.added("1", "a");
        immutableTree = immutableTree.added("1", "b");

        immutableTree.removed("1");
      }
    });
  }

  @Theory
  public void removedShouldReturnAnEmptyTreeOnRootNode(ImmutableTree<String> immutableTree) {
    withoutModifying(immutableTree, new Test<ImmutableTree<String>>() {
      @Override
      public void apply(ImmutableTree<String> immutableTree) {
        immutableTree = setupTreeTestData(immutableTree);

        immutableTree = immutableTree.removed(immutableTree.getRoot());

        assertNull(immutableTree.getRoot());
        assertFalse(immutableTree.contains("R"));
        assertFalse(immutableTree.contains("1"));
        assertFalse(immutableTree.contains("a"));
        assertFalse(immutableTree.contains("!"));
      }
    });
  }

  @Theory
  public void removedShouldReturnTheSameTreeOnUnknownNode(ImmutableTree<String> immutableTree) {
    withoutModifying(immutableTree, new Test<ImmutableTree<String>>() {
      @Override
      public void apply(ImmutableTree<String> immutableTree) {
        immutableTree = setupTreeTestData(immutableTree);

        ImmutableTree<String> newTree = immutableTree.removed("unknown node");

        assertThat(newTree, is(sameInstance(immutableTree)));
      }
    });
  }

  @Override
  @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
  public ImmutableTree<String> withRoot(ImmutableTree<String> immutableTree, String root) {
    return immutableTree.withRoot(root);
  }

  @Override
  @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
  public ImmutableTree<String> plus(ImmutableTree<String> immutableTree, String parent, String child) {
    return immutableTree.added(parent, child);
  }

  @Override
  @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
  public ImmutableTree<String> setupTreeTestData(ImmutableTree<String> immutableTree) {
    return TreeHelper.setupTreeTestData(immutableTree);
  }
}
