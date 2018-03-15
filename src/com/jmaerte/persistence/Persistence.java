package com.jmaerte.persistence;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.lin_alg.SBVector;
import com.jmaerte.util.calc.Util;

/**
 * Created by Julian on 27/02/2018.
 */
public class Persistence {

    private static final int factor = 10;

    private Filtration f;
    private SBVector[] matrix;
    private int[] low;
    private int occupation;
    private int[] lowCount;
    private int[] zeroCount;

    public Persistence(Filtration f, int initCap) {
        this.f = f;
        this.matrix = new SBVector[initCap];
        this.low = new int[initCap];

        this.lowCount = new int[f.dimension() + 2];
        this.zeroCount = new int[f.dimension() + 2];
        generate();
        System.out.format("%13s | %15s", "Dimension i", "Rank of H_i(K)");
        System.out.println("\n--------------|------------------");
        for(int i = 0; i < lowCount.length; i++) {
            System.out.format("%13d | %15d", i-1, zeroCount[i] - lowCount[i]);
            System.out.println();
        }
    }

    private void generate() {
        while(f.hasNext()) {
            SBVector v = f.next();
            int p = v.occupation();

            if(v.isZero()) {
                zeroCount[p]++;
                continue;
            }

            int k = Util.binarySearch(v.getEntry(v.occupation() - 1), low, 0, occupation);
            while(k < occupation && !v.isZero() && low[k] == v.getEntry(v.occupation() - 1)) {
                try {
                    v.add(matrix[k]);
                }catch(Exception e) {
                    e.printStackTrace();
                }
                if(!v.isZero()) k = Util.binarySearch(v.getEntry(v.occupation() - 1), low, 0, occupation);
            }
            if(!v.isZero()) {
                if(occupation == matrix.length) mkPlace();
                if(occupation - k > 0) {
                    System.arraycopy(matrix, k, matrix, k + 1, occupation - k);
                    System.arraycopy(low, k, low, k + 1, occupation - k);
                }
//                for(int i = 0; i < occupation; i++) {
//                    int m = matrix[i].index(v.getEntry(v.occupation() - 1));
//                    if(m < matrix[i].occupation() && matrix[i].getEntry(m) == v.getEntry(v.occupation() - 1)) {
//                        try {
//                            matrix[i].add(v);
//                        }catch(Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
                matrix[k] = v;
                low[k] = v.getEntry( v.occupation() - 1);
                occupation++;
                lowCount[f.get(low[k]).dim() + 1]++;
            }else {
                zeroCount[p]++;
            }
        }
    }

    private void mkPlace() {
        SBVector[] vectors = new SBVector[factor * matrix.length];
        int[] lows = new int[factor * low.length];
        System.arraycopy(matrix, 0, vectors, 0, occupation);
        System.arraycopy(low, 0, lows, 0, occupation);
        matrix = vectors;
        low = lows;
    }


    public String toString() {
        String s = "Matrix:\n";
        for(int i = 0; i < occupation; i++) {
            s += matrix[i] + "\n";
        }
        return s;
    }
}
