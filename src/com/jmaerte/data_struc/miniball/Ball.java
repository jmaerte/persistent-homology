package com.jmaerte.data_struc.miniball;

import com.jmaerte.data_struc.point_set.PointSet;

import java.util.Arrays;

public class Ball {
    private double[] center;
    private double radius;
    private PointSet S;

    public Ball(PointSet S, double[] center, double radius) {
        this.S = S;
        this.center = center;
        this.radius = radius;
    }

    private Ball() {}

    public boolean contains(int i) {
        return S != null && S.d(S.get(i), center) <= radius;
    }

    public double radius() {
        return radius;
    }

    public static Ball empty() {
        return new Ball();
    }

    public String toString() {
        return "[" + Arrays.toString(center) + ", " + radius + "]";
    }
}