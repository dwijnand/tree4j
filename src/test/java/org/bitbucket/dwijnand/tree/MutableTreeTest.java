package org.bitbucket.dwijnand.tree;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class MutableTreeTest extends TreeTest {

    @DataPoints
    public static MutableTree<?>[] data() {
        return new MutableTree[] {MultimapTree.<String> create()};
    }

    // TODO add tests for MutableTree's additional methods

}
