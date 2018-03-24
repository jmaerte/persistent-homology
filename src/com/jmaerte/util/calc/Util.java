package com.jmaerte.util.calc;

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

    public static int[] intersection(int[] a, int[] b) {
        return Arrays.stream(a)
                .distinct()
                .filter(x -> Arrays.stream(b).anyMatch(y -> y == x))
                .toArray();
    }

}
