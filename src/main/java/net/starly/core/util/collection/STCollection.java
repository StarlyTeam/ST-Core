package net.starly.core.util.collection;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public interface STCollection<E> extends Iterable<E> {

    boolean add(E element);

    boolean addAll(STImmutableCollection<E> otherCollection);

    boolean addAll(Collection<E> otherCollection);

    boolean remove(E element);

    void clear();

    STCollection<E> filter(Predicate<E> filter);

    <R> STCollection<R> filterInstance(Class<R> clazz);

    <R> STCollection<R> map(Function<E, R> mapper);

    STCollection<E> nonNull();

    E first();

    boolean contains(E element);

    <R> STCollection<R> mapNonNull(Function<E, R> mapper);

    List<E> toList();

    Set<E> toSet();

}
