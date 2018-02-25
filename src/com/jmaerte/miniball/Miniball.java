package com.jmaerte.miniball;

import com.jmaerte.data_struc.set.PointSet;

/**An abstraction of a smallest enclosing circle.
 * This instance provides a calculation of the smallest enclosing ball of a subset of an arbitrarily large {@link PointSet} {@code S}.
 *
 */
public class Miniball {

    private double r;
    private double[] center;

    private PointSet S;
    private int[] subset;

    public Miniball(PointSet S, int[] subset) {
        this.S = S;
        this.subset = subset;
    }

    public double[] center() {
        return center;
    }

    public double radius() {
        return r;
    }




}
