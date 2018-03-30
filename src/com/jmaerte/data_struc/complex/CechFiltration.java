package com.jmaerte.data_struc.complex;

import com.jmaerte.data_struc.miniball.AffineHull;
import com.jmaerte.data_struc.point_set.Euclidean;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Julian on 26/03/2018.
 */
public class CechFiltration {

    private double eps = 1e-10;
    private Euclidean S;
    private ArrayList<Simplex> simplices;
    private Integer[] sigma;
    private int[] tau;
    private int k;

    public CechFiltration(Euclidean S, int k) {
        this.S = S;
        this.k = k;
        simplices = new ArrayList<>();

        int r = ThreadLocalRandom.current().nextInt(0, S.size());
        double max = -1;
        int q = -1;
        double curr = 0;
        for(int i = 0; i < S.size(); i++) {
            curr = S.q(S.get(i), S.get(r));
            if(max < curr) {
                max = curr;
                q = i;
            }
        }
        AffineHull hull = initBall();
        int[] s = new int[S.size()];
        for(int i = 0; i < s.length; i++) {
            s[i] = i;
        }
        generate(new Simplex(s, hull.radius()), hull);
    }

    private AffineHull initBall() {
        return null;
    }

    private void generate(Simplex s, AffineHull hull) {
        simplices.add(s);
        if(s.dim() == k) {
            return;
        }
        for(int i = 0; i < s.dim() + 1; i++) {
            /**If i is on the boundary of B(T)
             *      Look up if i is a support of hull.
             *      if so look if it's the origin
             *          then recalculate T
             *      remove i from T
             *      let B(s) be the ball B(T)
             *      then shrink it until we got the smallest enclosing ball again.
             * else B(s\{i}) = B(s)
             */
        }
    }
}
