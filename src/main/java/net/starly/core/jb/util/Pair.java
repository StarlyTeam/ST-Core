package net.starly.core.jb.util;

public class Pair<F,S> {

    private final F first;
    private final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() { return first; }
    public S getSecond() { return second; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair<?, ?>) {
            Pair<?,?> other = (Pair<?,?>) obj;
            return first.equals(other.first) && second.equals(other.second);
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (first == null? 0 : first.hashCode());
        result = prime * result + (second == null ? 0 : second.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Pair " + Integer.toHexString(hashCode()) + ": (" + first.toString() + ", " + second.toString() + ")";
    }

}