package com.jmaerte.data_struc.point_set;

public class Euclidean implements VertexFactory {

    private int currID;
    private PointSet set;

    public Euclidean(PointSet set) {
        this.set = set;
        currID = set.card();
    }

    public double d(Integer i, Integer j) {
        double sum = 0;
        try {
            for(int k = 0; k < set.dimension(); k++) {
                double x = set.get(i, k) - set.get(j, k);
                sum +=  x*x;
            }
        } catch(Exception e) {e.printStackTrace();}
        return sum;
    }

    public int size() {
        return currID;
    }
}
