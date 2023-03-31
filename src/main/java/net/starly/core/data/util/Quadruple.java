package net.starly.core.data.util;

public class Quadruple<A, B, C, D> {
    private A a;
    private B b;
    private C c;
    private D d;

    public Quadruple(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public static <A, B, C, D> Quadruple<A, B, C, D> of(A a, B b, C c, D d) {
        return new Quadruple<>(a, b, c, d);
    }

    public A getFirst() {
        return a;
    }

    public B getSecond() {
        return b;
    }

    public C getThird() {
        return c;
    }

    public D getFourth() {
        return d;
    }

    public void setFirst(A a) {
        this.a = a;
    }

    public void setSecond(B b) {
        this.b = b;
    }

    public void setThird(C c) {
        this.c = c;
    }

    public void setFourth(D d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "Quadruple{" + a + ", " + b + ", " + c + ", " + d + "}";
    }
}
