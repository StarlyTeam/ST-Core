package net.starly.core.data.util;

@SuppressWarnings("unused")
public class Triple<A, B, C> {
    public A a;

    public B b;

    public C c;

    public Triple(final A a, final B b, final C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public static <A, B, C> Triple<A, B, C> of(final A a, final B b, final C c) {
        return new Triple<>(a, b, c);
    }

    public A getA() {
        return this.a;
    }

    public B getB() {
        return this.b;
    }

    public C getC() {
        return this.c;
    }

    public void setA(final A a) {
        this.a = a;
    }

    public void setB(final B b) {
        this.b = b;
    }

    public void setC(final C c) {
        this.c = c;
    }

    public String toString() {
        return "[" + this.a + ", " + this.b + ", " + this.c + "]";
    }
}
