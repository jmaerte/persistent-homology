package com.jmaerte.util.vector;

/**
 * Created by Julian on 26/03/2018.
 */
public class Vector6D<V,W,E,F,G,H> extends Vector5D<V,W,E,F,G> {

    private H h;

    public Vector6D(V v, W w, E e, F f, G g, H h) {
        super(v,w,e,f,g);
        this.h = h;
    }

    public H getSixth() {
        return h;
    }
}
