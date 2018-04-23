package com.jmaerte.test;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.point_set.*;
import com.jmaerte.lin_alg.BinaryVector;
import com.jmaerte.persistence.Persistence;
import com.jmaerte.util.calc.Util;

public class Main {

    public static void main(String[] args) {
//        BinaryVector v = new BinaryVector(3, new int[]{1,2}, 2);
//        BinaryVector w = new BinaryVector(3, new int[]{0,2}, 2);
//        BinaryVector u = new BinaryVector(3, new int[]{0,1}, 2);
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
//            System.out.print("Pushed " + i + "/" + n + " - RunTime: " + (System.currentTimeMillis() - ms) + "ms, Time adding: " + BinaryVector.timeAdding/1_000_000 + "\r");
//            matrix.push(new BinaryVector(m, entries[i], occs[i]));
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

//        PointArray S = new PointArray(2, 4);
//        try {
//            S.set(0, 0, -0.7);
//            S.set(0, 1, 0.71414);
//
//            S.set(1, 0, -0.25);
//            S.set(1, 1, -0.9682);
//
//            S.set(2, 0, 0.3);
//            S.set(2, 1, -0.953939);
//
//            S.set(3, 0, 0.87);
//            S.set(3, 1, 0.49305);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }

//        try{
//            PointSet S = PointSetUtils.randomPointSet(15, 5, -100, 100);
//            NeighborhoodFiltration nf = new NeighborhoodFiltration(WeightedGraph.vietoris(new Euclidean(S)), 6);
////            for(int i = 0; i < nf.size(); i++) {
////                System.out.println(i + " -> " + nf.get(i));
////            }
//            Persistence p = new Persistence(nf, 20);
////            System.out.println(p);
//            System.out.println(p.toBarcodePlot());
//        }catch(Exception e) {
//            e.printStackTrace();
//        }

//        Vector2D<Double, Double> v = NeighborhoodFiltration.testIntersection(100, 2, 20);
//        System.out.println(v.getFirst());
//        System.out.println(v.getSecond());

//        Euclidean S = PointSetUtils.getSphereData(2, 1000, 0, 1, Metric.EUCLIDEAN);
////        Filtration nf = new NeighborhoodFiltration(WeightedGraph.vietoris(S), 19, NeighborhoodFiltration.LOGINTERSECTION);
////        Persistence p = new Persistence(nf, 16);
////        System.out.println(p.toBarcodePlot());
//        long ms = System.currentTimeMillis();
//        System.out.println(Miniball.welzl(S) + " in " + (System.currentTimeMillis() - ms) + "ms");

//        Euclidean S = PointSetUtils.getSphereData(2, 100, 0.5, 4, Metric.EUCLIDEAN);
//        S.toFile();
//        System.out.println(S.toPlot());
//        CechFiltration cf = new CechFiltration(S, 3);
//        NeighborhoodFiltration vf = new NeighborhoodFiltration(WeightedGraph.vietoris(S), 3, NeighborhoodFiltration.LOGINTERSECTION);
//        Persistence p = new Persistence(cf, 16);
//        Persistence pv = new Persistence(vf, 16);
//        System.out.println(p.toBarcodePlot(1,2));
//        System.out.println(p.toDiagramPlot(1));
//        System.out.println(pv.toBarcodePlot(1, 2));
//        System.out.println(pv.toDiagramPlot(1));

        PointSet S = PointSetUtils.getRoseData(10000, 3, 0.2, 4, "Rose");
//        S.toFile();
//        System.out.println(S.toPlot());
//        CechFiltration cf = new CechFiltration(S, 3);
//        NeighborhoodFiltration vf = new NeighborhoodFiltration(WeightedGraph.vietoris(S), 3, NeighborhoodFiltration.LOGINTERSECTION);
//        Persistence p = new Persistence(cf, 16);
//        Persistence pv = new Persistence(vf, 16);
//        System.out.println(p.toBarcodePlot(1,2));
//        System.out.println(p.toDiagramPlot(1));
//        System.out.println(pv.toBarcodePlot(1, 2));
//        System.out.println(pv.toDiagramPlot(1));

//        Euclidean S = PointSetUtils.getRoseData(130, 3, 0.1, 4);
//        System.out.println(S.toPlot());
//        NeighborhoodFiltration nf = new NeighborhoodFiltration(WeightedGraph.witness(S, 20), 6, NeighborhoodFiltration.LOGINTERSECTION);
////            for(int i = 0; i < nf.size(); i++) {
////                System.out.println(i + " -> " + nf.get(i));
////            }
//            Persistence p = new Persistence(nf, 20);
////            System.out.println(p);
//            System.out.println(p.toBarcodePlot(1, 2));

//        Euclidean S = PointSetUtils.getSphereData(2, 10, 0.5, 4, Metric.EUCLIDEAN);

//        Euclidean S = PointSetUtils.getSphereData(2, 1000, 0.5, 4, Metric.EUCLIDEAN);
        System.out.println(S);

        Landmarks L = new Landmarks(S, 100, Landmarks.Choice.MINMAX);
        System.out.println(L.toPlot());
        Filtration f = Filtration.witness_lazy(L, 3);
        Persistence p = new Persistence(f, 16);
        System.out.println(p.toBarcodePlot(1,2));

//        Filtration f = new Filtration(100, 3, v -> S.d(v.getFirst(), v.getSecond()));
////        Filtration f = Filtration.example();
//        Persistence p = new Persistence(f, 16);
////        System.out.println(p.toBarcodePlot(0, 2));
//        System.out.println(p.toBarcodePlot(1,2));

//        Filtration c = new Filtration(120);
//        c.generate(3, Util.getCechFunction(S));
//        Persistence p = new Persistence(c, 16);
//        System.out.println(p.toBarcodePlot(1, 2));

//        Filtration w = new Filtration(5, 3, Util.witness(L));
//        Persistence p = new Persistence(w, 16);
//        System.out.println(p.toBarcodePlot(1, 2));


//        Euclidean S = PointSetUtils.getRoseData(100, 3, 0.5, 4);
//        WeightedGraph g = WeightedGraph.vietoris(S);
//        Filtration f = new Filtration(1000, 3, new Function<Vector2D<Simplex, Integer>, Double>() {
//            public Double eval(Vector2D<Simplex, Integer> v) {
//                double w = -1;
//                for(int i = 0; i < v.getFirst().getVertices().length; i++) {
//                    double d = S.d(v.getSecond(), v.getFirst().getVertices()[i]);
//                    if(d > w) {
//                        w = d;
//                    }
//                }
//                return w;
//            }
//        });

//        // A test with a increasing not-filled triangle.
//        Simplex[] simplices = new Simplex[]{
//                new Simplex(new int[]{}, 1),
//                new Simplex(new int[]{0}, 1),
//                new Simplex(new int[]{1}, 1),
//                new Simplex(new int[]{2}, 1),
//                new Simplex(new int[]{0,1}, 1),
//                new Simplex(new int[]{1,2}, 2),
//                new Simplex(new int[]{3}, 2),
//                new Simplex(new int[]{0,3}, 3),
//                new Simplex(new int[]{4}, 3),
//                new Simplex(new int[]{3,4}, 4),
//                new Simplex(new int[]{2,4}, 4),
//                new Simplex(new int[]{0,4}, 5),
//        };
//        Filtration f = new Filtration(){
//            public int size() {
//                return simplices.length;
//            }
//
//            public Simplex get(int i) {
//                return simplices[i];
//            }
//
//            public int[] faces(int i) {
//                switch(simplices[i].dim() + 1) {
//                    case 0: return new int[]{};
//                    case 1: return new int[]{0};
//                    case 2:
//                        int[] res = new int[2];
//                        for(int k = 0; k < simplices.length; k++) {
//                            if(simplices[k].dim() + 1 == 1) {
//                                if(simplices[k].get(0) == simplices[i].get(0)) {
//                                    res[0] = k;
//                                }else if(simplices[k].get(0) == simplices[i].get(1)) {
//                                    res[1] = k;
//                                }
//                            }
//                        }
//                        return res;
//                }
//                return new int[0];
//            }
//
//            @Override
//            public int dimension() {
//                return 1;
//            }
//        };
//        Persistence p = new Persistence(f, 10);
//        System.out.println(p);
//        System.out.println(p.toBarcodePlot());
    }
}
