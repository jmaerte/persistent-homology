package com.jmaerte.data_struc.point_set;

import com.jmaerte.util.calc.Function;
import com.jmaerte.util.input.Writable;

import java.util.ArrayList;

/**
 * An instance that stores an <i>n</i> point subset of the <i>same</i> euclidean space(meaning that their dimension has
 * to be the same, what is no restriction for our purposes).
 * This provides an interface to the user since he is not bound to deliver the points in a certain data structure, what
 * will be necessary in further progress.
 *
 */
public class PointSet<T extends Writable & Function<T, Double>> implements Metric<Integer> {

    private static int ID = 0;

    private ArrayList<T> list;
    private int id;

    public PointSet() {
        this(new ArrayList<>());
    }

    public PointSet(ArrayList<T> list) {
        this.list = list;
        id = ID++;
    }

    public T get(int i) {
        return list.get(i);
    }

    public int size() {
        return list.size();
    }

    public double d(Integer i, Integer j) {
        return list.get(i).eval(list.get(j));
    }

    public int id() {
        return id;
    }
}
