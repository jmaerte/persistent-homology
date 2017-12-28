package com.jmaerte.util;

import java.lang.reflect.Array;

/**
 * Created by Julian on 28/09/2017.
 */
public class IndexList <T extends Comparable<T>> {

    private static final int INITIAL_SIZE = 16;

    public T[] list;
    private Class<T[]> clazz;
    public int occupation;

    public IndexList(Class<T[]> clazz, int initial) {
        this.clazz = clazz;
        this.list = clazz.cast(Array.newInstance(clazz.getComponentType(), initial));
        this.occupation = 0;
    }

    public IndexList(Class<T[]> clazz) {
        this(clazz, INITIAL_SIZE);
    }

    public int occupation() {
        return this.occupation;
    }

    public boolean add(T t) {
        int k = index(t);

        if(k >= occupation || list[k].compareTo(t) != 0) {
            insert(k, t);
            return true;
        }
        return false;
    }

    public void remove(T t) {
        int k = index(t);
        if(k < occupation && list[k].compareTo(t) == 0) removePos(k);
    }

    private void removePos(int k) {
        if(occupation - k > 0) System.arraycopy(list, k + 1, list, k, occupation - k);
        occupation--;
    }

    private void insert(int k, T t) {
        if(list.length < occupation + 1) mkPlace();
        if(occupation - k > 0) System.arraycopy(list, k, list, k + 1, occupation - k);
        list[k] = t;
        occupation++;
    }

    private void mkPlace() {
        int capacity = (occupation * 3) / 2 + 1;
        T[] _list = clazz.cast(Array.newInstance(clazz.getComponentType(), capacity));
        System.arraycopy(list, 0, _list, 0, occupation);
        list = _list;
    }

    private int index(T t) {
        if(occupation == 0 || t.compareTo(list[occupation - 1]) > 0) return occupation;
        int left = 0;
        int right = occupation;
        while(left < right) {
            int mid = (right + left) / 2;
            if(list[mid].compareTo(t) > 0) right = mid;
            else if(list[mid].compareTo(t) < 0) left = mid + 1;
            else return mid;
        }
        return left;
    }

    public String toString() {
        String s = "[";
        for(int i = 0; i < occupation; i++) {
            s+= list[i].toString() + (occupation != i + 1 ?", ":"");
        }
        return s + "]";
    }
}
