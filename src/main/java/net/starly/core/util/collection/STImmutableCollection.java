package net.starly.core.util.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class STImmutableCollection<E> implements Iterable<E> {

    protected final Collection<E> baseCollection;

    public STImmutableCollection(STImmutableCollection<E> baseStCollection) {
        this.baseCollection = baseStCollection.toList();
    }

    public STImmutableCollection(Collection<E> baseCollection) {
        this.baseCollection = baseCollection;
    }

    public STImmutableCollection(List<E> baseList) {
        this.baseCollection = baseList;
    }

    public STImmutableCollection(Set<E> baseSet) {
        this.baseCollection = baseSet;
    }

    protected Stream<E> stream() {
        return baseCollection.stream();
    }

    public E first() {
        Optional<E> optional = stream().findFirst();
        return optional.orElse(null);
    }

    @Nullable public E firstOrNull() {
        return stream().findFirst().orElse(null);
    }

    @NotNull public E firstOrElse(@NotNull E elseValue) {
        return stream().findFirst().orElse(elseValue);
    }

    @Nullable public E firstOrNull(Predicate<E> filter) {
        return stream().filter(filter).findFirst().orElse(null);
    }

    @NotNull public E firstOrElse(Predicate<E> filter, @NotNull E elseValue) {
        return stream().filter(filter).findFirst().orElse(elseValue);
    }

    public void forEach(Consumer<? super E> block) {
        stream().forEach(block);
    }

    public void forEachIndexed(BiConsumer<Integer, E> block) {
        AtomicInteger value = new AtomicInteger();
        stream().forEach(it -> block.accept(value.getAndIncrement(), it));
    }

    public boolean any(Predicate<E> filter) { return stream().anyMatch(filter); }

    public boolean none(Predicate<E> filter) { return stream().noneMatch(filter); }

    public STImmutableCollection<E> filter(Predicate<E> filter) {
        return new STImmutableCollection<>(stream().filter(filter).collect(Collectors.toList()));
    }

    public <R> STImmutableCollection<R> filterInstance(Class<R> clazz) {
        return new STImmutableCollection<>(stream().filter(Objects::nonNull).filter(it-> $CollectionUtil.castable(it.getClass(), clazz)).map(clazz::cast).collect(Collectors.toList()));
    }

    public <R> STImmutableCollection<R> map(Function<E, R> mapper) {
        return new STImmutableCollection<>(stream().map(mapper).collect(Collectors.toList()));
    }

    public STImmutableCollection<E> nonNull() {
        return new STImmutableCollection<>(stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public <R> STImmutableCollection<R> mapNonNull(Function<E, R> mapper) {
        return new STImmutableCollection<>(stream().map(mapper).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public List<E> toList() {
        return new ArrayList<>(baseCollection);
    }

    public Set<E> toSet() {
        return new HashSet<>(baseCollection);
    }

    public boolean contains(E element) {
        return baseCollection.contains(element);
    }

    public boolean isEmpty() {
        return baseCollection.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public int getSize() {
        return baseCollection.size();
    }

    @Override
    public String toString() {
        return baseCollection.toString();
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return baseCollection.iterator();
    }

    @Override
    public Spliterator<E> spliterator() {
        return baseCollection.spliterator();
    }

}
