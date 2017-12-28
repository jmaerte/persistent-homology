package com.jmaerte.util;

public class Indexer {

    public int[] indices;
    public int occupation;

    public Indexer(int init) {
        occupation = 0;
        indices = new int[init];
    }

    public String toString() {
        String s = "[";
        for(int i = 0; i < occupation; i++) {
            s+= indices[i] + "" + (occupation != i + 1 ?", ":"");
        }
        return s + "]";
    }

    public void add(int i) {
        if(i < 0) {
            new Exception("Index out of Bounds: " + i).printStackTrace();
            System.exit(1);
        }
        int k = index(i);

        if(k >= occupation || indices[k] != i) {
            insert(k,i);
        }
    }

    public boolean isEmpty() {
        return occupation == 0;
    }

    public int get(int k) {
        return k < occupation ? indices[k] : -1;
    }

    private void insert(int k, int i) {
        if(indices.length < occupation + 1) {
            mkPlace();
        }
        if(occupation - k > 0) {
            System.arraycopy(indices, k, indices, k + 1, occupation - k);
        }
        indices[k] = i;
        occupation++;
    }

    private void mkPlace() {
        int capacity = (occupation * 3) / 2 + 1;
        int[] _indices = new int[capacity];
        System.arraycopy(indices, 0, _indices, 0, occupation);
        indices = _indices;
    }

    /**Removes at the position k in the array.
     *
     * @param k
     */
    public void removePos(int k) {
        occupation--;
        if(occupation - k > 0) {
            System.arraycopy(indices, k + 1, indices, k, occupation - k);
        }
    }

    /**Removes element i from the array.
     *
     * @param i
     */
    public void remove(int i) {
        int k = index(i);
        if(k < occupation && indices[k] == i) {
            removePos(k);
        }
    }

    public void empty() {
        indices = new int[indices.length];
        occupation = 0;
    }

    public int index(int i) {
        if(occupation == 0 || i > indices[occupation - 1]) return occupation;
        int left = 0;
        int right = occupation;
        while(left < right) {
            int mid = (right + left)/2;
            if(indices[mid] > i) right = mid;
            else if(indices[mid] < i) left = mid + 1;
            else return mid;
        }
        return left;
    }

}