package com.jmaerte.util.vector;

/**
 * Created by Julian on 25/03/2018.
 */
public class Vector4D<T,D,K,L> extends Vector3D<T,D,K> {

    private L l;

    public Vector4D(T t, D d, K k, L l) {
        super(t,d,k);
        this.l = l;
    }

    public L getFourth() {
        return l;
    }

}
