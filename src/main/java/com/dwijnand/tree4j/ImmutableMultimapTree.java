package com.dwijnand.tree4j;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Comparator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwijnand.tree4j.common.Factory;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;

/**
 * A <b>guaranteed</b> {@link ImmutableTree}-compliant implementation that uses
 * {@link ImmutableMultimap} and {@link ImmutableMap}.
 *
 * @param <T> the type of the nodes in the tree
 */
public class ImmutableMultimapTree<T> implements ImmutableTree<T> {

    // TODO add javadoc
    public static abstract class ChildrenBuilderFactory<B extends ImmutableMultimap.Builder<T, T>, T>
            implements Factory<B> {

        private ChildrenBuilderFactory() {
        }

        // TODO add javadoc
        public static <T> ChildrenBuilderFactory<ImmutableMultimap.Builder<T, T>, T> newImmutableMultimapsBuildersFactory() {
            return new ChildrenBuilderFactory<ImmutableMultimap.Builder<T, T>, T>() {

                @Override
                public ImmutableMultimap.Builder<T, T> get() {
                    return ImmutableMultimap.builder();
                }
            };
        }

        // TODO add javadoc
        public static <T> ChildrenBuilderFactory<ImmutableListMultimap.Builder<T, T>, T> newImmutableListMultimapsBuildersFactory() {
            return new ChildrenBuilderFactory<ImmutableListMultimap.Builder<T, T>, T>() {

                @Override
                public ImmutableListMultimap.Builder<T, T> get() {
                    return ImmutableListMultimap.builder();
                }
            };
        }

        // TODO add javadoc
        public static <T> ChildrenBuilderFactory<ImmutableSetMultimap.Builder<T, T>, T> newImmutableSetMultimapsBuildersFactory() {
            return new ChildrenBuilderFactory<ImmutableSetMultimap.Builder<T, T>, T>() {

                @Override
                public ImmutableSetMultimap.Builder<T, T> get() {
                    return ImmutableSetMultimap.builder();
                }
            };
        }

    }

