package com.jmaerte.util;

import com.jmaerte.lin_alg.RealVector;
import com.jmaerte.lin_alg.Vector;
import com.jmaerte.lin_alg.Vector2D;

import java.util.ArrayList;

import static com.jmaerte.lin_alg.Standard.euclidean;

/**
 * Created by Julian on 22/12/2017.
 */
public class SEB {

    ArrayList<RealVector> set;
    Ball<RealVector> ball = null;

    public SEB(ArrayList<RealVector> set) {
        this.set = set;
    }

    private Ball<RealVector> seb(boolean[] interior, boolean[] boundary) {
        boolean isEmpty = true;
        int first = 0;
        Ball<RealVector> b = null;
        for(int i = 0; i < interior.length && isEmpty; i++) {
            if(interior[i]) {
                first = i;
                isEmpty = false;
            }
        }
        if(isEmpty) {
            RealVector sum = null;
            RealVector firstV = null;
            int m = 0;
            for(int i = 0; i < boundary.length; i++) {
                if(boundary[i]) {
                    if(sum == null) {
                        sum = set.get(i);
                        firstV = sum;
                    }else {
                        sum = sum.plus(set.get(i));
                    }
                    m++;
                }
            }
            if(m != 0) {
                sum = sum.times(new RealVector(new double[] {
                        1/(double) m
                }));
                return new Ball<>(sum, euclidean.eval(sum, firstV), euclidean);
            }else {
                return new Ball<>(new RealVector(0), 0, euclidean);
            }
        }else {
            interior[first] = false;
            b = seb(interior, boundary);
            if(b.radius <= 0 || !b.contains(set.get(first))) {
                boundary[first] = true;
                b = seb(interior, boundary);
                boundary[first] = false;
            }
            interior[first] = true;
        }
        return b;
    }

    public Ball<RealVector> getBall() {
        if(ball == null) {
            boolean[] interior = new boolean[set.size()];
            boolean[] boundary = new boolean[set.size()];
            for(int i = 0; i < interior.length; i++) interior[i] = true;
            ball = seb(interior, boundary);
        }
        for(RealVector v : set) {
            System.out.println(v);
            System.out.println(euclidean.eval(v, ball.center));
        }
        return ball;
    }
}
