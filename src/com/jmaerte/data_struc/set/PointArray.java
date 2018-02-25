package com.jmaerte.data_struc.set;

/**An implementation of the PointSet, that stores the Points of the Set as a double[].
 * An instance of this realizes a {@link PointSet} with cardinality <i>n</i> in euclidean <i>R^d</i>.
 */
public class PointArray implements PointSet {

    private int d, n;
    private double[] values;

    public PointArray(int d, int n) {
        this.d = d;
        this.n = n;
        this.values = new double[n * d];
    }

    public double get(int i, int j) throws Exception {
        if(i < 0 || i >= n || j < 0 || j >= d) throw new Exception("Dimension mismatch or cardinality out of bounds.");
        return values[i * d + j];
    }

    public int dimension() {
        return d;
    }

    public int card() {
        return n;
    }

}
