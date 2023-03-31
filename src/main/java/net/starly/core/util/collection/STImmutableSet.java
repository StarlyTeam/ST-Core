package net.starly.core.util.collection;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class STImmutableSet<E> extends STImmutableCollection<E> {

    @SafeVarargs
    public STImmutableSet(E... elements) { super(new HashSet<>(Arrays.asList(elements))); }

    public STImmutableSet(STImmutableCollection<E> baseCollection) {
        super(baseCollection);
    }

    public STImmutableSet(Collection<E> baseCollection) {
        super(new HashSet<>(baseCollection));
    }

    public STImmutableSet(List<E> baseList) {
        super(new HashSet<>(baseList));
    }

    public STImmutableSet(Set<E> baseSet) {
        super(baseSet);
    }

    @Override
    public STImmutableSet<E> filter(Predicate<E> filter) {
        return new STImmutableSet<>(stream().filter(filter).collect(Collectors.toList()));
    }

    @Override
    public <R> STImmutableSet<R> filterInstance(Class<R> clazz) {
        return new STImmutableSet<>(stream().filter(Objects::nonNull).filter(it-> $CollectionUtil.castable(it.getClass(), clazz)).map(clazz::cast).collect(Collectors.toList()));
    }

    @Override
    public <R> STImmutableSet<R> map(Function<E, R> mapper) {
        return new STImmutableSet<>(stream().map(mapper).collect(Collectors.toList()));
    }

    @Override
    public STImmutableSet<E> nonNull() {
        return new STImmutableSet<>(stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    public <R> STImmutableSet<R> mapNonNull(Function<E, R> mapper) {
        return new STImmutableSet<>(stream().map(mapper).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return baseCollection.toString();
    }

}
