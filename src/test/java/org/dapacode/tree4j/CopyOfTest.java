package org.dapacode.tree4j;

import org.dapacode.tree4j.testutils.TreeHelper.Test;

import static org.dapacode.tree4j.testutils.TreeHelper.setupTreeTestData;
import static org.dapacode.tree4j.testutils.TreeHelper.withoutModifying;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

// CSOFF: WhitespaceAroundCheck
@SuppressWarnings({"LocalCanBeFinal", "InstanceMethodNamingConvention", "DesignForExtension"})
// CSON: WhitespaceAroundCheck
public class CopyOfTest {
  @org.junit.Test
  public void immutableMultimapTreeCopyOfShouldReturnAnEqualButNotSameTreeOnMultimapTree() {
    testImmutableMultimapTreeCopyOf(setupTreeTestData(MultimapTree.<String>create()));
  }

  @org.junit.Test
  public void immutableMultimapTreeCopyOfShouldReturnAnEqualButNotSameTreeOnImmutableMultimapTree() {
    testImmutableMultimapTreeCopyOf(setupTreeTestData(ImmutableMultimapTree.<String>create()));
  }

  @org.junit.Test
  public void multimapTreeCopyOfShouldReturnAnEqualButNotSameTreeOnMultimapTree() {
    testMultimapTreeCopyOf(setupTreeTestData(MultimapTree.<String>create()));
  }

  @org.junit.Test
  public void multimapTreeCopyOfShouldReturnAnEqualButNotSameTreeOnImmutableMultimapTree() {
    testMultimapTreeCopyOf(setupTreeTestData(ImmutableMultimapTree.<String>create()));
  }

  @org.junit.Test
  public void immutableMultimapTreeCopyOfShouldReturnAnEqualButNotSameTreeOnDelegatingImmutableTree() {
    final ImmutableTree<String> delegate = setupTreeTestData(ImmutableMultimapTree.<String>create());
    testImmutableMultimapTreeCopyOf(new DelegatingImmutableTree<String>() {
      @Override
      protected ImmutableTree<String> delegate() {
        return delegate;
      }
    });
  }

  @org.junit.Test
  public void multimapTreeCopyOfShouldReturnAnEqualButNotSameTreeOnDelegatingMutableTree() {
    final MutableTree<String> delegate = setupTreeTestData(MultimapTree.<String>create());
    testMultimapTreeCopyOf(new DelegatingMutableTree<String>() {
      @Override
      protected MutableTree<String> delegate() {
        return delegate;
      }
    });
  }

  @org.junit.Test
  public void multimapTreeCopyOfShouldReturnAnEqualButNotSameTreeOnEmptyImmutableMultimapTree() {
    testMultimapTreeCopyOf(ImmutableMultimapTree.<String>create());
  }

  @org.junit.Test
  public void immutableMultimapTreeCopyOfShouldReturnAnEqualButNotSameTreeOnEmptyMultimapTree() {
    testImmutableMultimapTreeCopyOf(MultimapTree.<String>create());
  }

  @org.junit.Test
  public void multimapTreeCopyOfShouldReturnAnEqualButNotSameTreeOnEmptyDelegatingImmutableMultimapTree() {
    final ImmutableMultimapTree<String> delegate = ImmutableMultimapTree.create();
    testMultimapTreeCopyOf(new DelegatingImmutableTree<String>() {
      @Override
      protected ImmutableTree<String> delegate() {
        return delegate;
      }
    });
  }

  @org.junit.Test
  public void immutableMultimapTreeCopyOfShouldReturnAnEqualButNotSameTreeOnEmptyDelegatingMultimapTree() {
    final MultimapTree<String> delegate = MultimapTree.create();
    testImmutableMultimapTreeCopyOf(new DelegatingMutableTree<String>() {
      @Override
      protected MutableTree<String> delegate() {
        return delegate;
      }
    });
  }

  private void testImmutableMultimapTreeCopyOf(Tree<String> tree) {
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        ImmutableMultimapTree<String> copyTree = ImmutableMultimapTree.copyOf(tree);
        assertThat(copyTree, is(not(sameInstance(tree))));
        assertThat(copyTree, is(equalTo(tree)));
      }
    });
  }

  private void testMultimapTreeCopyOf(Tree<String> tree) {
    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        final MultimapTree<String> copyTree = MultimapTree.copyOf(tree);
        assertThat(copyTree, is(not(sameInstance(tree))));
        assertThat(copyTree, is(equalTo(tree)));
      }
    });
  }
}
