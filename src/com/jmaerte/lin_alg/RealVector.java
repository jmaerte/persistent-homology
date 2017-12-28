package com.jmaerte.lin_alg;

/**
 * Created by Julian on 22/12/2017.
 */
public class RealVector implements Vector<RealVector> {

    int dimension;
    double[] val;

    public RealVector(int dimension) {
        this(new double[dimension]);
    }

    public RealVector(double[] val) {
        this.val = val;
        this.dimension = val.length;
    }

    public int dimension() {
        return dimension;
    }

    public RealVector plus(RealVector v) {
        if(dimension != v.dimension) {
            new Exception("Can't add vectors with different dimensions.").printStackTrace();
            System.exit(0);
        }
        double[] that = new double[dimension];
        for(int i = 0; i < dimension; i++) {
            that[i] = val[i] + v.val[i];
        }
        return new RealVector(that);
    }

    public RealVector times(RealVector d) {
        if(d.dimension != 1) {
            new Exception("Scalars can only be one dimensional!").printStackTrace();
            System.exit(0);
        }
        double[] that = new double[dimension];
        for(int i = 0; i < dimension; i++) {
            that[i] = val[i] * d.val[0];
        }
        return new RealVector(that);
    }

    public double get(int i) {
        if(i < 0 || i >= dimension) {
            new ArrayIndexOutOfBoundsException().printStackTrace();
        }
        return val[i];
    }

    public String toString() {
        String s = "(";
        for(int i = 0; i < dimension; i++) {
            s += val[i] + (i == dimension - 1 ? ")" : ", ");
        }
        return s;
    }
}
