package com.jmaerte.lin_alg;

/**
 *  A sparse binary vector implementation.
 *
 *  @version 1.0
 */
public class BinaryVector {

    public static long timeAdding = 0;

    public int simplexDim;
    public double filterVal;
    public int filterInd;

    private int dimension;
    private int[] entries;
    private int occupation;

    public BinaryVector(int dimension, int simplexDim, double filterVal, int filterInd) {
        this(dimension, new int[(int)(dimension*0.05) + 10], 0, simplexDim, filterVal, filterInd);
    }

    public BinaryVector(int dimension, int[] entries, int occupation, int simplexDim, double filterVal, int filterInd) {
        this.dimension = dimension;
        this.entries = entries;
        this.occupation = occupation;
        this.simplexDim = simplexDim;
        this.filterVal = filterVal;
        this.filterInd = filterInd;
    }

    public int index(int pos) {
        if(occupation == 0 || entries[occupation - 1] < pos) return occupation;
        int min = 0;
        int max = occupation;
        while(min < max) {
            int mid = (min + max)/2;
            if(entries[mid] < pos) min = mid + 1;
            else if(entries[mid] > pos) max = mid;
            else return mid;
        }
        return min;
    }

    public boolean isZero() {
        return occupation == 0;
    }

    /** Sets the entry at position pos of the vector to 0.
     *  Note that this is in general not the position pos of the array "entries".
     *
     * @param pos position of the vector to point_set to 0.
     */
    public void remove(int pos) {
        int k = index(pos);
        if(k < occupation && entries[k] == pos) {
            if(occupation - k > 0) System.arraycopy(entries, k + 1, entries, k, occupation - k);
            else entries[occupation] = 0;
            occupation--;
        }
    }

    public void set(int pos) throws Exception {
        if(pos >= dimension) throw new Exception("Dimension exceeded!");
        if(occupation == entries.length) mkPlace();
        int k = index(pos);
        if(k < occupation && entries[k] == pos) return;
        if(occupation - k > 0) {
            System.arraycopy(entries, k, entries, k + 1, occupation - k);
        }
        entries[k] = pos;
        occupation++;
    }

    public boolean get(int pos) {
        int k = index(pos);
        return k < occupation && entries[k] == pos;
    }

    public int getEntry(int i) {
        assert i < occupation;
        return entries[i];
    }

    private void mkPlace() {
        int[] newEntries = new int[Math.max(occupation * 2 + 5, dimension)];
        System.arraycopy(entries, 0, newEntries, 0, occupation);
        this.entries = newEntries;
    }

    public void add(BinaryVector v) throws Exception {
        if(dimension != v.dimension) throw new Exception("Dimensions are incompatible: " + dimension + " - " + v.dimension);

        int[] newEntries = new int[Math.min(occupation + v.occupation, dimension)];
        int newOccupation = 0;
        for(int i = 0, j = 0; i < occupation || j < v.occupation; ) {
            if(j == v.occupation || (i != occupation && entries[i] < v.entries[j])) {
                newEntries[newOccupation] = entries[i];
                i++;
                newOccupation++;
            }else if(i == occupation || entries[i] > v.entries[j]) {
                newEntries[newOccupation] = v.entries[j];
                j++;
                newOccupation++;
            }else {
                i++;
                j++;
            }
        }
        entries = newEntries;
        occupation = newOccupation;
    }

    public String toString() {
        String s = "[";
        for(int i = 0; i < occupation; i++) {
            s += entries[i] + (i == occupation - 1 ? "" : ", ");
        }
        return s + "]";
    }

    public int occupation() {
        return occupation;
    }
}
