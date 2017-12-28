package com.jmaerte.util;

public class SparseVector implements Comparable<SparseVector> {

    private static final int MINIMAL_SIZE = 16;

    int length;
    public int occupation;
    public int[] indices;

    public SparseVector(int length, int capacity) {
        this.length = length;
        this.occupation = 0;
        int size = 0;
        try {
            size = size(length, capacity);
        }catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        indices = new int[size];
    }

    public SparseVector(int length) {
        this(length, 0);
    }

    public static final SparseVector ZERO(int length) {
        return ZERO(length, 0);
    }

    public static final SparseVector ZERO(int length, int capacity) {
        return new SparseVector(length, capacity);
    }

    public SparseVector clone() {
        SparseVector v = new SparseVector(length, indices.length);
        int[] _indices = new int[indices.length];
        v.occupation = occupation;
        System.arraycopy(indices, 0, _indices, 0, occupation);
        v.indices = _indices;
        return v;
    }

    private int size(int length, int capacity) throws Exception {
        if(capacity > length) throw new Exception("Capacity must be less than length: " + capacity + " " + length);
        if(capacity < 0) throw new Exception("Capacity must be a non-negative number: " + capacity);
        return Math.min(length, ((capacity / MINIMAL_SIZE) + 1) * MINIMAL_SIZE);
    }

    public void set(int i, boolean value) {
        if(i < 0 || i >= length) {
            new Exception("Index out of Bounds: " + i).printStackTrace();
            System.exit(1);
        }
        int k = index(i);

        if(k < occupation && indices[k] == i) {
            if(!value) {
                remove(k);
            }
        }else {
            insert(k, i, value);
        }
    }

    public void remove(int k) {
        occupation--;
        if(occupation - k > 0) {
            System.arraycopy(indices, k + 1, indices, k, occupation - k);
        }
    }

    public void insert(int k, int i, boolean value) {
        if(!value) {
            return;
        }
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
        if(indices.length == length) {
            new Exception("Can't occupate more place than the vector has.").printStackTrace();
            System.exit(1);
        }

        int capacity = Math.min(length, (occupation * 3) / 2 + 1);
        int[] _values = new int[capacity];
        int[] _indices = new int[capacity];
        System.arraycopy(indices, 0, _indices, 0, occupation);
        indices = _indices;
    }

    public int getFirstIndex() {
        if(occupation == 0) return -1;
        return indices[0];
    }

    /** Adds lambda times the vector v to this vector.
     *
     * @param v the vector that shell get added.
     */
    public void add(SparseVector v) throws Exception {
        int[] ind = new int[Math.min(occupation + v.occupation, length)];
        int occ = 0;
        int i = 0;
        for(int j = 0; j < v.occupation; j++) {
            if(i >= occupation) {
                ind[occ] = v.indices[j];
            }else if(indices[i] < v.indices[j]) {
                ind[occ] = indices[i];
                j--;
                i++;
            }else if(indices[i] > v.indices[j]) {
                ind[occ] = v.indices[j];
            }else {
                i++;
                occ--;
            }
            occ++;
        }
        while(i < occupation) {
            ind[occ] = indices[i];
            occ++;
            i++;
        }
        this.indices = ind;
        this.occupation = occ;
    }

    public boolean get(int i) {
        int k = index(i);
        if(k < occupation && indices[k] == i) {
            return true;
        }else {
            return false;
        }
    }

    /** Binary searches for the index k, such that values[k] is the i-th index entry.
     * is values[k] undefined (k=-1 f.e.), so i-th index entry is 0.
     *
     * @param i index to search for
     * @return k
     */
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

    public String toString() {
        String s = "Occ: " + occupation + " -> ";
        for(int i = 0; i < occupation; i++) {
            s+= indices[i] + (i + 1 != occupation ? ", " : "");
        }
        return s;
    }

    public int compareTo(SparseVector v) {
        if(v.getFirstIndex() != getFirstIndex()) return getFirstIndex() - v.getFirstIndex();
        int result = 0;
        int i = 0;
        while(result == 0) {
            if(get(i) != v.get(i)) result = get(i) ? 1 : -1;
            i++;
        }
        return result;
    }
}