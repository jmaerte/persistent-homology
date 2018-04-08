package com.jmaerte.data_struc.complex;

import com.jmaerte.util.calc.Function;
import com.jmaerte.util.vector.Vector2D;

public class Filtratio {

    private Tree[] top;
    private int k;
    private int dim;

    private int[] sigma;

    /**Generates a filtration of the k-skeleton of the order-n total complex K^n_{total}.
     *
     * @param n Amount of vertices.
     * @param k dimension of the skeleton.
     * @param valuation function that maps every tuple (sigma, i) to the valuation of the coface sigma\cup\{i\} of sigma.
     */
    public Filtratio(int n, int k, Function<Vector2D<Simplex, Integer>, Double> valuation) {
        this.k = k;
        this.dim = -1;
        top = new Tree[n];
        for(int i = 0; i < n; i++) {
            top[i] = new Tree(i, null);
            generate(i, valuation);
        }
    }

    private void generate(int i, Function<Vector2D<Simplex, Integer>, Double> val) {
        for(int j = 0; j < i; j++) {
            top[i].subTrees[j] = top[j];
        }
    }
}
