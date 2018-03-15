package com.jmaerte.data_struc.miniball;

import static com.jmaerte.util.log.Logger.*;

import com.jmaerte.data_struc.point_set.PointSet;

import java.text.DecimalFormat;

/**
 * This is an affine hull-implementation for a cardinality <i>k+1</i> subset A of a cardinality <i>n</i> {@link PointSet} S in <i>R^d</i>.
 * It provides multiple features like checking if an Element lies inside the affine hull or calculating the
 * affine coordinates of a point that lies inside the affine hull.
 * Furthermore the hull is in contrast to the {@link PointSet} S mutable, meaning that the subset A can be changed at runtime.
 * Q is a recursively constructed matrix. It is the inverse matrix to Q' := (q(T_i, T_ij))_{i,j = 1}^n the matrix of coefficients of the standard-scalarproduct
 * q: R^d x R^d -> R with respect to the linearly independent vectors T_i := t_i - t_0, where M = {t_0, ..., t_k}.
 * This is obviously a <i>k</i> \times <i>k</i> - matrix with real entries.
 * Since M is affinely independent k + 1 = card(A) is less or equal to d+1. This is why we allocate d^2 doubles for saving this matrix.
 *
 * For the recursion formula see
 */
public class AffineHull {

    private static DecimalFormat df2 = new DecimalFormat("0.##");

    private final PointSet S;

    private int dim;
    private int size;

    private int[] A;

    private double[][] T;// T[i] = t_i - t_0
    private double[][] Q;// Q_ij = Q[j][i]. It is upper triangular save-able, since it is symmetric.
    private double[] Tq;// Tq[i] = q(T[i], T[i])

    private double[] c; // coordinates of the center of the unique Ball B with center c in Aff(A) and A \subset \partial(B)
                        // Such that C = \sum lambda[i] * T[i], where lambda = 1/2 * (Q * Tq)
    private double[] c_coef;
    private double sqRadius;

    public AffineHull(PointSet S, int initIndex) {
        this.S = S;

        // init affine hull
        this.dim = S.dimension();
        this.size = 1;
        this.A = new int[dim + 1];
        A[0] = initIndex;

        // init tools
        this.T = new double[dim][dim];
        this.Q = new double[dim][dim];
        this.Tq = new double[dim];

        // init result fields
        this.c = new double[dim];
        this.c_coef = new double[dim + 1];
        try {
            for(int i = 0; i < dim; i++) {
                c[i] = S.get(initIndex, i);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        c_coef[0] = 1;
        sqRadius = 0;
    }

    /**Note: This is not the dimension of the affine hull.
     * You can get the dimension of the hull by using the {@link #size()} functionality.
     * dim(aff(A)) = card(A) - 1.
     *
     * @return the dimension of the data.
     */
    public int dimension() {
        return dim;
    }

    /**Generator set size.
     *
     * @return Size of the generator set of this affine hull instance.
     */
    public int size() {
        return size;
    }

    /**Adds a vector of S to the generators of this affine hull instance.
     * This function calculates a value (namely <i>y</i>) that later gets inverted, be sure that this value doesn't get too small.
     * It is in fact(see Lemma ) the distance from T_{m+1} = S.get(index) - S.get(A[0]) = t_{m+1} - t_0, m = card(A),
     * to its orthogonal projection \pi_A (T_{m+1}) squared.
     *
     * For the full construction see chapter <b>Smallest enclosing ball</b>.
     *
     * @param index index of vector to be added in S.
     * @throws Exception it is either an Exception inherited by {@link com.jmaerte.data_struc.point_set.PointSet#get(int, int)} or a PrecisionReachedException when the value y gets too small(i.e. the vector is too close to the affine hull).
     */
    public void add(int index) throws Exception {
        int m = size - 1;
        for(int i = 0; i < dim; i++) {
            T[m][i] = S.get(index, i) - S.get(A[0], i);
        }
        if(m == 0) {
            Tq[0] = q(T[0], T[0]);
            Q[0][0] = 1/Tq[0];
        }else {
            double[] mu = new double[m + 1];
            double[] v = new double[m];
            for(int i = 0; i < m; i++) {
                v[i] = q(T[i], T[m]);
            }
            mu[m] = -1;
            // calculate mu
            for(int j = 0; j < m; j++) {
                for(int i = 0; i < m; i++) {
                    mu[i] += v[j] * Q[j][i];
                }
            }

            // calculate z
            double y = 0;
            for(int i = 0; i < m; i++) {
                y += mu[i] * v[i];
            }
            Tq[m] = q(T[m], T[m]);
            y = Tq[m] - y;

            // Recursively add to Q
            for(int j = 0; j < m + 1; j++) {
                for(int i = 0; i < m + 1; i++) {
                    Q[j][i] += mu[i] * mu[j] / y;
                }
            }
        }
        A[size] = index;
        calculateBall();
        size++;
        log("Successfully added vector " + index + ", squared radius: " + sqRadius);
        String s = "";
        for(int i = 0; i < size; i++) {
            s  += c_coef[i] + "\t";
        }
        log("Coefficients: " + s);
    }

    /**An intern function that updates the center and squared radius fields after a vector being added or removed.
     *
     * @throws Exception inherited by {@link com.jmaerte.data_struc.point_set.PointSet#get(int, int)}(Shouldn't appear anyway)
     */
    private void calculateBall() throws Exception {
        double[] lambda = new double[dim];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                lambda[i] += Q[j][i] * Tq[j];
            }
            lambda[i] *= 0.5;
        }
        c = new double[dim];
        double lambda_0 = 1;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < dim; j++) {
                c[j] += lambda[i] * T[i][j];
            }
            c_coef[i + 1] = lambda[i];
            lambda_0 -= lambda[i];
        }
        c_coef[0] = lambda_0;
        sqRadius = q(c, c);
        for(int i = 0; i < dim; i++) {
            c[i] += S.get(A[0], i);
        }
    }

    public void pop(int index) {

    }

    private double q(double[] x, double[] y) {
        double scalar = 0;
        for(int i = 0; i < dim; i++) {
            scalar += x[i] * y[i];
        }
        return scalar;
    }

    public String toString() {
        String s = "";
        for(int j = 0; j < dim; j++) {
            for(int i = 0; i < size; i++) {
                try{
                    s += df2.format(S.get(A[i], j)) + "\t";
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
            s += "\n";
        }
        s += "\n\nQ^(-1):\n";
        for(int i = 0; i < size - 1; i++) {
            for(int j = 0; j < size - 1; j++) {
                s += Q[j][i] + "\t";
            }
            s += "\n";
        }
        s += "\n\nCenter: ";
        for(int i = 0; i < dim; i++) {
            s += c[i] + "\t";
        }
        s += "\nSquared Radius: " + sqRadius;
        return s;
    }
}
