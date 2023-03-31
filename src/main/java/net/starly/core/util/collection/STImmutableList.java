package net.starly.core.util.collection;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class STImmutableList<E> extends STImmutableCollection<E> {

    @SafeVarargs
    public STImmutableList(E... elements) {
        super(Arrays.asList(elements));
    }

    public STImmutableList(STImmutableCollection<E> baseCollection) {
        super(baseCollection);
    }

    public STImmutableList(Collection<E> baseCollection) {
        super(new ArrayList<>(baseCollection));
    }

    public STImmutableList(List<E> baseList) {
        super(baseList);
    }

    public STImmutableList(Set<E> baseSet) {
        super(new ArrayList<>(baseSet));
    }

    public int indexOf(E element) {
        return ((List<E>)baseCollection).indexOf(element);
    }

    public E getAt(int index) {
        return ((List<E>)baseCollection).get(index);
    }

    @Override
    public STImmutableList<E> filter(Predicate<E> filter) {
        return new STImmutableList<>(stream().filter(filter).collect(Collectors.toList()));
    }

    @Override
    public <R> STImmutableList<R> filterInstance(Class<R> clazz) {
        return new STImmutableList<>(stream().filter(Objects::nonNull).filter(it-> $CollectionUtil.castable(it.getClass(), clazz)).map(clazz::cast).collect(Collectors.toList()));
    }

    @Override
    public <R> STImmutableList<R> map(Function<E, R> mapper) {
        return new STImmutableList<>(stream().map(mapper).collect(Collectors.toList()));
    }

    public STImmutableList<E> sort() {
        return new STImmutableList<>(baseCollection.stream().sorted().collect(Collectors.toList()));
    }

    public STImmutableList<E> sort(Comparator<E> comparator) {
        return new STImmutableList<>(baseCollection.stream().sorted(comparator).collect(Collectors.toList()));
    }

    @Override
    public STImmutableList<E> nonNull() {
        return new STImmutableList<>(stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    public <R> STImmutableList<R> mapNonNull(Function<E, R> mapper) {
        return new STImmutableList<>(stream().map(mapper).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return baseCollection.toString();
    }

}
