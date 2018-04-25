package com.jmaerte.data_struc.miniball;

import com.jmaerte.data_struc.point_set.Euclidean;
import com.jmaerte.data_struc.point_set.PointSet;

import java.util.Arrays;

public class Ball {
    private Euclidean center;
    private double radius;
    private PointSet<Euclidean> S;

    public Ball(PointSet<Euclidean> S, Euclidean e, double radius) {
        this.S = S;
        this.center = e;
        this.radius = radius;
    }

    private Ball() {}

    public boolean contains(int i) {
        return S != null && S.get(i).eval(center) <= radius;
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

    public PointSet<Euclidean> getEuclidean() {
        return S;
    }
}