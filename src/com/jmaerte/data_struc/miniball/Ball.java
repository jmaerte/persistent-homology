package com.jmaerte.data_struc.miniball;

import com.jmaerte.data_struc.point_set.PointSet;

public class Ball {
    private double[] center;
    private double radius;
    private PointSet<double[]> S;

    public Ball(PointSet<double[]> S, double[] e, double radius) {
        this.S = S;
        this.center = e;
        this.radius = radius;
    }

    private Ball() {}

    public boolean contains(int i) {
        return S != null && S.getMetadata().d(S.get(i), center) <= radius;
    }

    public double radius() {
        return radius;
    }

    public static Ball empty() {
        return new Ball();
    }

    public String toString() {
        return "[" + center.toString() + ", " + radius + "]";
    }

    public PointSet<double[]> getEuclidean() {
        return S;
    }
}