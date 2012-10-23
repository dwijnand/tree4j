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
public abstract class TreeTest<T extends Tree<String>> {
  @Rule
  @SuppressWarnings("PublicField")
  public final ExpectedException expectedException = ExpectedException.none();

  @Theory
  public void containsShouldReturnFalseWhenEmpty(final T tree) {
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        assertFalse(tree.contains("node"));
      }
    });
  }

  @Theory
  public void containsShouldThrowANPEOnNullNode(final T tree) {
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        expectedException.expect(NullPointerException.class);
        tree.contains(null);
      }
    });
  }

  @Theory
  public void containsShouldReturnTrueOnAddedNode(T tree) {
    tree = withRoot(tree, "R");
    tree = plus(tree, "R", "1");
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        assertTrue(tree.contains("1"));
      }
    });
  }

  @Theory
  public void containsShouldReturnTrueForSetRoot(T tree) {
    tree = withRoot(tree, "R");
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        assertTrue(tree.contains("R"));
      }
    });
  }

  @Theory
  public void getParentShouldThrowANPEOnNullNode(T tree) {
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        expectedException.expect(NullPointerException.class);
        tree.getParent(null);
      }
    });
  }

  @Theory
  public void getParentShouldThrowAnIAEOnUnknownNode(T tree) {
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        expectedException.expect(IllegalArgumentException.class);
        tree.getParent("unknown node");
      }
    });
  }

  @Theory
  public void getParentShouldReturnTheExpectedNode(T tree) {
    tree = setupTreeTestData(tree);
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        final String parent = tree.getParent("a");
        assertEquals("1", parent);
      }
    });
  }

  @Theory
  public void getChildrenShouldThrowANPEOnNullNode(T tree) {
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        expectedException.expect(NullPointerException.class);
        tree.getChildren(null);
      }
    });
  }

  @Theory
  public void getChildrenShouldThrowAnIAEOnUnknownNode(T tree) {
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        expectedException.expect(IllegalArgumentException.class);
        tree.getChildren("unknown node");
      }
    });
  }

  @Theory
  public void getChildrenShouldReturnTheExpectedNodes(T tree) {
    tree = setupTreeTestData(tree);
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        final Collection<String> children = tree.getChildren("1");
        assertEquals(2, children.size());
        assertThat(children, hasItems("a", "b"));
      }
    });
  }

  @Theory
  public void getChildrenShouldReturnAnEmptyCollectionOnALeafNode(T tree) {
    tree = setupTreeTestData(tree);
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        final Collection<String> children = tree.getChildren("c");
        assertNotNull(children);
        assertEquals(0, children.size());
      }
    });
  }

  @Theory
  public void getRootShouldReturnNullBeforeSetRoot(T tree) {
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        assertNull(tree.getRoot());
      }
    });
  }

  @Theory
  public void getRootShouldReturnSetRoot(T tree) {
    tree = withRoot(tree, "R");
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        assertEquals("R", tree.getRoot());
      }
    });
  }

  public abstract T withRoot(T tree, String root);

  public abstract T plus(T tree, String parent, String child);

  public abstract T minus(T tree, String node);

  public abstract T setupTreeTestData(T tree);

  protected static <T extends Tree<String>> void withoutModifying(T tree, Test<T> test) {
    final long beforeHash = ObjectHashes.getCRCChecksum(tree);
    try {
      test.apply(tree);
    } finally {
      assertEquals(beforeHash, ObjectHashes.getCRCChecksum(tree));
    }
  }

  protected interface Test<T extends Tree<String>> {
    void apply(T tree);
  }
}
