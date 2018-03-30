package com.jmaerte.data_struc.miniball;

import com.jmaerte.data_struc.point_set.Euclidean;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.util.calc.Util;
import com.jmaerte.util.vector.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**An abstraction of a smallest enclosing circle.
 * This instance provides a calculation of the smallest enclosing ball of a subset of an arbitrarily large {@link PointSet} {@code S}.
 *
 */
public class Miniball {

    // input fields
    private Euclidean S;
    private int[] M;

    // tool fields
    private AffineHull hull;
    private int dim;
    private int size;

    // ball fields
    private double sqRadius = 0;
    private double[] center;

    public Miniball(Euclidean S, int[] M) {
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

    public static Ball welzl(Euclidean S) {
        int[] I = new int[S.size()];
        int[] B = new int[S.size()];
        for(int i = 0; i < S.size(); i++) {
            I[i] = i;
            B[i] = i;
        }
        return welzl(S, I, S.size(), null);
    }

    /**Uses welzl algorithm for computing the smallest enclosing ball.
     * Calling welzl(S, [0,1,...,n], []) will return SED(S)
     * @param S The overall pointset.
     * @param I interior points (maybe on the boundary - not defining)
     * @param i the occupation of I
     * @param hull the boundary points(defining the ball)
     * @return The smallest enclosing disk of S.
     */
    public static Ball welzl(Euclidean S, int[] I, int i, AffineHull hull) {
        Ball D;
        if(i == 0 || (hull != null && hull.size() == S.dimension() + 1)) {
            D = hull == null ? Ball.empty() : hull.ball();
        }else {
            int p = ThreadLocalRandom.current().nextInt(0, i);
            int temp = I[p];
            System.arraycopy(I, p + 1, I, p, i - p - 1);
            I[i - 1] = temp;
            D = welzl(S, I, --i, hull);
            if(!D.contains(temp)) {
                Vector2D<double[], Double> changes = null;
                if(hull != null) {
                    changes = hull.add(temp);
                }else {
                    hull = new AffineHull(S, p);
                }
                D = welzl(S, I, i, hull);
                if(hull.size() > 1) {
                    hull.pop(changes);
                }
            }
        }
        return D;
    }

    public static double miniball(Euclidean S, int iterations) {
        int p = ThreadLocalRandom.current().nextInt(0, S.size());
        double factor = 1 - 1/(double)iterations;
        double d = 1/2d;
        double dist = -1;
        int q = -1;
        for(int i = 0; i < S.size(); i++) {
            double curr = S.d(i, p);
            if(curr > dist) {
                q = i;
                dist = curr;
            }
        }
        double[] c = new double[S.dimension()];
        System.arraycopy(S.get(p), 0, c, 0, S.dimension());
        shift(c, S.get(q), d);
        d *= factor;
        for(int i = 0; i < iterations; i++) {
            dist = -1;
            q = -1;
            for(int j = 0; j < S.size(); j++) {
                double curr = S.d(S.get(j), c);
                if(curr > dist) {
                    q = j;
                    dist = curr;
                }
            }
            shift(c, S.get(q), d);
            System.out.println(Arrays.toString(c));
            d *= factor;
        }
        return dist;
    }

    /**Calculates c <- c + scalar*(v-c)
     *
     */
    private static void shift(double[] c, double[] v, double scalar) {
        for(int i = 0; i < c.length; i++) {
            c[i] += scalar * (v[i] - c[i]);
        }
    }
}
