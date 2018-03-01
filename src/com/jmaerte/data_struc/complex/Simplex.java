package com.jmaerte.data_struc.complex;

import com.jmaerte.data_struc.miniball.AffineHull;
import com.jmaerte.data_struc.miniball.Miniball;
import com.jmaerte.data_struc.point_set.PointSet;

/**
 * This class represents a simplex spanned by a subset <i>M</i> of a {@link PointSet} <i>S</i>.
 * We assume that M is affinely-independent.
 */
public class Simplex {

    private final PointSet S;

    protected final int[] M;
    protected final double sqRadius;

    public Simplex(PointSet S, int[] M) {
        this.S = S;
        this.M = M;
        sqRadius = Miniball.miniball(S,M).squaredRadius();
    }


}