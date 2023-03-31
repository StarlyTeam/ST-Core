package net.starly.core.data.util;

public class Tuple<A, B> {
    private A a;
    private B b;

    public Tuple(final A a, final B b) {
        this.a = a;
        this.b = b;
    }

    public static <A, B> Tuple<A, B> of(final A a, final B b) {
        return new Tuple<>(a, b);
    }

    public A getFirst() {
        return this.a;
    }

    public B getSecond() {
        return this.b;
    }

    public void setFirst(final A a) {
        this.a = a;
    }

    public void setSecond(final B b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "Tuple {" + a + ", " + b + "}";
    }
}
