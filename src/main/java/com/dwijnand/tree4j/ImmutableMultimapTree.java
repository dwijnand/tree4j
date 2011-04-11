package com.dwijnand.tree4j;

import com.dwijnand.tree4j.common.Factory;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A <strong>guaranteed</strong> {@link ImmutableTree}-compliant implementation
 * that uses {@link ImmutableMultimap} and {@link ImmutableMap}.
 *
 * @param <T> the type of the nodes in the tree
 */
public class ImmutableMultimapTree<T> implements ImmutableTree<T> {

    // TODO add javadoc
    public static abstract class ChildrenBuilderFactory<T>
            implements Factory<ImmutableMultimap.Builder<T, T>> {

        private ChildrenBuilderFactory() {
        }

        // TODO add javadoc
        public static <T> ChildrenBuilderFactory<T> usingListMultimap() {
            return new ChildrenBuilderFactory<T>() {
                @Override
                public ImmutableListMultimap.Builder<T, T> get() {
                    return ImmutableListMultimap.builder();
                }
            };
        }

        // TODO add javadoc
        public static <T> ChildrenBuilderFactory<T> usingSetMultimap() {
            return new ChildrenBuilderFactory<T>() {
                @Override
                public ImmutableSetMultimap.Builder<T, T> get() {
                    return ImmutableSetMultimap.builder();
                }
            };
        }
    }

    // TODO add javadoc
    public static abstract class ParentsBuilderFactory<T>
            implements Factory<ImmutableMap.Builder<T, T>> {

        private ParentsBuilderFactory() {
        }

        // TODO add javadoc
        public static <T> ParentsBuilderFactory<T> usingImmutableMap() {
            return new ParentsBuilderFactory<T>() {
                @Override
                public ImmutableMap.Builder<T, T> get() {
                    return ImmutableMap.builder();
                }
            };
        }

        // TODO add javadoc
        public static <T extends Comparable<T>> ParentsBuilderFactory<T>
        usingImmutableSortedMapInNaturalOrder() {
            return new ParentsBuilderFactory<T>() {

                @Override
                public ImmutableSortedMap.Builder<T, T> get() {
                    return new ImmutableSortedMap.Builder<T, T>(
                            Ordering.natural());
                }

            };
        }

        // TODO add javadoc
        public static <T> ParentsBuilderFactory<T>
        usingImmutableSortedMapOrderedBy(
                final Comparator<T> comparator) {
            return new ParentsBuilderFactory<T>() {

                @Override
                public ImmutableSortedMap.Builder<T, T> get() {
                    return new ImmutableSortedMap.Builder<T, T>(comparator);
                }

            };
        }

        // TODO add javadoc
        public static <T extends Comparable<T>> ParentsBuilderFactory<T>
        usingImmutableSortedMapInReverseOrder() {
            return new ParentsBuilderFactory<T>() {

                @Override
                public ImmutableSortedMap.Builder<T, T> get() {
                    return new ImmutableSortedMap.Builder<T, T>(Ordering
                            .natural().reverse());
                }

            };
        }

    }

    /**
     * The factory of immutable multimap builder instances, used to build the
     * parent-children associations of a new immutable multimap tree.
     */
    private final ChildrenBuilderFactory<T> childrenBuilderFactory;

    /**
     * The factory of immutable map builder instances, used to build the
     * child-parent associations of a new immutable multimap tree.
     */
    private final ParentsBuilderFactory<T> parentsBuilderFactory;

    /**
     * The parent-children associations of the tree.
     */
    private final ImmutableMultimap<T, T> children;

    /**
     * The child-parent associations of the tree.
     */
    private final ImmutableMap<T, T> parents;

    /**
     * The root of the tree.
     */
    private final T root;

    /**
     * Creates a new immutable multimap tree using the specified
     * {@link ChildrenBuilderFactory} and {@link ParentsBuilderFactory}.
     * <p/>
     * These factories are used to obtain builders required to constructing the
     * parent-children and child-parent associations within the tree.
     *
     * @param childrenBuilderFactory an ImmutableMultimap builder factory
     * @param parentsBuilderFactory  an ImmutableMap builder factory
     */
    protected ImmutableMultimapTree(
            final ChildrenBuilderFactory<T> childrenBuilderFactory,
            final ParentsBuilderFactory<T> parentsBuilderFactory) {
        this.childrenBuilderFactory = checkNotNull(childrenBuilderFactory);
        this.parentsBuilderFactory = checkNotNull(parentsBuilderFactory);
        children = childrenBuilderFactory.get().build();
        parents = parentsBuilderFactory.get().build();
        root = null;
    }

    /**
     * Creates a new multimap tree with the specified factories, associations
     * and root node. See {@link #create(ChildrenBuilderFactory,
     * ParentsBuilderFactory)} for more details.
     * <p/>
     * This private constructor is used internally to set specific associations
     * and root node, including setting the root node to {@code null}.
     *
     * @param childrenBuilderFactory an ImmutableMultimap builder factory
     * @param parentsBuilderFactory  an ImmutableMap builder factory
     * @param children               the parent-children associations to be used
     * @param parents                the child-parent associations to be used
     * @param root                   the root node
     */
    private ImmutableMultimapTree(
            final ChildrenBuilderFactory<T> childrenBuilderFactory,
            final ParentsBuilderFactory<T> parentsBuilderFactory,
            final ImmutableMultimap<T, T> children,
            final ImmutableMap<T, T> parents, final T root) {
        this.childrenBuilderFactory = childrenBuilderFactory;
        this.parentsBuilderFactory = parentsBuilderFactory;
        this.children = children;
        this.parents = parents;
        this.root = root;
    }