    // TODO add javadoc
    public static abstract class ParentsBuildersFactory<B extends ImmutableMap.Builder<T, T>, T>
            implements Factory<B> {

        private ParentsBuildersFactory() {
        }

        // TODO add javadoc
        public static <T> ParentsBuildersFactory<ImmutableMap.Builder<T, T>, T> newImmutableMapsBuildersFactory() {
            return new ParentsBuildersFactory<ImmutableMap.Builder<T, T>, T>() {

                @Override
                public ImmutableMap.Builder<T, T> get() {
                    return ImmutableMap.builder();
                }
            };
        }

        // TODO add javadoc
        public static <T> ParentsBuildersFactory<ImmutableBiMap.Builder<T, T>, T> newImmutableBiMapsBuildersFactory() {
            return new ParentsBuildersFactory<ImmutableBiMap.Builder<T, T>, T>() {

                @Override
                public ImmutableBiMap.Builder<T, T> get() {
                    return ImmutableBiMap.builder();
                }

            };
        }

        // TODO add javadoc
        public static <T extends Comparable<T>> ParentsBuildersFactory<ImmutableSortedMap.Builder<T, T>, T> newImmutableSortedMapsBuildersFactoryNaturalOrder() {
            return new ParentsBuildersFactory<ImmutableSortedMap.Builder<T, T>, T>() {

                @Override
                public ImmutableSortedMap.Builder<T, T> get() {
                    return new ImmutableSortedMap.Builder<T, T>(
                            Ordering.natural());
                }

            };
        }

        // TODO add javadoc
        public static <T> ParentsBuildersFactory<ImmutableSortedMap.Builder<T, T>, T> newImmutableSortedMapsBuildersFactoryOrderedBy(
                final Comparator<T> comparator) {
            return new ParentsBuildersFactory<ImmutableSortedMap.Builder<T, T>, T>() {

                @Override
                public ImmutableSortedMap.Builder<T, T> get() {
                    return new ImmutableSortedMap.Builder<T, T>(comparator);
                }

            };
        }

        // TODO add javadoc
        public static <T extends Comparable<T>> ParentsBuildersFactory<ImmutableSortedMap.Builder<T, T>, T> newImmutableSortedMapsBuildersFactoryReverseOrder() {
            return new ParentsBuildersFactory<ImmutableSortedMap.Builder<T, T>, T>() {

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
     * parent-children relationships of a new multimap tree.
     */
    private final ChildrenBuilderFactory<? extends ImmutableMultimap.Builder<T, T>, T> childrenBuilderFactory;

    /**
     * The factory of immutable map builder instances, used to build the
     * child-parent relationships of a new multimap tree.
     */
    private final ParentsBuildersFactory<? extends ImmutableMap.Builder<T, T>, T> parentsBuilderFactory;

    /**
     * The parent-children relationships of the tree.
     */
    private final ImmutableMultimap<T, T> children;

    /**
     * The child-parent relationships of the tree.
     */
    private final ImmutableMap<T, T> parents;

    /**
     * The root of the tree.
     */
    private final T root;

    /**
     * Creates a new immutable multimap tree using the specified
     * {@link ImmutableMultimap} and {@link ImmutableMap} builder factories.
     * These factories are used to obtain builders required to constructing the
     * parent-children and child-parent relationships within the tree.
     * <p>
     * Factories of builders are required, as opposed to simply builders,
     * because all the 'modifying' methods actually return new instances of this
     * tree, and therefore require new builders.
     *
     * @param <T> the type of the nodes in the tree
     * @param childrenBuilderFactory a children builder factory
     * @param parentsBuilderFactory a parents builder factory
     * @return a new immutable multimap tree
     */
    public static <T> ImmutableMultimapTree<T> create(
            final ChildrenBuilderFactory<? extends ImmutableMultimap.Builder<T, T>, T> childrenBuilderFactory,
            final ParentsBuildersFactory<? extends ImmutableMap.Builder<T, T>, T> parentsBuilderFactory) {
        return new ImmutableMultimapTree<T>(childrenBuilderFactory,
                parentsBuilderFactory);
    }

    /**
     * Creates a new immutable multimap tree. This constructor is used directly
     * by {@link #create(ChildrenBuilderFactory, ParentsBuildersFactory)} so
     * look there for details.
     * <p>
     * This constructor is protected so that the class can be subclassed. To
     * instantiate this class, please use the create(ChildrenBuilderFactory,
     * ParentsBuildersFactory) static method.
     *
     * @param childrenMultimapFactory a Multimap factory
     * @param parentsMapFactory a Map factory
     *
     * @param childrenBuilderFactory a children builder factory
     * @param parentsBuilderFactory a parents builder factory
     */
    protected ImmutableMultimapTree(
            final ChildrenBuilderFactory<? extends ImmutableMultimap.Builder<T, T>, T> childrenBuilderFactory,
            final ParentsBuildersFactory<? extends ImmutableMap.Builder<T, T>, T> parentsBuilderFactory) {
        this.childrenBuilderFactory = checkNotNull(childrenBuilderFactory);
        this.parentsBuilderFactory = checkNotNull(parentsBuilderFactory);

        final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenBuilderFactory
                .get();
        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderFactory
                .get();

        children = childrenBuilder.build();
        parents = parentsBuilder.build();
        root = null;
    }

    /**
     * Creates a new multimap tree with the specified factories, associations
     * and root node. See {@link #create(Factory, Factory)} for more details.
     * <p>
     * This private constructor is used internally to set specific associations
     * and root node, including setting the root node to <code>null</code>.
     *
     * @param childrenBuilderFactory an ImmutableMultimap builder factory
     * @param parentsBuilderFactory an ImmutableMap builder factory
     * @param children the parent-children associations to be used
     * @param parents the child-parent associations to be used
     * @param root the root node
     */
    private ImmutableMultimapTree(
            final ChildrenBuilderFactory<? extends ImmutableMultimap.Builder<T, T>, T> childrenBuilderFactory,
            final ParentsBuildersFactory<? extends ImmutableMap.Builder<T, T>, T> parentsBuilderFactory,
            final ImmutableMultimap<T, T> children,
            final ImmutableMap<T, T> parents, final T root) {
        this.childrenBuilderFactory = childrenBuilderFactory;
        this.parentsBuilderFactory = parentsBuilderFactory;
        this.children = children;
        this.parents = parents;
        this.root = root;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.TreeSpec#contains(java.lang.Object)
     */
    @Override
    public final boolean contains(final T node) {
        if (node == null) {
            return false;
        } else if (node == root) {
            return true;
        } else {
            return parents.containsKey(node);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.TreeSpec#getParent(java.lang.Object)
     */
    @Override
    public final T getParent(final T node) {
        return parents.get(checkNotNull(node));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.TreeSpec#getChildren(java.lang.Object)
     */
    @Override
    public final ImmutableCollection<T> getChildren(final T node) {
        return children.get(checkNotNull(node));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.TreeSpec#getRoot()
     */
    @Override
    public final T getRoot() {
        return root;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.ImmutableTree#withRoot(java.lang.Object)
     */
    @Override
    public final ImmutableMultimapTree<T> withRoot(final T node) {
        checkNotNull(node);

        final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenBuilderFactory
                .get();
        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderFactory
                .get();

        final ImmutableMultimap<T, T> children = childrenBuilder.build();
        final ImmutableMap<T, T> parents = parentsBuilder.build();

        return new ImmutableMultimapTree<T>(childrenBuilderFactory,
                parentsBuilderFactory, children, parents, node);
    };

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.ImmutableTree#add(java.lang.Object,
     * java.lang.Object)
     */
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

        final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenBuilderFactory
                .get();
        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderFactory
                .get();

        childrenBuilder.putAll(children);
        parentsBuilder.putAll(parents);

        addInternal(parent, child, childrenBuilder, parentsBuilder);

        final ImmutableMultimap<T, T> children = childrenBuilder.build();
        final ImmutableMap<T, T> parents = parentsBuilder.build();

        return new ImmutableMultimapTree<T>(childrenBuilderFactory,
                parentsBuilderFactory, children, parents, root);
    };

    /**
     * Adds a new parent/child association to the specified builders.
     *
     * @param parent the parent node
     * @param child the child node
     * @param childrenBuilder the parent-children association builder
     * @param parentsBuilder the child-parent association builder
     */
    private final void addInternal(final T parent, final T child,
            final ImmutableMultimap.Builder<T, T> childrenBuilder,
            final ImmutableMap.Builder<T, T> parentsBuilder) {
        childrenBuilder.put(parent, child);
        parentsBuilder.put(child, parent);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dwijnand.tree.ImmutableTree#remove(java.lang.Object)
     */
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

        final ImmutableMultimap.Builder<T, T> childrenBuilder = childrenBuilderFactory
                .get();
        final ImmutableMap.Builder<T, T> parentsBuilder = parentsBuilderFactory
                .get();

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
     * <p>
     * The effect of this is that, after the recursion, the builders hold all
     * the associations of this tree, except the exclude node and all of its
     * children.
     *
     * @param node a node
     * @param excludeNode the excluded node
     * @param childrenBuilder a builder of parent-child associations
     * @param parentsBuilder a builder of the child-parent associations
     */
    private final void addRecursivelyExcludingNode(final T node,
            final T excludeNode,
            final ImmutableMultimap.Builder<T, T> childrenBuilder,
            final ImmutableMap.Builder<T, T> parentsBuilder) {
        final ImmutableCollection<T> nodeChildren = getChildren(node);
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
     * <p>
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
     * <p>
     * This method uses reflection to build the returned hash code.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
