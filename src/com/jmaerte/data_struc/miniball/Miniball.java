package com.jmaerte.data_struc.miniball;

import com.jmaerte.data_struc.point_set.PointSet;

import java.util.ArrayList;
import java.util.Stack;

/**An abstraction of a smallest enclosing circle.
 * This instance provides a calculation of the smallest enclosing ball of a subset of an arbitrarily large {@link PointSet} {@code S}.
 *
 */
public class Miniball {

    /**Returns the smallest enclosing Ball of a given subset <i>M</i> of a {@link PointSet} <i>S</i>.
     *
     * @param S A fixed PointSet
     * @param M arbitrary subset of S
     * @return the smallest {@link Ball} containing M.
     */
    public static Ball miniball(PointSet S, int[] M) {
        Stack<Integer> B = new Stack<>();
        Stack<Integer> L = new Stack<>();
        for(int i : M) {
            L.push(i);
        }
        return miniball(S, B, L);
    }

    private static Ball miniball(PointSet S, Stack<Integer> R, Stack<Integer> L) {
        Ball B = null;
        if(L.isEmpty()) {

            //TODO: Affinely independent enclosing ball algorithm.


        }else {
            int u = L.pop();
            B = miniball(S, R, L);
            double dist = 0;
            try {
                for(int i = 0; i < S.dimension(); i++) {
                    dist += (B.center[i] - S.get(u, i)) * (B.center[i] - S.get(u, i));
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
            if(dist > B.sqRadius) {
                R.push(u);
                B = miniball(S, R, L);
                R.pop();
            }
            L.push(u);
        }
        return B;
    }


    public static class Ball {
        private final double sqRadius;
        private final double[] center;

        public Ball(double sqRadius, double[] center) {
            this.sqRadius = sqRadius;
            this.center = center;
        }

        public double squaredRadius() {
            return sqRadius;
        }

        public String toString() {
            String c = "(";
            for(int i = 0; i < center.length; i++) {
                c += center[i] + (i == center.length - 1 ? ")" : ", ");
            }
            return "B_(" + squaredRadius() + ") " + c;
        }
    }
}
