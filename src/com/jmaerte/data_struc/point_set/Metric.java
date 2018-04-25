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

    public static final Metric<String> Levehnshtein = ((u, v) -> {
        int m = u.length() + 1;
        int n = v.length() + 1;
        int[] col = new int[n];
        int[] newCol = new int[n];
        for(int i = 0; i < n; i++) {
            col[i] = i;
        }
        for(int j = 1; j < m; j++) {
            newCol[0] = j;
            for(int i = 1; i < n; i++) {
                int equality;
                if(u.charAt(j - 1) == v.charAt(i - 1)) equality = 0;
                else equality = 1;
                int col_replace = col[i - 1] + equality;
                int col_insert = col[i] + 1;
                int col_delete = col[i - 1] + 1;

                newCol[i] = Math.min(Math.min(col_replace, col_insert), col_delete);
            }
            int[] swap = col;
            col = newCol;
            newCol = swap;
        }
        return col[n - 1];
    });
}