    /**
     * Create a new immutable multimap tree backed by a {@link ListMultimap} and
     * a {@link ImmutableMap}.
     *
     * @param <T> the type of the nodes in the tree
     * @return a new immutable multimap tree
     */
    public static <T> ImmutableMultimapTree<T> create() {
        return create(ChildrenBuilderFactory.<T>usingListMultimap(),
                ParentsBuilderFactory.<T>usingImmutableMap());
    }

    /**
     * Creates a new immutable multimap tree using the specified
     * {@link ChildrenBuilderFactory} and {@link ParentsBuilderFactory}.
     * See the {@link
     * ImmutableMultimapTree#ImmutableMultimapTree(ChildrenBuilderFactory, ParentsBuilderFactory)}
     * constructor for more details, as it calls that directly.
     *
     * @param <T>                    the type of the nodes in the tree
     * @param childrenBuilderFactory an ImmutableMultimap builder factory
     * @param parentsBuilderFactory  an ImmutableMap builder factory
     * @return a new immutable multimap tree
     */
    public static <T> ImmutableMultimapTree<T> create(
            final ChildrenBuilderFactory<T> childrenBuilderFactory,
            final ParentsBuilderFactory<T> parentsBuilderFactory) {
        return new ImmutableMultimapTree<T>(childrenBuilderFactory,
                parentsBuilderFactory);
    }

    @Override
    public final boolean contains(final T node) {
        return node != null && (node == root || parents.containsKey(node));
    }

    @Override
    public final T getParent(final T node) {
        return parents.get(checkNotNull(node));
    }

    @Override
    public final ImmutableCollection<T> getChildren(final T node) {
        return children.get(checkNotNull(node));
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

        final ImmutableMultimap<T, T> children =
                childrenBuilderFactory.get().build();

        final ImmutableMap<T, T> parents =
                parentsBuilderFactory.get().build();

        return new ImmutableMultimapTree<T>(childrenBuilderFactory,
                parentsBuilderFactory, children, parents, node);
    }

    @Override
    public final ImmutableMultimapTree<T> add(final T parent, final T child) {
        checkNotNull(parent);
        checkNotNull(child);

        checkArgument(contains(parent),
                "%s does not contain parent node %s", getClass()
                .getSimpleName(), parent);

        if (contains(child)) {
            return this;
        }

        final ImmutableMultimap.Builder<T, T> childrenBuilder =
                childrenBuilderFactory.get();

        final ImmutableMap.Builder<T, T> parentsBuilder =
                parentsBuilderFactory.get();

        childrenBuilder.putAll(children);
        parentsBuilder.putAll(parents);

        addInternal(parent, child, childrenBuilder, parentsBuilder);

        final ImmutableMultimap<T, T> children = childrenBuilder.build();
        final ImmutableMap<T, T> parents = parentsBuilder.build();

        return new ImmutableMultimapTree<T>(childrenBuilderFactory,
                parentsBuilderFactory, children, parents, root);
    }

    /**
     * Adds a new parent/child association to the specified builders.
     *
     * @param parent          the parent node
     * @param child           the child node
     * @param childrenBuilder the parent-children association builder
     * @param parentsBuilder  the child-parent association builder
     */
    private void addInternal(final T parent, final T child,
                             final ImmutableMultimap.Builder<T, T> childrenBuilder,
                             final ImmutableMap.Builder<T, T> parentsBuilder) {
        childrenBuilder.put(parent, child);
        parentsBuilder.put(child, parent);
    }

    @Override
    public final ImmutableMultimapTree<T> remove(final T node) {
        checkNotNull(node);

        if (node == root) {
            // optimisation
            return create(childrenBuilderFactory, parentsBuilderFactory);
        }

        if (!contains(node)) {
            return this;
        }

        final ImmutableMultimap.Builder<T, T> childrenBuilder =
                childrenBuilderFactory.get();
        final ImmutableMap.Builder<T, T> parentsBuilder =
                parentsBuilderFactory.get();

        addRecursivelyExcludingNode(root, node, childrenBuilder,
                parentsBuilder);

        final ImmutableMultimap<T, T> children = childrenBuilder.build();
        final ImmutableMap<T, T> parents = parentsBuilder.build();

        return new ImmutableMultimapTree<T>(childrenBuilderFactory,
                parentsBuilderFactory, children, parents, root);
    }

    /**
     * For each child of the specified node that
     * <em>isn't the excluded node</em>, it adds a new association for the
     * specified node and its child in the parent-children and child-parent
     * builders and then invokes this method again passing the child as the
     * specified node, propagating all other arguments.
     * <p/>
     * The effect of this is that, after the recursion, the builders hold all
     * the associations of this tree, except the exclude node and all of its
     * children.
     *
     * @param node            a node
     * @param excludeNode     the excluded node
     * @param childrenBuilder a builder of parent-child associations
     * @param parentsBuilder  a builder of the child-parent associations
     */
    private void addRecursivelyExcludingNode(final T node, final T excludeNode,
                                             final ImmutableMultimap.Builder<T, T>
                                                     childrenBuilder,
                                             final ImmutableMap.Builder<T, T>
                                                     parentsBuilder) {
        final Collection<T> nodeChildren = getChildren(node);
        for (final T child : nodeChildren) {
            if (child != excludeNode) {
                addInternal(node, child, childrenBuilder, parentsBuilder);
                addRecursivelyExcludingNode(child, excludeNode,
                        childrenBuilder, parentsBuilder);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This method uses reflection to determine whether the specified object is
     * equal to this tree.
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
