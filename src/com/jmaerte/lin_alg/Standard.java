package com.jmaerte.lin_alg;

import com.jmaerte.util.Metric;

/**
 * Created by Julian on 22/12/2017.
 */
public class Standard {

    public static Metric<RealVector> euclidean = new Metric<RealVector>() {
        @Override
        public Double eval(Vector2D<RealVector, RealVector> tuple) {
            if(tuple.v.dimension() != tuple.w.dimension()) {
                new Exception("Can't use metric on vectors with different dimensions.").printStackTrace();
                System.exit(0);
            }
            double sum = 0;
            for(int i = 0; i < tuple.v.dimension(); i++) {
                sum += (tuple.v.get(i) - tuple.w.get(i)) * (tuple.v.get(i) - tuple.w.get(i));
            }
            return Math.sqrt(sum);
        }
    };

}
