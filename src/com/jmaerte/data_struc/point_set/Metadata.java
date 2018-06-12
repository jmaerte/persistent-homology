package com.jmaerte.data_struc.point_set;

public abstract class Metadata<T> implements Metric<T> {

    private boolean isEuclidean;
    private int dimension;

    public Metadata(boolean isEuclidean, int dimension) {
        this.isEuclidean = isEuclidean;
        this.dimension = dimension;
    }

    public boolean isEuclidean() {
        return isEuclidean;
    }

    public int dimension() throws Exception {
        if(!isEuclidean) throw new Exception("The PointSet is not euclidean.");
        return dimension;
    }

    public abstract ScalarProduct product();

    public static Metadata<double[]> getEuclidean(int n) {
        ScalarProduct q = ScalarProduct.getStandard(n);
        return new Metadata<double[]>(true, n) {
            @Override
            public ScalarProduct product() {
                return q;
            }

            public double d(double[] v, double[] w) {
                return q.d(v, w);
            }
        };
    }
}
