package com.jmaerte.data_struc.miniball;

import com.jmaerte.data_struc.point_set.PointSet;

import java.util.ArrayList;
import java.util.Stack;

/**An abstraction of a smallest enclosing circle.
 * This instance provides a calculation of the smallest enclosing ball of a subset of an arbitrarily large {@link PointSet} {@code S}.
 *
 */
public class Miniball {

    // input fields
    private PointSet S;
    private int[] M;

    // tool fields
    private AffineHull hull;
    private int dim;
    private int size;

    // ball fields
    private double sqRadius = 0;
    private double[] center;

    public Miniball(PointSet S, int[] M) {
        this.S = S;
        this.M = M;
        this.dim = S.dimension();
        this.size = M.length;

        this.center = new double[dim];

        try{
            initBall();
            if(sqRadius != 0) calculate();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**initializes the ball as the minimal ball that has center p = S.get(M[0]) and radius
     * max_{q \in M} ||S.get(q) - p||.
     *
     */
    private void initBall() throws Exception {
        for(int i = 0; i < dim; i++) {
            center[i] = S.get(M[0], i);
        }
        int k = -1;
        for(int i = 1; i < size; i++) {
            double sum = 0;
            for(int j = 0; j < dim; j++) {
                double x = S.get(i, j) - center[j];
                sum += x * x;
            }
            if(sum > sqRadius) {
                k = i;
                sqRadius = sum;
            }
        }
        hull = new AffineHull(S, k);
    }

    /**This method shrinks the initial ball. At every time the ball given through sqRadius and center contains the whole set M.
     * This happens through letting the center walk towards the center of the ball given in {@code hull].
     *
     */
    private void calculate() {

    }

    public double squaredRadius() {
        return sqRadius;
    }
}
