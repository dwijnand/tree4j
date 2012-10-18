package org.dapacode.tree4j;

import org.dapacode.tree4j.common.Factory;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Ordering;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * A <strong>guaranteed</strong> {@link ImmutableTree}-compliant implementation that uses {@link ImmutableMultimap} and {@link
 * ImmutableMap}.
 *
 * @param <T> the type of the nodes in the tree
 */
public class ImmutableMultimapTree<T> implements ImmutableTree<T> {
  // TODO document this class entirely
  public static abstract class ChildrenMaker<T> implements Factory<ImmutableMultimap.Builder<T, T>> {
    private ChildrenMaker() {}

    public static <T> ChildrenMaker<T> usingListMultimap() {
      return new ChildrenMaker<T>() {
        @Override
        public ImmutableListMultimap.Builder<T, T> get() {
          return ImmutableListMultimap.builder();
        }
      };
    }

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
  public static abstract class ParentsMaker<T> implements Factory<ImmutableMap.Builder<T, T>> {
    private ParentsMaker() {}

    public static <T> ParentsMaker<T> usingImmutableMap() {
      return new ParentsMaker<T>() {
        @Override
        public ImmutableMap.Builder<T, T> get() {
          return ImmutableMap.builder();
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

    public static <T> ParentsMaker<T> usingImmutableSortedMapOrderedBy(final Comparator<T> comparator) {
      return new ParentsMaker<T>() {
        @Override
        public ImmutableSortedMap.Builder<T, T> get() {
          return new ImmutableSortedMap.Builder<T, T>(comparator);
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

  /** The parent-children associations of the tree. */
  private final ImmutableMultimap<T, T> children;

  /** The child-parent associations of the tree. */
  private final ImmutableMap<T, T> parents;

  /** The root of the tree. */
  private final T root;

  /**
   * Creates a new immutable multimap tree using the specified {@link ChildrenMaker} and {@link ParentsMaker}.
   * <p/>
   * These factories are used to obtain builders required to constructing the parent-children and child-parent associations
   * within the tree.
   *
   * @param childrenMaker an ImmutableMultimap builder factory
   * @param parentsMaker an ImmutableMap builder factory
   */
  protected ImmutableMultimapTree(final ChildrenMaker<T> childrenMaker, final ParentsMaker<T> parentsMaker) {
    this.childrenMaker = checkNotNull(childrenMaker);
    this.parentsMaker = checkNotNull(parentsMaker);
    children = childrenMaker.get().build();
    parents = parentsMaker.get().build();
    root = null;
  }

  /**
   * Creates a new multimap tree with the specified factories, associations and root node. See {@link #create(ChildrenMaker,
   * ParentsMaker)} for more details.
   * <p/>
   * This private constructor is used internally to set specific associations and root node, including setting the root node to
   * {@code null}.
   *
   * @param childrenMaker an ImmutableMultimap builder factory
   * @param parentsMaker an ImmutableMap builder factory
   * @param children the parent-children associations to be used
   * @param parents the child-parent associations to be used
   * @param root the root node
   */
  private ImmutableMultimapTree(final ChildrenMaker<T> childrenMaker, final ParentsMaker<T> parentsMaker,
                                final ImmutableMultimap<T, T> children, final ImmutableMap<T, T> parents, final T root) {
    this.childrenMaker = childrenMaker;
    this.parentsMaker = parentsMaker;
    this.children = children;
    this.parents = parents;
    this.root = root;
  }

  /**
   * Create a new immutable multimap tree backed by a {@link ListMultimap} and a {@link ImmutableMap}.
   *
   * @param <T> the type of the nodes in the tree
   * @return a new immutable multimap tree
   */
  public static <T> ImmutableMultimapTree<T> create() {
    return create(ChildrenMaker.<T>usingListMultimap(), ParentsMaker.<T>usingImmutableMap());
  }

  /**
   * Creates a new immutable multimap tree using the specified {@link ChildrenMaker} and {@link ParentsMaker}. See the {@link
   * ImmutableMultimapTree#ImmutableMultimapTree(ChildrenMaker, ParentsMaker)} constructor for more details, as it calls that
   * directly.
   *
   * @param <T> the type of the nodes in the tree
   * @param childrenMaker an ImmutableMultimap builder factory
   * @param parentsMaker an ImmutableMap builder factory
   * @return a new immutable multimap tree
   */
  public static <T> ImmutableMultimapTree<T> create(final ChildrenMaker<T> childrenMaker, final ParentsMaker<T> parentsMaker) {
    return new ImmutableMultimapTree<T>(childrenMaker, parentsMaker);
  }

  // TODO add javadoc
  public static <T> ImmutableMultimapTree<T> copyOf(final Tree<T> tree) {
    checkNotNull(tree);

    if (tree instanceof ImmutableMultimapTree) {
      final ImmutableMultimapTree<T> immutableMultimapTree = (ImmutableMultimapTree<T>) tree;

      final ImmutableMultimap.Builder<T, T> childrenBuilder = immutableMultimapTree.childrenMaker.get();
      final ImmutableMap.Builder<T, T> parentsBuilder = immutableMultimapTree.parentsMaker.get();

      childrenBuilder.putAll(immutableMultimapTree.children);
      parentsBuilder.putAll(immutableMultimapTree.parents);

      final ImmutableMultimap<T, T> children = childrenBuilder.build();
      final ImmutableMap<T, T> parents = parentsBuilder.build();

      return new ImmutableMultimapTree<T>(immutableMultimapTree.childrenMaker, immutableMultimapTree.parentsMaker, children,
          parents, immutableMultimapTree.root);
    } else {
      ImmutableMultimapTree<T> immutableMultimapTree = create();

      final T root = tree.getRoot();
      immutableMultimapTree = immutableMultimapTree.withRoot(root);

      for (final Map.Entry<T, T> entry : tree) {
        immutableMultimapTree = immutableMultimapTree.plus(entry.getKey(), entry.getValue());
      }

      // TODO check and test this!
      return immutableMultimapTree;
    }
  }

  @Override
  public final boolean contains(final T node) {
    checkNotNull(node);
    return node == root || parents.containsKey(node);
  }

  @Override
  public final T getParent(final T node) {
    checkNotNull(node);
    checkArgument(contains(node), "The tree doesn't contain the specified node: %s", node);
    return parents.get(node);
  }

  @Override
  public final ImmutableCollection<T> getChildren(final T node) {
    checkNotNull(node);
    checkArgument(contains(node), "The tree doesn't contain the specified node: %s", node);
    return children.get(node);
  }

  @Override
  public final T getRoot() {
    return root;
  }

  @Override
  public Iterator<Map.Entry<T, T>> iterator() {
    return children.entries().iterator();
  }

  @Override
  public final ImmutableMultimapTree<T> withRoot(final T node) {
    checkNotNull(node);

    final ImmutableMultimap<T, T> children = childrenMaker.get().build();
    final ImmutableMap<T, T> parents = parentsMaker.get().build();

    return new ImmutableMultimapTree<T>(childrenMaker, parentsMaker, children, parents, node);
  }

  @Override
  public final ImmutableMultimapTree<T> plus(final T parent, final T child) {
    checkNotNull(parent);
    checkNotNull(child);

    checkArgument(contains(parent), "The tree doesn't contain the specified parent node: %s", parent);

    final T childParent = parents.get(child);
    // TODO test both cases
    checkArgument(childParent == null || childParent == parent, "The child node (%s) is already associated to another node",
        child);

    if (contains(child)) {
      return this;
    }

    final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenMaker.get();
    final ImmutableMap.Builder<T, T> parentsBuilder = parentsMaker.get();

    childrenBuilder.putAll(children);
    parentsBuilder.putAll(parents);

    addInternal(parent, child, childrenBuilder, parentsBuilder);

    final ImmutableMultimap<T, T> children = childrenBuilder.build();
    final ImmutableMap<T, T> parents = parentsBuilder.build();

    return new ImmutableMultimapTree<T>(childrenMaker, parentsMaker, children, parents, root);
  }

  /**
   * Adds a new parent/child association to the specified builders.
   *
   * @param parent the parent node
   * @param child the child node
   * @param childrenBuilder the parent-children association builder
   * @param parentsBuilder the child-parent association builder
   */
  private void addInternal(final T parent, final T child, final ImmutableMultimap.Builder<T, T> childrenBuilder,
                           final ImmutableMap.Builder<T, T> parentsBuilder) {
    childrenBuilder.put(parent, child);
    parentsBuilder.put(child, parent);
  }

  @Override
  public final ImmutableMultimapTree<T> minus(final T node) {
    checkNotNull(node);

    if (node == root) {
      // optimisation
      return create(childrenMaker, parentsMaker);
    }

    if (!contains(node)) {
      return this;
    }

    final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenMaker.get();
    final ImmutableMap.Builder<T, T> parentsBuilder = parentsMaker.get();

    addRecursivelyExcludingNode(root, node, childrenBuilder, parentsBuilder);

    final ImmutableMultimap<T, T> children = childrenBuilder.build();
    final ImmutableMap<T, T> parents = parentsBuilder.build();

    return new ImmutableMultimapTree<T>(childrenMaker, parentsMaker, children, parents, root);
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
                                           final ImmutableMultimap.Builder<T, T> childrenBuilder,
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
  // TODO decide if equals and hashCode should be final
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
