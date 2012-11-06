package org.dapacode.tree4j;

import org.dapacode.tree4j.testutils.TreeHelper;
import org.dapacode.tree4j.testutils.TreeHelper.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.dapacode.tree4j.testutils.TreeHelper.withoutModifying;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(Theories.class)
// CSOFF: WhitespaceAroundCheck
@SuppressWarnings({"LocalCanBeFinal", "InstanceMethodNamingConvention", "DesignForExtension"})
// CSON: WhitespaceAroundCheck
public class CopyOfTest {
  @DataPoints
  public static Tree<?>[] data() {
    return new Tree<?>[]{MultimapTree.create(), ImmutableMultimapTree.create()};
  }

  @Theory
  public void immutableMultimapTreeCopyOfShouldReturnAnEqualButNotSameTree(Tree<String> tree) {
    tree = setupTreeTestData(tree);

    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        ImmutableMultimapTree<String> copyTree = ImmutableMultimapTree.copyOf(tree);
        assertThat(copyTree, is(not(sameInstance(tree))));
        assertThat(copyTree, is(equalTo(tree)));
      }
    });
  }

  @Theory
  public void multimapTreeCopyOfShouldReturnAnEqualButNotSameTree(Tree<String> tree) {
    tree = setupTreeTestData(tree);

    withoutModifying(tree, new Test<Tree<String>>() {
      @Override
      public void apply(Tree<String> tree) {
        final MultimapTree<String> copyTree = MultimapTree.copyOf(tree);
        assertThat(copyTree, is(not(sameInstance(tree))));
        assertThat(copyTree, is(equalTo(tree)));
      }
    });
  }

  private Tree<String> setupTreeTestData(Tree<String> tree) {
    if (tree instanceof ImmutableTree) {
      return TreeHelper.setupTreeTestData((ImmutableTree<String>) tree);
    } else if (tree instanceof MultimapTree) {
      return TreeHelper.setupTreeTestData((MultimapTree<String>) tree);
    } else {
      throw new AssertionError("Unexpected Tree type: " + tree.getClass());
    }
  }
}
