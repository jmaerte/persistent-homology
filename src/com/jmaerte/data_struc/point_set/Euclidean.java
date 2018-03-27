package com.jmaerte.data_struc.point_set;

public class Euclidean implements VertexFactory  {

    private int currID;
    private PointSet set;
    private ScalarProduct q;

    public Euclidean(PointSet set, ScalarProduct q) {
        assert set.dimension() == q.dimension();
        this.set = set;
        currID = set.card();
        this.q = q;
    }

    public double d(Integer i, Integer j) {
        return d(set.get(i), set.get(j));
    }

    public double d(double[] a, double[] b) {
        return q.d(a, b);
    }

    public int size() {
        return currID;
    }

    public String toString() {
        return set.toString();
    }
}
