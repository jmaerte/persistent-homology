package com.jmaerte.util.vector;

/**
 * Created by Julian on 25/03/2018.
 */
public class Vector5D<T,D,K,L,M> extends Vector4D<T,D,K,L> {

    private M m;

    public Vector5D(T t, D d, K k, L l, M m) {
        super(t,d,k,l);
        this.m = m;
    }

    public M getFifth() {
        return m;
    }
}
