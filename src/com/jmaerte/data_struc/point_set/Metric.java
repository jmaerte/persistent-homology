package com.jmaerte.data_struc.point_set;

public interface Metric<X> {
    double d(X x, X y);

    public static final Metric<double[]> EUCLIDEAN = new Metric<double[]>() {
        @Override
        public double d(double[] x, double[] y) {
            double sum = 0;
            for(int i = 0; i < x.length; i++) sum += (x[i] - y[i]) * (x[i] - y[i]);
            return Math.sqrt(sum);
        }
    };
}
