package com.jmaerte.util.vector;

/**
 * Created by Julian on 25/03/2018.
 */
public class Vector3D<T,D,K> extends Vector2D<T,D> {

    private K k;

    public Vector3D(T t, D d, K k) {
        super(t,d);
        this.k = k;
    }

    public K getThird() {
        return k;
    }

}
