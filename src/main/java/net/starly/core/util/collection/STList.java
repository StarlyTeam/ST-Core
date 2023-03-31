package net.starly.core.util.collection;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class STList<E> extends STImmutableList<E> implements STCollection<E> {

    public STList() { super(new ArrayList<>()); }

    @SafeVarargs
    public STList(E... elements) {
        super(Arrays.asList(elements));
    }

    public STList(STImmutableCollection<E> baseCollection) {
        super(baseCollection);
    }

    public STList(Collection<E> baseCollection) {
        super(new ArrayList<>(baseCollection));
    }

    public STList(List<E> baseList) {
        super(baseList);
    }

    public STList(Set<E> baseSet) {
        super(new ArrayList<>(baseSet));
    }

    public int indexOf(E element) {
        return ((List<E>)baseCollection).indexOf(element);
    }

    public E getAt(int index) {
        return ((List<E>)baseCollection).get(index);
    }

    public STList<E> sort() {
        return new STList<>(baseCollection.stream().sorted().collect(Collectors.toList()));
    }

    public STList<E> sort(Comparator<E> comparator) {
        return new STList<>(baseCollection.stream().sorted(comparator).collect(Collectors.toList()));
    }

    @Override
    public STList<E> filter(Predicate<E> filter) {
        return new STList<>(stream().filter(filter).collect(Collectors.toList()));
    }

    @Override
    public <R> STList<R> filterInstance(Class<R> clazz) {
        return new STList<>(stream().filter(Objects::nonNull).filter(it-> $CollectionUtil.castable(it.getClass(), clazz)).map(clazz::cast).collect(Collectors.toList()));
    }

    @Override
    public <R> STList<R> map(Function<E, R> mapper) {
        return new STList<>(stream().map(mapper).collect(Collectors.toList()));
    }

    @Override
    public STList<E> nonNull() {
        return new STList<>(stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    public <R> STList<R> mapNonNull(Function<E, R> mapper) {
        return new STList<>(stream().map(mapper).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public boolean add(E element) {
        return baseCollection.add(element);
    }

    public boolean addAll(STImmutableCollection<E> otherCollection) {
        return baseCollection.addAll(otherCollection.toList());
    }

    public boolean addAll(Collection<E> otherCollection) {
        return baseCollection.addAll(otherCollection);
    }

    public boolean remove(E element) {
        return baseCollection.remove(element);
    }

    public void clear() {
        baseCollection.clear();
    }

    @Override
    public String toString() {
        return baseCollection.toString();
    }

}
