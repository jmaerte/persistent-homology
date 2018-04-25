package com.jmaerte.data_struc.point_set;

import com.jmaerte.util.calc.Function;
import java.util.ArrayList;

public class Euclidean implements Function<Euclidean, Double> {

    public double[] vector;
    public ScalarProduct q;

    private Euclidean() {}

    public static Euclidean fromArray(ArrayList<Double> a, ScalarProduct q) {
        Euclidean e = new Euclidean();
        e.vector = new double[a.size()];
        for(int i = 0; i < e.vector.length; i++) {
            e.vector[i] = a.get(i);
        }
        e.q = q;
        return e;
    }

    public static Euclidean fromArray(double[] a, ScalarProduct q) {
        Euclidean e = new Euclidean();
        e.vector = a;
        e.q = q;
        return e;
    }

    public double get(int i) {
        return vector[i];
    }

    public Double eval(Euclidean e) {
        return q.d(this.vector, e.vector);
    }
}
