package net.starly.core.util.collection;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class STSet<E> extends STImmutableSet<E> implements STCollection<E> {

    public STSet() { super(new HashSet<>()); }

    @SafeVarargs
    public STSet(E... elements) {
        super(Arrays.asList(elements));
    }

    public STSet(STImmutableCollection<E> baseCollection) {
        super(baseCollection);
    }

    public STSet(Collection<E> baseCollection) {
        super(new HashSet<>(baseCollection));
    }

    public STSet(List<E> baseList) {
        super(new HashSet<>(baseList));
    }

    public STSet(Set<E> baseSet) {
        super(baseSet);
    }

    @Override
    public STSet<E> filter(Predicate<E> filter) {
        return new STSet<>(stream().filter(filter).collect(Collectors.toList()));
    }

    @Override
    public <R> STSet<R> filterInstance(Class<R> clazz) {
        return new STSet<>(stream().filter(Objects::nonNull).filter(it-> $CollectionUtil.castable(it.getClass(), clazz)).map(clazz::cast).collect(Collectors.toList()));
    }

    @Override
    public <R> STSet<R> map(Function<E, R> mapper) {
        return new STSet<>(stream().map(mapper).collect(Collectors.toList()));
    }

    @Override
    public STSet<E> nonNull() {
        return new STSet<>(stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    public <R> STSet<R> mapNonNull(Function<E, R> mapper) {
        return new STSet<>(stream().map(mapper).filter(Objects::nonNull).collect(Collectors.toList()));
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
