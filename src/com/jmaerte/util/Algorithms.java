package com.jmaerte.util;

import com.jmaerte.lin_alg.RealVector;
import static com.jmaerte.lin_alg.Standard.*;

import java.util.ArrayList;

/**
 * Created by Julian on 22/12/2017.
 */
public class Algorithms {

    public static Ball<RealVector> smallestEnclosingBall(ArrayList<RealVector> set) {
        return seb(set, new ArrayList<>());
    }

    /**This is a smallest enclosing ball algorithm implemented
     * in the style of edelsbrunner "Computational Topology" S.73.
     *
     * @return Returns the smallest enclosing ball of the input set.
     */
    private static Ball<RealVector> seb(ArrayList<RealVector> interior, ArrayList<RealVector> boundary) {
        System.out.println(interior + " " + boundary);
        Ball<RealVector> b = null;
        if(interior.isEmpty()) {
            double[] arr = new double[boundary.get(0).dimension()];
            for(RealVector v : boundary) {
                for(int i = 0; i < arr.length; i++) {
                    arr[i] += v.get(i);
                }
            }
            for(int i = 0; i < arr.length; i++) arr[i] *= 1/(double) boundary.size();
            RealVector center = new RealVector(arr);
            double r = euclidean.eval(center, boundary.get(0));
            return new Ball<>(center, r, euclidean);
        }else {
            RealVector x = interior.get(0);
            interior.remove(0);
            b = seb(interior, boundary);
            if(!b.contains(x)) {
                boundary.add(x);
                b = seb(interior, boundary);
            }
        }
        return b;
    }

}
