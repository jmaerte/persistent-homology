package com.jmaerte.data_struc.point_set;

import com.jmaerte.util.input.Writer;

import java.io.BufferedWriter;
import java.util.ArrayList;

/**
 * An instance that stores an <i>n</i> point subset of the <i>same</i> euclidean space(meaning that their dimension has
 * to be the same, what is no restriction for our purposes).
 * This provides an interface to the user since he is not bound to deliver the points in a certain data structure, what
 * will be necessary in further progress.
 *
 */
public abstract class PointSet<T> implements Metric<T>, Writer<T> {

    private ArrayList<T> list;
    private int id;

    private static int ID = 0;

    public PointSet() {
        this(new ArrayList<>());
    }

    public PointSet(ArrayList<T> list) {
        id = ID++;
        this.list = list;
    }

    public T get(int i) {
        return list.get(i);
    }

    public int size() {
        return list.size();
    }

    public int id() {
        return id;
    }

    public double d(Integer i, Integer j) {
        return d(get(i), get(j));
    }

    public PointSet<T> getSubSet(int[] subset) {
        ArrayList<T> sub = new ArrayList<>();
        for(int i = 0; i < subset.length; i++) {
            sub.add(list.get(subset[i]));
        }
        PointSet<T> sup = this;
        return new PointSet<T>(sub) {
            public double d(T t, T k) {
                return sup.d(t, k);
            }

            @Override
            public Metadata<T> getMetadata() {
                return sup.getMetadata();
            }

            @Override
            public void write(BufferedWriter bw, T t) throws Exception {
                sup.write(bw, t);
            }
        };
    }

    public void write(BufferedWriter bw) throws Exception {
        for(int i = 0; i < size(); i++) {
            write(bw, list.get(i));
        }
    }

    public abstract Metadata<T> getMetadata();
}
