package net.starly.core.data.util;

@SuppressWarnings("unused")
public class Tuple<A, B> {
    public A a;

    public B b;

    public Tuple(final A a, final B b) {
        this.a = a;
        this.b = b;
    }

    public static <A, B> Tuple<A, B> of(final A a, final B b) {
        return new Tuple<>(a, b);
    }

    public A getA() {
        return this.a;
    }

    public B getB() {
        return this.b;
    }

    public void setA(final A a) {
        this.a = a;
    }

    public void setB(final B b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "[" + this.a + ", " + this.b + "]";
    }
}
