package com.jmaerte.util;

/**
 * Created by Julian on 22/12/2017.
 */
public class Ball<X> {

    Metric<X> d;
    X center;
    public double radius;

    public Ball(X center, double radius, Metric<X> d) {
        if(radius < 0) {
            new Exception("Radius needs to be non-negative").printStackTrace();
            System.exit(0);
        }
        this.center = center;
        this.radius = radius;
        this.d = d;
    }

    public boolean contains(X x) {
        return d.eval(center, x) < radius;
    }

    public String toString() {
        return "B_" + radius + " " + center;
    }
}
