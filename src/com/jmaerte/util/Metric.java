package com.jmaerte.util;

import com.jmaerte.lin_alg.Vector2D;

/**
 * Created by Julian on 22/12/2017.
 */
public abstract class Metric<X> implements Function<Vector2D<X,X>, Double> {

    public double eval(X x, X y) {
        return eval(new Vector2D<>(x,y));
    }

}