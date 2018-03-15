package com.jmaerte.util;

public class Vector2D<V,E> {

    private V v;
    private E e;

    public Vector2D(V v, E e) {
        this.v = v;
        this.e = e;
    }

    public V getFirst() {
        return v;
    }

    public E getSecond() {
        return e;
    }
}
