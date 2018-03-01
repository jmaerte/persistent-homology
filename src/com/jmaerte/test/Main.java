package com.jmaerte.test;

import com.jmaerte.data_struc.miniball.Miniball;
import com.jmaerte.data_struc.point_set.PointArray;
import com.jmaerte.lin_alg.GaussianSBMatrix;
import com.jmaerte.lin_alg.SBVector;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
//        SBVector v = new SBVector(3, new int[]{1,2}, 2);
//        SBVector w = new SBVector(3, new int[]{0,2}, 2);
//        SBVector u = new SBVector(3, new int[]{0,1}, 2);
//        GaussianSBMatrix m = new GaussianSBMatrix(3, 3);
//        m.add(v);
//        System.out.println(m);
//        m.add(w);
//        System.out.println(m);
//        m.add(u);
//        System.out.println(m);


//        int n = 10000;
//        int m = 1000;
//        Random r = new Random();
//        int[][] entries = new int[n][m];
//        int[] occs = new int[n];
//        for(int i = 0; i < n; i++) {
//            for(int j = 0; j < m; j++) {
//                int rand = r.nextInt(1000);
//                if(rand < 0.05 * 1000) {
//                    entries[i][occs[i]] = j;
//                    occs[i]++;
//                }
//            }
//        }
//
//        long ms = System.currentTimeMillis();
//        GaussianSBMatrix matrix = new GaussianSBMatrix(n, m);
//        for(int i = 0; i < n; i++) {
//            System.out.print("Pushed " + i + "/" + n + " - RunTime: " + (System.currentTimeMillis() - ms) + "ms, Time adding: " + SBVector.timeAdding/1_000_000 + "\r");
//            matrix.push(new SBVector(m, entries[i], occs[i]));
//        }
//        try{
//            matrix.smith();
//            System.out.println();
//            System.out.println((System.currentTimeMillis() - ms) + " ms, Rank: " + matrix.getRank());
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//    }

        PointArray S = new PointArray(3, 4);
        try {
            S.set(0, 0, 1);
            S.set(0, 1, 0);
            S.set(0, 2, 0);

            S.set(1, 0, 0);
            S.set(1, 1, 1);
            S.set(1, 2, 0);

            S.set(2, 0, 0);
            S.set(2, 1, 0);
            S.set(2, 2, 1);

            S.set(3, 0, 0);
            S.set(3, 1, 0);
            S.set(3, 2, 0);
        }catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println(Miniball.miniball(S, new int[]{0,1,2,3}));
    }
}
