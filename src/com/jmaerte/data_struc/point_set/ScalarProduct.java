package com.jmaerte.data_struc.point_set;

/**
 * Created by Julian on 27/03/2018.
 */
public abstract class ScalarProduct {

    public abstract double scalar(double[] a, double[] b);
    abstract int dimension();

    public double d(double[] a, double[] b) {
        double[] x = new double[dimension()];
        for(int i = 0; i < dimension(); i++) {
            x[i] = a[i] - b[i];
        }
        return Math.sqrt(scalar(x,x));
    }

    public static ScalarProduct getStandard(int dim) {
        return new ScalarProduct() {
            @Override
            public double scalar(double[] a, double[] b) {
                double sum = 0;
                try {
                    for(int k = 0; k < dimension(); k++) {
                        sum += a[k] * b[k];
                    }
                } catch(Exception e) {e.printStackTrace();}
                return sum;
            }

            @Override
            public int dimension() {
                return dim;
            }
        };
    }
}
