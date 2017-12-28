package com.jmaerte.main;

import com.jmaerte.lin_alg.RealVector;
import com.jmaerte.lin_alg.Vector2D;
import com.jmaerte.simplicial.Filtration;
import com.jmaerte.util.Algorithms;
import com.jmaerte.util.Metric;
import com.jmaerte.util.SEB;

import java.util.ArrayList;

/**
 * Created by Julian on 22/12/2017.
 */
public class Main {

    public static void main(String[] args) {
        Metric<Double> d = new Metric<Double>() {
            @Override
            public Double eval(Vector2D<Double, Double> tuple) {
                return Math.abs(tuple.v - tuple.w);
            }
        };// euclidean metric on R

        System.out.println(d.eval(2d, 4d));

        ArrayList<RealVector> vectors = new ArrayList<>();
        vectors.add(new RealVector(new double[] {
                -1, 0
        }));
        vectors.add(new RealVector(new double[] {
                0,1
        }));
        vectors.add(new RealVector(new double[] {
                1,0
        }));
        vectors.add(new RealVector(new double[] {
                0,0.5
        }));

        SEB seb = new SEB(vectors);
        System.out.println(seb.getBall());
//        Filtration.cech_fromVertices(new int[10], null);
    }
}
