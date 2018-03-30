package com.jmaerte.data_struc.point_set;

public class Euclidean implements VertexFactory, PointSet {

    private int currID;
    private PointSet set;
    private ScalarProduct q;

    public Euclidean(PointSet set, ScalarProduct q) {
        assert set.dimension() == q.dimension();
        this.set = set;
        currID = set.size();
        this.q = q;
    }

    public double d(Integer i, Integer j) {
        return d(set.get(i), set.get(j));
    }

    public double d(double[] a, double[] b) {
        return q.d(a, b);
    }

    public double q(double[] a, double[] b) {
        return q.scalar(a, b);
    }

    public int size() {
        return currID;
    }

    public String toString() {
        return set.toString();
    }

    public int dimension() {
        return set.dimension();
    }

    public double get(int i, int j) {
        return set.get(i, j);
    }

    public double[] get(int i) {
        return set.get(i);
    }
}
