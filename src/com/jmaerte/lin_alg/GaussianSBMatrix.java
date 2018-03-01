package com.jmaerte.lin_alg;

import com.jmaerte.util.Search;

/**
 * A sparse binary matrix implementation.
 * It calculates the elementary divisors while adding the rows to it. This provides a faster runtime of the
 * smith-algorithm, that has to run after finished adding all rows and less memory usage since linear dependent rows
 * are discarded while adding process.
 */
public class GaussianSBMatrix {

    // Rank attribute of the matrix. It is initially point_set to -1.
    private int rank;

    // temp caches that are used before smith algorithm ran.
    private int[] leading;
    private SBVector[] vectors;
    private int rows;

    public GaussianSBMatrix(int n, int m) {
        rank = -1;
        leading = new int[n];
        vectors = new SBVector[n];
        rows = 0;
    }

    public int getRank() throws Exception {
        if(rank < 0) throw new Exception("Need to run Smith-Algorithm first.");
        return rank;
    }

    private void mkPlace() {
        int[] newLeading = new int[rows * 2 + 5];
        SBVector[] newVectors = new SBVector[rows * 2 + 5];
        System.arraycopy(leading, 0, newLeading, 0, rows);
        System.arraycopy(vectors, 0, newVectors, 0, rows);
        this.leading = newLeading;
        this.vectors = newVectors;
    }

    public void push(SBVector vector) {
        if(vector.occupation() == 0) return;

        int k = Search.binarySearch(leading, vector.getEntry(0), 0, rows);
        while(vector.occupation() != 0 && k < rows && leading[k] == vector.getEntry(0)) {
            try {
                vector.add(vectors[k]);
            }catch(Exception e) {
                e.printStackTrace();
            }
            k = Search.binarySearch(leading, vector.getEntry(0), 0, rows);
        }
        if(vector.occupation() != 0) {
            // insert vector into the matrix

            if(leading.length == rows) mkPlace();
            if(rows - k > 0) {
                System.arraycopy(leading, k, leading, k + 1, rows - k);
                System.arraycopy(vectors, k, vectors, k + 1, rows - k);
            }
            leading[k] = vector.getEntry(0);
            vectors[k] = vector;
            rows++;
        }
    }

    public void smith() {
        // Algorithm

        rank = rows;

        //Clear cache variables
        leading = null;
        vectors = null;
    }

    public String toString() {
        String s = "";
        for(int i = 0; i < rows; i++) {
            s += leading[i] + ": " + vectors[i] + "\n";
        }
        return s;
    }

}
