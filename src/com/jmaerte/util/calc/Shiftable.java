package com.jmaerte.util.calc;

import com.jmaerte.data_struc.point_set.PointSet;

/**
 * Created by Julian on 27/02/2018.
 */
public class Shiftable {

    // tools
    boolean[] shift;
    public int CURRENT_K = 0;
    public int lowest = 0;
    boolean finished = false;

    public Shiftable(int n) {
        shift = new boolean[n];
    }

    public void reset(int k) {
        if(k > shift.length) {
            return;// throw exception.
        }
        shift = new boolean[shift.length];
        CURRENT_K = k;
        finished = false;
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

    public void print() {
        String s = "}";
        for(boolean sh : shift) {
            s = (sh ? "1" : "0") + s;
        }
        System.out.println("{" + s);
    }

//    public Simplex get()
//    {
//        int[] result = new int[CURRENT_K];
//        for(int i = 0, x = 0; i < set.card(); i++)
//        {
//            if(shift[i])
//            {
//                result[x++] = i;
//            }
//        }
//        return new Simplex(set, result);
//    }
}