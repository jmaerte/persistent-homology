package com.jmaerte.data_struc.point_set;

import java.util.Random;

public class PointSetUtils {

    /**Instantiate a {@link PointSet} from a 2d array.
     *
     * @param arr 2d array to read from.
     * @return the point set such that arr[i] is the array corresponding to the i-th vector of the {@link PointSet}.
     */
    public static PointSet from2D_Array(double[][] arr) throws Exception {
        if(arr.length == 0) return null;
        PointArray ps = new PointArray(arr[0].length, arr.length);
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; i++) {
                ps.set(i, j, arr[i][j]);
            }
        }
        return ps;
    }

    /**Instantiate a {@link PointSet} from a 1d array.
     *
     * @param arr the array of values, where arr[n * j + i] is the j-th component of the i-th vector.
     *            (meaning that in modulo n notation the columns are the vectors)
     * @param n the amount of vectors to parse.
     * @return
     * @throws Exception inherited from {@link com.jmaerte.data_struc.point_set.PointArray#set(int, int, double)}.
     */
    public static PointSet fromArray(double[] arr, int n) throws Exception {
        if(arr.length == 0 || arr.length % n != 0) return null;
        PointArray ps = new PointArray(arr.length / n, n);
        for(int i = 0; i < arr.length; i++) {
            ps.set(i % n, i /n, arr[i]);
        }
        return ps;
    }

    public static PointSet randomPointSet(int n, int d, double min, double max) throws Exception {
        PointArray pa = new PointArray(d, n);
        Random r = new Random();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < d; j++) {
                pa.set(i, j, r.nextDouble() * (max - min) + min);
            }
        }
        return pa;
    }

}
