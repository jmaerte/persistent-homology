package com.jmaerte.lin_alg;

/**
 * Created by Julian on 22/12/2017.
 */
public interface Vector<V extends Vector<V>> {

    int dimension();
    Vector<V> plus(Vector<V> v);
    Vector<V> times(V v);
}
