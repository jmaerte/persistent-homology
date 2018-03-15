package com.jmaerte.data_struc.point_set;

import java.text.DecimalFormat;

/**An implementation of the PointSet, that stores the Points of the Set as a double[].
 * An instance of this realizes a {@link PointSet} with cardinality <i>n</i> in euclidean <i>R^d</i>.
 */
public class PointArray implements PointSet {

    private static DecimalFormat df2 = new DecimalFormat("0.##");

    private final int d, n;
    private final double[] values;

    public PointArray(int d, int n) {
        this.d = d;
        this.n = n;
        this.values = new double[n * d];
    }

    public double get(int i, int j) throws Exception {
        if(i < 0 || i >= n || j < 0 || j >= d) throw new Exception("Dimension mismatch or cardinality out of bounds.");
        return values[i * d + j];
    }

    public void set(int i, int j, double val) throws Exception {
        if(i < 0 || i >= n || j < 0 || j >= d) throw new Exception("Dimension mismatch or cardinality out of bounds.");
        values[i * d + j] = val;
    }

    public int dimension() {
        return d;
    }

    public int card() {
        return n;
    }

    public String toString() {
        String s = "";
        for(int j = 0; j < d; j++) {
            for(int i = 0; i < n; i++) {
                s += df2.format(values[i * d + j]) + "\t";
            }
            s+= "\n";
        }
        return s;
    }

}
