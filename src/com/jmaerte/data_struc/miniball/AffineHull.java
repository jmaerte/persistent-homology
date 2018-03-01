package com.jmaerte.data_struc.miniball;

import com.jmaerte.data_struc.point_set.PointSet;

/**
 * This is an affine hull-implementation for a cardinality <i>k</i> subset A of a cardinality <i>n</i> {@link PointSet} S in <i>R^d</i>.
 * It provides multiple features like checking if an Element lies inside the affine hull or calculating the
 * affine coordinates of a point that lies inside the affine hull.
 * Furthermore the hull is in contrast to the {@link PointSet} S mutable, meaning that the subset M can be changed at runtime.
 */
public class AffineHull {

    private final PointSet S;

    private int dim;
    private int size;

    private int[] A;

    public AffineHull(PointSet S, int initIndex) {
        this.S = S;

        this.dim = S.dimension();
        this.size = 1;
        A[0] = initIndex;
        this.A = new int[dim + 1];
    }

    public int dimension() {
        return dim;
    }

    public int size() {
        return size;
    }

    public void add(int index) {

    }

}
