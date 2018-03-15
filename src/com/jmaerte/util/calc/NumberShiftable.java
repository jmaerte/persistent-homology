package com.jmaerte.util.calc;

import com.jmaerte.data_struc.complex.Simplex;

import java.util.Arrays;

public class NumberShiftable {

    private int[] values;
    private int n;
    private boolean finished;
    private int lowest;

    public NumberShiftable(int n) {
        this.n = n;
    }

    public void reset(int k) {
        assert k < n +1;
        this.values = new int[k];
        finished = false;
        lowest = 0;
        for(int i = 0; i < k; i++) {
            values[i] = i;
        }
    }

    public boolean isMax() {
        return finished;
    }

    public void shift() {
        boolean isMax = true;
        for(int i = 0; i < values.length; i++) {
            isMax &= values[i] == n - values.length + i;
        }
        if(isMax) {
            finished = true;
            return;
        }
        shift(0);
    }

    private void shift(int i) {
        if(i + 1 == values.length) {
            values[i]++;
            lowest = 0;
            return;
        }
        if(values[i + 1] == values[i] + 1) {
            values[i] = lowest;
            lowest++;
            shift(i+1);
        }else {
            values[i]++;
            lowest = 0;
        }
    }

    /**Return this array as simplex. Note that the vertices are already sorted.
     *
     * @return simplex with vertices {@code values}.
     */
    public int[] get() {
        return values;
    }

    public String toString() {
        return Arrays.toString(values);
    }
}
