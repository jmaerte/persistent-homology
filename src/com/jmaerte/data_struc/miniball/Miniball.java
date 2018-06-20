package com.jmaerte.data_struc.miniball;

import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.util.vector.Vector2D;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**An abstraction of a smallest enclosing circle.
 * This instance provides a calculation of the smallest enclosing ball of a subset of an arbitrarily large {@link PointSet} {@code S}.
 *
 */
public class Miniball {
    public static Ball welzl(PointSet<double[]> S) {
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
    public static Ball welzl(PointSet<double[]> S, int[] I, int i, AffineHull hull) {
        int dim = S.get(0).length;
        Ball D;
        if(i == 0 || (hull != null && hull.size() == dim + 1)) {
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

    public static double miniball(PointSet<double[]> S, int iterations) {
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
        double[] c = new double[S.get(p).length];
        System.arraycopy(S.get(p), 0, c, 0, c.length);
        shift(c, S.get(q), d);
        d *= factor;
        for(int i = 0; i < iterations; i++) {
            dist = -1;
            q = -1;
            for(int j = 0; j < S.size(); j++) {
                double curr = S.getMetadata().d(S.get(j), c);
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
