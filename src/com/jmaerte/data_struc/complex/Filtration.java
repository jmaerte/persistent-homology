package com.jmaerte.data_struc.complex;

import com.jmaerte.lin_alg.SBVector;

import java.util.Arrays;
import java.util.Iterator;

/**This is an interface that represents a filtration in the simplicial sense.
 * Note that an implementation should offer the simplices in compatible ordering.
 */
public abstract class Filtration implements Iterator<SBVector> {

    private Tree simplices;

    private int i = 0;

    @Override
    public boolean hasNext() {
        return i < size();
    }

    @Override
    public SBVector next() {
        int[] faces = faces(i);
        Arrays.sort(faces);
        int[] entries = new int[faces.length];
        System.arraycopy(faces, 0, entries, 0, faces.length);
        i++;
        return new SBVector(size(), entries, faces.length);
    }

    /**How many simplices are involved?
     *
     * @return Size of the totalcomplex.
     */
    public abstract int size();

    /**Get the i-th simplex.
     *
     * @param i index of the simplex in compatible ordering.
     * @return
     */
    public abstract Simplex get(int i);
    public abstract int[] faces(int i);
    public abstract int dimension();
}
