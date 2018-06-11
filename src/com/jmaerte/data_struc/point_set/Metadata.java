package com.jmaerte.data_struc.point_set;

public abstract class Metadata<T> {

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


    public static Metadata<double[]> getEuclidean(int n) {
        return new Metadata<double[]>(true, n) {};
    }
}
