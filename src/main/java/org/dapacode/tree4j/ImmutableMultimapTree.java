package org.dapacode.tree4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dapacode.tree4j.common.Factory;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * A <strong>guaranteed</strong> {@link ImmutableTree}-compliant implementation that uses {@link ImmutableSetMultimap} and
 * {@link ImmutableMap}.
 *
 * @param <T> the type of the nodes in the tree
 */
// TODO see if it's possible to return ImmutableCollection on getChildren(T)
public final class ImmutableMultimapTree<T> extends AbstractMultimapTree<T> implements ImmutableTree<T> {
  // TODO document this class entirely
  public abstract static class ChildrenMaker<T> implements Factory<ImmutableSetMultimap.Builder<T, T>> {
    private ChildrenMaker() {}

    public static <T> ChildrenMaker<T> usingSetMultimap() {
      return new ChildrenMaker<T>() {
        @Override
        public ImmutableSetMultimap.Builder<T, T> get() {
          return ImmutableSetMultimap.builder();
        }
      };
    }
  }

  // TODO document this class entirely
  public abstract static class ParentsMaker<T> implements Factory<ImmutableMap.Builder<T, T>> {
    private ParentsMaker() {}

    public static <T> ParentsMaker<T> usingImmutableMap() {
      return new ParentsMaker<T>() {
        @Override
        public ImmutableMap.Builder<T, T> get() {
          return ImmutableMap.builder();
        }
      };
    }

    public static <T> ParentsMaker<T> usingImmutableSortedMapOrderedBy(final Comparator<T> comparator) {
      return new ParentsMaker<T>() {
        @Override
        public ImmutableSortedMap.Builder<T, T> get() {
          return new ImmutableSortedMap.Builder<T, T>(comparator);
        }
      };
    }

    public static <T extends Comparable<T>> ParentsMaker<T> usingImmutableSortedMapInNaturalOrder() {
      return new ParentsMaker<T>() {
        @Override
        public ImmutableSortedMap.Builder<T, T> get() {
          return new ImmutableSortedMap.Builder<T, T>(Ordering.natural());
        }
      };
    }

    public static <T extends Comparable<T>> ParentsMaker<T> usingImmutableSortedMapInReverseOrder() {
      return new ParentsMaker<T>() {
        @Override
        public ImmutableSortedMap.Builder<T, T> get() {
          return new ImmutableSortedMap.Builder<T, T>(Ordering.natural().reverse());
        }
      };
    }
  }

  /**
   * The factory of immutable multimap builder instances, used to build the parent-children associations of a new immutable
   * multimap tree.
   */
  private final ChildrenMaker<T> childrenMaker;

  /**
   * The factory of immutable map builder instances, used to build the child-parent associations of a new immutable multimap
   * tree.
   */
  private final ParentsMaker<T> parentsMaker;

  /** The root of the tree. */
  private final T root;

  /**
   * Creates a new immutable multimap tree using the specified {@link ChildrenMaker} and {@link ParentsMaker}.
   * <p/>
   * These factories are used to obtain builders required to constructing the parent-children and child-parent associations
   * within the tree.
   *
   * @param childrenMaker an ImmutableSetMultimap builder factory
   * @param parentsMaker an ImmutableMap builder factory
   */
  protected ImmutableMultimapTree(final ChildrenMaker<T> childrenMaker, final ParentsMaker<T> parentsMaker) {
    this(checkNotNull(childrenMaker), checkNotNull(parentsMaker),
        childrenMaker.get().build(), parentsMaker.get().build(), null);
  }

  /**
   * Creates a new multimap tree with the specified factories, associations and root node. See {@link #create(ChildrenMaker,
   * ParentsMaker)} for more details.
   * <p/>
   * This private constructor is used internally to set specific associations and root node, including setting the root node to
   * {@code null}.
   *
   * @param childrenMaker an ImmutableSetMultimap builder factory
   * @param parentsMaker an ImmutableMap builder factory
   * @param children the parent-children associations to be used
   * @param parents the child-parent associations to be used
   * @param root the root node
   */
  private ImmutableMultimapTree(final ChildrenMaker<T> childrenMaker, final ParentsMaker<T> parentsMaker,
                                final ImmutableSetMultimap<T, T> children, final ImmutableMap<T, T> parents, final T root) {
    super(children, parents);
    this.childrenMaker = childrenMaker;
    this.parentsMaker = parentsMaker;
    this.root = root;
  }

  /**
   * Create a new immutable multimap tree backed by a {@link com.google.common.collect.ListMultimap ListMultimap} and a {@link
   * ImmutableMap}.
   *
   * @param <T> the type of the nodes in the tree
   * @return a new immutable multimap tree
   */
  public static <T> ImmutableMultimapTree<T> create() {
    return create(ChildrenMaker.<T>usingSetMultimap(), ParentsMaker.<T>usingImmutableMap());
  }

  /**
   * Creates a new immutable multimap tree using the specified {@link ChildrenMaker} and {@link ParentsMaker}. See the {@link
   * ImmutableMultimapTree#ImmutableMultimapTree(ChildrenMaker, ParentsMaker)} constructor for more details, as it calls that
   * directly.
   *
   * @param <T> the type of the nodes in the tree
   * @param childrenMaker an ImmutableSetMultimap builder factory
   * @param parentsMaker an ImmutableMap builder factory
   * @return a new immutable multimap tree
   */
  public static <T> ImmutableMultimapTree<T> create(final ChildrenMaker<T> childrenMaker, final ParentsMaker<T> parentsMaker) {
    return new ImmutableMultimapTree<T>(childrenMaker, parentsMaker);
  }

