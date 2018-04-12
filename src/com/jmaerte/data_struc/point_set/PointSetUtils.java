package com.jmaerte.data_struc.point_set;

import com.jmaerte.util.calc.Function;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PointSetUtils {

    /**Instantiate a {@link PointSet} from a 2d array.
     *
     * @param arr 2d array to read from.
     * @return the point set such that arr[i] is the array corresponding to the i-th vector of the {@link PointSet}.
     */
    public static PointSet from2D_Array(double[][] arr, Metric<double[]> m) {
        if(arr.length == 0) return null;
        PointArray ps = new PointArray(arr[0].length, arr.length, m);
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
    public static PointSet fromArray(double[] arr, int n, Metric<double[]> m) {
        if(arr.length == 0 || arr.length % n != 0) return null;
        PointArray ps = new PointArray(arr.length / n, n, m);
        for(int i = 0; i < arr.length; i++) {
            ps.set(i % n, i /n, arr[i]);
        }
        return ps;
    }

    public static PointSet randomPointSet(int n, int d, double min, double max, Metric<double[]> m) throws Exception {
        PointArray pa = new PointArray(d, n, m);
        Random r = new Random();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < d; j++) {
                pa.set(i, j, r.nextDouble() * (max - min) + min);
            }
        }
        return pa;
    }

    /**Generates sphere-like data for testing purposes.
     *
     * @return
     */
    public static Euclidean getSphereData(int d, int n, double eps, double radius, Metric<double[]> m) {
        PointArray res = new PointArray(d, n, m);
        for(int i = 0; i < n; i++) {
            double[] point = new double[d];
            double sqSum = 0;
            double[] noise = new double[d];
            for(int p = 0; p < d; p++) {
                noise[p] = eps != 0 ? ThreadLocalRandom.current().nextDouble(-eps, eps) : 0;
                point[p] = ThreadLocalRandom.current().nextDouble(-radius, radius);
                sqSum += point[p] * point[p];
            }
            double sum = Math.sqrt(sqSum);
            for(int p = 0; p < d; p++) {
                res.set(i, p, radius / sum * point[p] + noise[p]);
            }
        }
        return new Euclidean(res, ScalarProduct.getStandard(d));
    }

    /**Returns a point set in a rose structure with some artificial noise.
     *
     * @param n amount of points
     * @param k number of petals. even k produces 2k petals, odd k produces k petals.
     * @param eps noise factor. every generated point on the rose set gets noised into a eps x eps square around the point(see supremal-metric)
     * @param radius the radius of the petals(the norm of the extremal point from origin)
     * @return the described rose set.
     */
    public static Euclidean getRoseData(int n, int k, double eps, double radius) {
        PointArray res = new PointArray(2, n, Metric.EUCLIDEAN);
        double theta;
        for(int i = 0; i < n; i++) {
            theta = ThreadLocalRandom.current().nextDouble(0, 2 * Math.PI);
            double factor = radius * Math.cos(k * theta);
            res.set(i, 0, factor * Math.cos(theta) + (eps > 0 ? ThreadLocalRandom.current().nextDouble(0, eps) : 0));
            res.set(i, 1, factor * Math.sin(theta) + (eps > 0 ? ThreadLocalRandom.current().nextDouble(0, eps) : 0));
        }
        return new Euclidean(res, ScalarProduct.getStandard(2));
    }

    /**Creates points on a sub-manifold from a chart of an atlas.
     * The chart is given by a function which maps points from the cuboid [0,boundary[0])x...x[0,boundary[n])
     * onto the sub-manifold. Examples for such charts are up to a less-dimensional sub-manifold bijective maps of the torus or the sphere.
     *
     * @param n amount of points to generate
     * @param d dimension of the ambient euclidean space of the target-manifold.
     * @param boundary the boundary points of the pre-image cuboid.
     * @param chart the mapping to use on the generated points
     * @return
     */
    public static Euclidean getFromMapping(int n, int d, double[] boundary, Function<double[], double[]> chart) {
        PointArray res = new PointArray(d, n, Metric.EUCLIDEAN);
        for(int i = 0; i < n; i++) {
            double[] point = new double[boundary.length];
            for(int j = 0; j < boundary.length; j++) {
                point[j] = ThreadLocalRandom.current().nextDouble(0, boundary[j]);
            }
            point = chart.eval(point);
            for(int j = 0; j < d; j++) {
                res.set(i, j, point[j]);
            }
        }
        return new Euclidean(res, ScalarProduct.getStandard(d));
    }

    public static Function<double[], double[]> torusChart(double r, double R) {
        return new Function<double[], double[]>() {
            @Override
            public double[] eval(double[] v) {
                if(v.length != 2) return null;
                double[] res = new double[3];
                res[0] = (R + r * Math.cos(v[0])) * Math.cos(v[1]);
                res[1] = (R + r * Math.cos(v[0])) * Math.sin(v[1]);
                res[2] = r * Math.sin(v[0]);
                return res;
            }
        };
    }
}
