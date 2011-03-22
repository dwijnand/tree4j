package com.dwijnand.tree;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.google.common.base.Supplier;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

@RunWith(Theories.class)
public class MutableTreeTest extends TreeTest {

    @DataPoints
    public static MutableTree<?>[] data() {
        final Supplier<Multimap<String, String>> childrenMultimapSupplier = new Supplier<Multimap<String, String>>() {

            @Override
            public Multimap<String, String> get() {
                return LinkedHashMultimap.create();
            }

        };

        final Supplier<Map<String, String>> parentsMapSupplier = new Supplier<Map<String, String>>() {

            @Override
            public Map<String, String> get() {
                return Maps.newHashMap();
            }

        };
        return new MutableTree[] {BaseMutableTree.create(
                childrenMultimapSupplier, parentsMapSupplier)};
    }

    @Theory
    public void setRootShouldSetTheRoot(final MutableTree<String> mutableTree) {
        mutableTree.setRoot("root");

        assertEquals("root", mutableTree.getRoot());
    }

    @Theory
    public void setRootShouldRemoveAllExistingNodes(
            final MutableTree<String> mutableTree) {
        mutableTree.setRoot("root");
        mutableTree.added("root", "child");

        mutableTree.setRoot("new root");

        assertFalse(mutableTree.contains("child"));
    }

    @Theory
    public void addedShouldAddTheParentChildAssociation(
            final MutableTree<String> multimapTree) {
        multimapTree.setRoot("root");

        multimapTree.added("root", "child");

        assertTrue(multimapTree.contains("root"));
        assertTrue(multimapTree.contains("child"));
        assertTrue(multimapTree.getChildren("root").contains("child"));
    }

    @Theory
    public void addedShouldThrowAnIllegalArgumentExceptionOnUnknownParent(
            final MutableTree<String> multimapTree) {
        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage("does not contain parent node unknown parent");

        multimapTree.added("unknown parent", "child");
    }

    @Theory
    public void clearShouldRemoveAllNodes(
            final MutableTree<String> multimapTree) {
        multimapTree.setRoot("root");
        multimapTree.added("root", "child");

        multimapTree.clear();

        assertFalse(multimapTree.contains("root"));
        assertFalse(multimapTree.contains("child"));
    }

    @Theory
    public void removedShouldRemoveTheSpecifiedNodeAndAllOfItsChildren(
            final MutableTree<String> multimapTree) {
        multimapTree.setRoot("root");
        multimapTree.added("root", "1").added("root", "2")
                .added("root", "3");
        multimapTree.added("1", "1-1").added("1", "1-2");
        multimapTree.added("2", "2-1");

        multimapTree.removed("1");

        assertFalse(multimapTree.contains("1"));
        final Collection<String> rootChildren = multimapTree
                .getChildren("root");
        assertThat(2, is(rootChildren.size()));
        assertFalse(rootChildren.contains("1"));
        assertFalse(multimapTree.contains("1-2"));
        assertNotSame("root", multimapTree.getParent("1"));
    }

}
