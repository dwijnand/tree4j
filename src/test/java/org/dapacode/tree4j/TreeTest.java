package org.dapacode.tree4j;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.dapacode.tree4j.testutils.ObjectHashes;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.experimental.theories.Theory;
import org.junit.matchers.JUnitMatchers;
import org.junit.rules.ExpectedException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItems;

/**
 * This class defines the tests for compliance to the specifications defined in the {@link Tree} interface. It is used as the
 * base class for the test classes for the mutable and immutable extensions of this interface.
 */
// TODO make methods final
// CSOFF: WhitespaceAroundCheck
@SuppressWarnings(
    {"InstanceMethodNamingConvention", "NonBooleanMethodNameMayNotStartWithQuestion", "DesignForExtension", "LocalCanBeFinal"})
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

  @Theory
  public void iteratorShouldReturnAllNodes(T tree) {
    tree = setupTreeTestData(tree);
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        Iterator<Map.Entry<String, String>> iterator = tree.iterator();
        @SuppressWarnings("unchecked")
        Matcher<Iterable<Map.Entry<String, String>>> matcher = JUnitMatchers.<Map.Entry<String, String>>hasItems(
            ImmutablePair.of("R", "1"),
            ImmutablePair.of("R", "2"),
            ImmutablePair.of("1", "a"),
            ImmutablePair.of("1", "b"),
            ImmutablePair.of("2", "c"));
        assertThat(ImmutableList.copyOf(iterator), matcher);
      }
    });
  }

  public abstract T withRoot(T tree, String root);

  public abstract T plus(T tree, String parent, String child);

  public abstract T setupTreeTestData(T tree);

  protected static <T extends Tree<String>> void withoutModifying(T tree, Test<T> test) {
    long beforeHash = ObjectHashes.getCRCChecksum(tree);
    try {
      test.apply(tree);
    } finally {
      assertThat(ObjectHashes.getCRCChecksum(tree), is(equalTo(beforeHash)));
    }
  }

  protected interface Test<T extends Tree<String>> {
    void apply(T tree);
  }
}
