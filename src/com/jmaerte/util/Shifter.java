package com.jmaerte.util;

import com.jmaerte.lin_alg.Vector;
import com.jmaerte.simplicial.Simplex;

/**
 * Created by Julian on 28/09/2017.
 */
public class Shifter {

    public boolean[] shift;
    public int CURRENT_K = 0;
    public int lowest = 0;
    boolean finished = false;

    public Shifter(int length) {
        shift = new boolean[length];
    }

    public void reset(int k) {
        if(k > shift.length) {
            return;// throw exception.
        }
        shift = new boolean[shift.length];
        CURRENT_K = k;
        finished = k <= 0 || k > shift.length;
        lowest = 0;
        for(int i = 0; i < k; i++) {
            shift[i] = true;
        }
    }

    public boolean isMax() {
        return finished;
    }

    public void shift() {
        boolean isMax = true;
        for(int i = 0; i < CURRENT_K; i++) {
            if(!shift[shift.length - i - 1]) isMax = false;
        }
        if(isMax) {
            finished = true;
            return;
        }
        shift(0);
    }

    private void shift(int i) {
        if(i + 1 == shift.length) {
            lowest = 0;
            return;
        }
        if(!shift[i]) {
            shift(i+1);
            return;
        }
        if(shift[i + 1]) {
            shift[i] = false;
            shift[lowest] = true;
            lowest++;
            shift(i+1);
        }else {
            shift[i + 1] = true;
            shift[i] = false;
            lowest = 0;
        }
    }

    public <V extends Vector<V>> Simplex<V> wrap(int[] IDs, VertexFactory<V> factory, Metric<V> d) {
        int[] ids = new int[CURRENT_K];
        for(int i = 0, l = 0; i < shift.length; i++) {
            if(shift[i]) {
                ids[l] = IDs[i];
                l++;
            }
        }
        return new Simplex<>(ids, factory, d);
    }

    public void print() {
        String s = "}";
        for(boolean sh : shift) {
            s = (sh ? "1" : "0") + s;
        }
        System.out.println("{" + s);
    }

    public String get() {
        String s = "";
        for(int i = 0; i < shift.length; i++) {
            s += shift[i] ? 1 : 0;
        }
        return s;
    }
}
