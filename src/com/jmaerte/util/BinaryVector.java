package com.jmaerte.util;

import com.jmaerte.err.DimensionException;

import java.util.Arrays;

/**
 * Created by Julian on 31/12/2017.
 */
public class BinaryVector {

    int[] indices;
    int length;
    int occupation;

    public BinaryVector(int length, int initialCapacity) {
        indices = new int[initialCapacity];
        this.length = length;
    }

    private BinaryVector(int length, int[] indices, int occupation) {
        this.length = length;
        this.indices = indices;
        this.occupation = occupation;
    }

    public BinaryVector add(BinaryVector that) throws DimensionException {
        if(this.length != that.length) throw new DimensionException("Can't add vectors of different dimensions: this.dimension = " + this.length + ", that.dimension = " + that.length);
        int[] xor = new int[Math.min(length, this.indices.length + that.indices.length)];
        int occupation = 0;
        for(int i = 0, j = 0; i < this.occupation || j < that.occupation;) {
            if(i == this.occupation || j == that.occupation) {
                if(i == this.occupation) {
                    xor[occupation] = that.indices[j];
                    j++;
                    occupation++;
                }else if(j == that.occupation) {
                    xor[occupation] = this.indices[i];
                    i++;
                    occupation++;
                }
            }else if(this.indices[i] != that.indices[j]) {
                while(this.indices[i] < that.indices[j] && i < this.occupation) {
                    xor[occupation] = this.indices[i];
                    i++;
                    occupation++;
                }
                while(this.indices[i] > that.indices[j] && j < that.occupation) {
                    xor[occupation] = that.indices[j];
                    j++;
                    occupation++;
                }
            }else {
                i++;
                j++;
            }

        }
        return new BinaryVector(this.length, xor, occupation);
    }

    public void set(int pos, boolean val) throws DimensionException {
        if(pos >= length || pos < 0) throw new DimensionException("Position is not inside vector dimension bounds: pos = " + (pos + 1) + ", this.dimension = " + this.length);
        int i = index(pos);
        if(i < occupation && indices[i] == pos) {
            if(!val) {
                remove(i);
            }
        }else {
            if(val) {
                insert(i, pos);
            }
        }
    }

    /**Inserts given position into the indices array at the given index.
     *
     * @param index the position in the array
     * @param pos the position of a 1 in the actual vector represented by this object.
     */
    public void insert(int index, int pos) {
        if(indices.length < occupation + 1) {
            mkPlace();
        }
        if(occupation - index > 0) {
            System.arraycopy(indices, index, indices, index + 1, occupation - index);
        }
        indices[index] = pos;
        occupation++;
    }

    private void mkPlace() {
        if(indices.length == length) {
            new DimensionException("Can't occupy more place than the vector has.").printStackTrace();
            System.exit(1);
        }
        int capacity = Math.min(length, (occupation * 3) / 2 + 1);
        int[] _indices = new int[capacity];
        System.arraycopy(indices, 0, _indices, 0, occupation);
        indices = _indices;
    }

    public void remove(int index) {
        if(index < occupation && index >= 0) {
            System.arraycopy(indices, index + 1, indices, index, occupation-index);
            occupation--;
        }
    }

    /**Binary searches for the given positions index in the indices array. If the element isn't found the place where the element should be is returned.
     * So you need to double check if the indices array really contains the searched element at the returned index.
     *
     * @param pos position to search for. Should be an element of {0,...,this.length - 1}
     * @return algorithm where the element should belong to.
     */
    private int index(int pos) {
        if(occupation == 0 || pos > indices[occupation - 1]) return occupation;
        int left = 0;
        int right = occupation;
        while(left < right) {
            int mid = (right + left)/2;
            if(indices[mid] > pos) right = mid;
            else if(indices[mid] < pos) left = mid + 1;
            else return mid;
        }
        return left;
    }

    public String toString() {
        return Arrays.toString(indices);
    }
}
