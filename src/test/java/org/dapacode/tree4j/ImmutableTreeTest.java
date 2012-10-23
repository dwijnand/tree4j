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
// CSOFF: WhitespaceAroundCheck
@SuppressWarnings({"FeatureEnvy", "InstanceMethodNamingConvention"})
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

        final Collection<String> children = immutableTree.getChildren("1");

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

        final Tree<String> newTree = immutableTree.added("R", "1");

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
        immutableTree = immutableTree.withRoot("R");
        immutableTree = immutableTree.added("R", "1");
        immutableTree = immutableTree.added("R", "2");
        immutableTree = immutableTree.added("R", "3");
        immutableTree = immutableTree.added("1", "a");
        immutableTree = immutableTree.added("1", "b");
        immutableTree = immutableTree.added("2", "c");

        immutableTree = immutableTree.removed("1");

        assertFalse(immutableTree.contains("1"));
        final Collection<String> rootChildren = immutableTree.getChildren("R");
        assertEquals(2, rootChildren.size());
        assertFalse(rootChildren.contains("1"));
        assertFalse(immutableTree.contains("a"));
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
      }
    });
  }

  @Override
  public ImmutableTree<String> withRoot(final ImmutableTree<String> immutableTree, final String root) {
    return immutableTree.withRoot(root);
  }

  @Override
  public ImmutableTree<String> plus(final ImmutableTree<String> immutableTree, final String parent, final String child) {
    return immutableTree.added(parent, child);
  }

  @Override
  public ImmutableTree<String> minus(final ImmutableTree<String> immutableTree, final String node) {
    return immutableTree.removed(node);
  }

  @Override
  public ImmutableTree<String> setupTreeTestData(ImmutableTree<String> immutableTree) {
    immutableTree = immutableTree.withRoot("R");
    immutableTree = immutableTree.added("R", "1");
    immutableTree = immutableTree.added("R", "2");
    immutableTree = immutableTree.added("1", "a");
    immutableTree = immutableTree.added("1", "b");
    immutableTree = immutableTree.added("2", "c");
    return immutableTree;
  }
}
