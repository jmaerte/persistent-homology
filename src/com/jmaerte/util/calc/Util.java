package com.jmaerte.util.calc;

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

}
