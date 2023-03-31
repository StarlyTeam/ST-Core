package net.starly.core.data.util;

public class Triple<A, B, C> {
    private A a;
    private B b;
    private C c;

    public Triple(final A a, final B b, final C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public static <A, B, C> Triple<A, B, C> of(final A a, final B b, final C c) {
        return new Triple<>(a, b, c);
    }

    public A getFirst() {
        return this.a;
    }

    public B getSecond() {
        return this.b;
    }

    public C getThird() {
        return this.c;
    }

    public void setFirst(final A a) {
        this.a = a;
    }

    public void setSecond(final B b) {
        this.b = b;
    }

    public void setThird(final C c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Triple{" + a + ", " + b + ", " + c + "}";
    }
}
