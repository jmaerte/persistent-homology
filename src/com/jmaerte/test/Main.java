package com.jmaerte.test;

import com.jmaerte.data_struc.complex.NeighborhoodFiltration;
import com.jmaerte.data_struc.graph.WeightedGraph;
import com.jmaerte.data_struc.point_set.*;
import com.jmaerte.persistence.Persistence;

import java.util.Arrays;

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

//        PointArray S = new PointArray(2, 3);
//        try {
//            S.set(0, 0, 0);
//            S.set(0, 1, 0);
//
//            S.set(1, 0, 2);
//            S.set(1, 1, 1);
//
//            S.set(2, 0, 1.25);
//            S.set(2, 1, 0.25);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }

//        AffineHull hull = new AffineHull(S, 0);
//        try {
//            hull.add(1);
//            hull.add(2);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }


//        try {
//            PointSet S = PointSetUtils.randomPointSet(4, 3, -1_000, 1_000);
//            System.out.println(S);
//            AffineHull hull = new AffineHull(S, 0);
//            for(int i = 1; i < S.card(); i++) {
//                hull.add(i);
//            }
//        }catch(Exception e) {
//            e.printStackTrace();
//        }

//        PointArray S = new PointArray(2, 4);
//        try {
//            S.set(0, 0, 0);
//            S.set(0, 1, 0);
//
//            S.set(1, 0, 0.5);
//            S.set(1, 1, 1);
//
//            S.set(2, 0, 0.25);
//            S.set(2, 1, 1.75);
//
//            S.set(3, 0, -0.25);
//            S.set(3, 1, 0.5);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
////            PointSet S = PointSetUtils.randomPointSet(1000, 5, -1_000, 1_000);
//            VertexFactory euclidean = new Euclidean(S);
//            WitnessFiltration wf = new WitnessFiltration(euclidean, 1);
////            VietorisFiltration vf = new VietorisFiltration(euclidean);
//
//        }catch(Exception e) {
//            e.printStackTrace();
//        }

//        int n = 40;
//        int k = 10;
//
//        long ms = System.currentTimeMillis();
//        NumberShiftable shiftable = new NumberShiftable(n);
//        shiftable.reset(k);
//        int m = 0;
//        while(!shiftable.isMax()) {
////            System.out.println(shiftable);
//            shiftable.get();
//            shiftable.shift();
//            m++;
//        }
//        System.out.println(m);
//        System.out.println(System.currentTimeMillis() - ms + "ms");
//        Shiftable shift = new Shiftable(n);
//        shift.reset(k);
//        while(!shift.isMax()) {
//            shift.shift();
//        }
//        System.out.println(System.currentTimeMillis() - ms + "ms");

        PointArray S = new PointArray(2, 4);
        try {
            S.set(0, 0, 0);
            S.set(0, 1, 0);

            S.set(1, 0, 0.5);
            S.set(1, 1, 1);

            S.set(2, 0, 0.25);
            S.set(2, 1, 1.75);

            S.set(3, 0, 4);
            S.set(3, 1, 2);
        }catch(Exception e) {
            e.printStackTrace();
        }

        try{
//            PointSet S = PointSetUtils.randomPointSet(100, 5, -1_000, 1_000);
            NeighborhoodFiltration nf = new NeighborhoodFiltration(WeightedGraph.vietoris(new Euclidean(S)), 2);
            Persistence p = new Persistence(nf, 20);
            System.out.println(p);
        }catch(Exception e) {
            e.printStackTrace();
        }


    }
}
