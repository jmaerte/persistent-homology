package com.jmaerte.util.calc;

import com.jmaerte.data_struc.miniball.AffineHull;
import com.jmaerte.data_struc.miniball.Ball;
import com.jmaerte.data_struc.miniball.Miniball;
import com.jmaerte.data_struc.point_set.Euclidean;
import com.jmaerte.data_struc.point_set.Landmarks;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.util.vector.Vector2D;
import com.jmaerte.util.vector.Vector4D;

import java.util.Arrays;

/**
 * Created by Julian on 27/02/2018.
 */
public class Util {

    public static long binomi(int n, int k) {
        if ((n == k) || (k == 0))
            return 1;
        else
            return binomi(n - 1, k) + binomi(n - 1, k - 1);
    }

    public static int binarySearch(int j, int[] arr, int minimum, int maximum) {
        if(maximum == 0 || arr[maximum - 1] < j) return maximum;
        if(arr[minimum] > j) return minimum;
        int min = minimum;
        int max = maximum;
        while(min < max) {
            int mid = (min + max)/2;
            if(arr[mid] < j) min = mid + 1;
            else if(arr[mid] > j) max = mid;
            else return mid;
        }
        return min;
    }

    public static int binarySearch(int j, int[] arr) {
        return binarySearch(j, arr, 0, arr.length);
    }

    public static int binarySearch(double j, double[] arr, int minimum, int maximum) {
        if(maximum == 0 || arr[maximum - 1] < j) return maximum;
        if(arr[minimum] > j) return minimum;
        int min = minimum;
        int max = maximum;
        while(min < max) {
            int mid = (min + max)/2;
            if(arr[mid] < j) min = mid + 1;
            else if(arr[mid] > j) max = mid;
            else return mid;
        }
        return min;
    }

    public static int binarySearch(double j, double[] arr) {
        return binarySearch(j, arr, 0, arr.length);
    }

    public static int[] logIntersection(int[] a, int[] b) {
        if(a.length > b.length) return logIntersection(b, a);
        return Arrays.stream(a)
                .distinct()
                .filter(x -> {
                    int k = binarySearch(x, b, 0, b.length);
                    return k < b.length && b[k] == x;
                })
                .toArray();
    }

    public static int[] linIntersection(int[] a, int[] b) {
        return Arrays.stream(a)
                .distinct()
                .filter(x -> Arrays.stream(b).anyMatch(y -> y == x))
                .toArray();
    }

    public static Function<Vector4D<int[], Ball, Integer, Integer>, Vector2D<Ball, Double>> getCechFunction(PointSet<Euclidean> S) {
        return v -> {
            int[] sigma = v.getFirst();
            Ball seb = v.getSecond();
            int d = v.getThird(); // Dimension of the simplex.
            int j = v.getFourth(); // The added vertex.

            if(d == 0) {
                return new Vector2D<>(new Ball(S, S.get(j), 0d), 0d);
            }

            if(seb.contains(j)) {
                return new Vector2D<>(seb, seb.radius());
            }else {
                int[] s = new int[d];
                System.arraycopy(sigma, 0, s, 0, d);
                Ball nseb = Miniball.welzl(seb.getEuclidean(), s, d, new AffineHull(seb.getEuclidean(), j));
                return new Vector2D<>(nseb, nseb.radius());
            }
        };
    }

    public static Function<Vector2D<Integer, Integer>, Double> vietoris(PointSet S) {
        return v -> S.d(v.getFirst(), v.getSecond());
    }

    public static Function<Vector2D<Integer, Integer>, Double> witness(Landmarks L) {
        return v -> L.getValuation(v.getFirst(), v.getSecond());
    }
}