  // TODO add javadoc & test
  public static <T> ImmutableMultimapTree<T> copyOf(final Tree<T> tree) {
    checkNotNull(tree);

    if (tree instanceof ImmutableMultimapTree) {
      final ImmutableMultimapTree<T> immutableMultimapTree = (ImmutableMultimapTree<T>) tree;

      final ImmutableSetMultimap.Builder<T, T> childrenBuilder = immutableMultimapTree.childrenMaker.get();
      final ImmutableMap.Builder<T, T> parentsBuilder = immutableMultimapTree.parentsMaker.get();

      childrenBuilder.putAll(immutableMultimapTree.children);
      parentsBuilder.putAll(immutableMultimapTree.parents);

      final ImmutableSetMultimap<T, T> children = childrenBuilder.build();
      final ImmutableMap<T, T> parents = parentsBuilder.build();

      return new ImmutableMultimapTree<T>(immutableMultimapTree.childrenMaker, immutableMultimapTree.parentsMaker, children,
          parents, immutableMultimapTree.root);
    } else {
      ImmutableMultimapTree<T> immutableMultimapTree = create();

      final T root = tree.getRoot();
      immutableMultimapTree = immutableMultimapTree.withRoot(root);

      for (final Map.Entry<T, T> entry : tree) {
        immutableMultimapTree = immutableMultimapTree.added(entry.getKey(), entry.getValue());
      }

      return immutableMultimapTree;
    }
  }

  @Override
  public T getRoot() {
    return root;
  }

  @Override
  public ImmutableMultimapTree<T> withRoot(final T node) {
    checkNotNull(node);

    final ImmutableSetMultimap<T, T> newChildren = childrenMaker.get().build();
    final ImmutableMap<T, T> newParents = parentsMaker.get().build();

    return new ImmutableMultimapTree<T>(childrenMaker, parentsMaker, newChildren, newParents, node);
  }

  @Override
  public ImmutableMultimapTree<T> added(final T parent, final T child) {
    checkNotNull(parent);
    checkNotNull(child);
    checkArgument(contains(parent), "The tree doesn't contain the specified parent node: %s", parent);

    final T childParent = parents.get(child);
    if (childParent == parent) {
      return this;
    }

    checkArgument(childParent == null, "The child node (%s) is already associated to another node", child);

    final ImmutableSetMultimap.Builder<T, T> childrenBuilder = childrenMaker.get();
    final ImmutableMap.Builder<T, T> parentsBuilder = parentsMaker.get();

    childrenBuilder.putAll(children);
    parentsBuilder.putAll(parents);

    addInternal(parent, child, childrenBuilder, parentsBuilder);

    final ImmutableSetMultimap<T, T> newChildren = childrenBuilder.build();
    final ImmutableMap<T, T> newParents = parentsBuilder.build();

    return new ImmutableMultimapTree<T>(childrenMaker, parentsMaker, newChildren, newParents, root);
  }

  /**
   * Adds a new parent/child association to the specified builders.
   *
   * @param parent the parent node
   * @param child the child node
   * @param childrenBuilder the parent-children association builder
   * @param parentsBuilder the child-parent association builder
   */
  private void addInternal(final T parent, final T child, final ImmutableSetMultimap.Builder<T, T> childrenBuilder,
                           final ImmutableMap.Builder<T, T> parentsBuilder) {
    childrenBuilder.put(parent, child);
    parentsBuilder.put(child, parent);
  }

  @Override
  public ImmutableMultimapTree<T> removed(final T node) {
    checkNotNull(node);

    if (node == root) { // optimisation
      return create(childrenMaker, parentsMaker);
    }

    if (!contains(node)) {
      return this;
    }

    final ImmutableSetMultimap.Builder<T, T> childrenBuilder = childrenMaker.get();
    final ImmutableMap.Builder<T, T> parentsBuilder = parentsMaker.get();

    addRecursivelyExcludingNode(root, node, childrenBuilder, parentsBuilder);

    final ImmutableSetMultimap<T, T> newChildren = childrenBuilder.build();
    final ImmutableMap<T, T> newParents = parentsBuilder.build();

    return new ImmutableMultimapTree<T>(childrenMaker, parentsMaker, newChildren, newParents, root);
  }

  /**
   * For each child of the specified node that <em>isn't the excluded node</em>, it adds a new association for the specified
   * node and its child in the parent-children and child-parent builders and then invokes this method again passing the child as
   * the specified node, propagating all other arguments.
   * <p/>
   * The effect of this is that, after the recursion, the builders hold all the associations of this tree, except the exclude
   * node and all of its children.
   *
   * @param node a node
   * @param excludeNode the excluded node
   * @param childrenBuilder a builder of parent-child associations
   * @param parentsBuilder a builder of the child-parent associations
   */
  private void addRecursivelyExcludingNode(final T node, final T excludeNode,
                                           final ImmutableSetMultimap.Builder<T, T> childrenBuilder,
                                           final ImmutableMap.Builder<T, T> parentsBuilder) {
    final Collection<T> nodeChildren = getChildren(node);
    for (final T child : nodeChildren) {
      if (child != excludeNode) {
        addInternal(node, child, childrenBuilder, parentsBuilder);
        addRecursivelyExcludingNode(child, excludeNode, childrenBuilder, parentsBuilder);
      }
    }
  }

  /**
   * {@inheritDoc}
   * <p/>
   * This method uses reflection to determine whether the specified object is equal to this tree.
   */
  @Override
  public boolean equals(final Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  /**
   * {@inheritDoc}
   * <p/>
   * This method uses reflection to build the returned hash code.
   */
  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
