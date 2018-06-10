package com.jmaerte.test;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.point_set.*;
import com.jmaerte.lin_alg.BinaryVector;
import com.jmaerte.persistence.Persistence;
import com.jmaerte.util.calc.Util;
import com.jmaerte.util.input.FileIO;
import com.jmaerte.util.input.Input;
import com.jmaerte.util.input.Register;
import com.jmaerte.util.input.commands.Commands;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

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

//        PointSet S = PointSetUtils.getRoseData(10000, 3, 0.2, 4, "Rose");
//        PointSet centers = PointSetUtils.randomPointSet(3, 10, -100,100, Metric.EUCLIDEAN, "Centers");
//        PointSet S = PointSetUtils.getClusteredData(centers, 1000000, new double[]{1d, 1d, 1d}, "Cluster");
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
//        System.out.println(centers);
//        System.out.println(S);

//        PointSet<Euclidean> S = FileIO.fromCSV("C:\\Users\\Julian\\Desktop\\data.dat", Double::valueOf,
//                v -> Euclidean.fromArray(v, ScalarProduct.getStandard(v.size())), '\n', ' ', '"');


        // ------------------------ FILE CREATION SECTION ----------------------------------
//        PointSet<Euclidean> orthogonal = PointSetUtils.getFromMapping(100000, new double[]{-200, 200, -100, 100}, v -> {
//            if(v[0] < 0) {
//                return new double[]{v[0] + 100, v[1], 0};
//            }else {
//                return new double[]{0, v[1], v[0] - 100};
//            }
//        });
////
//        FileIO.toCSV("C:\\Users\\Julian\\Desktop\\latex\\Bachelor Arbeit\\new\\dat\\orthogonal\\orthogonal.dat", orthogonal);
//        System.exit(0);



        PointSet<Euclidean> parallels = FileIO.fromCSV("C:\\Users\\Julian\\Desktop\\latex\\Bachelor Arbeit\\new\\dat\\parallels\\parallels.dat", Double::valueOf,
                v -> Euclidean.fromArray(v, ScalarProduct.getStandard(v.size())), '\n', ',', '\"');

        int k = 3400;
        double[] radii = new double[]{12};
        Persistence[] p = Persistence.dimensionalityReduction(parallels, k, 0, radii);
        System.out.println(p[0].toBarcodePlot(0, 3));

//        ArrayList<Euclidean> list = new ArrayList<>();
//        list.add(Euclidean.fromArray(new double[]{-5.5d, 2d}, ScalarProduct.getStandard(2)));
//        list.add(Euclidean.fromArray(new double[]{-4.5d, 1d}, ScalarProduct.getStandard(2)));
//        list.add(Euclidean.fromArray(new double[]{-2.5d, 1d}, ScalarProduct.getStandard(2)));
//        list.add(Euclidean.fromArray(new double[]{-2.5d, 2.5d}, ScalarProduct.getStandard(2)));
//        list.add(Euclidean.fromArray(new double[]{-4d, 4d}, ScalarProduct.getStandard(2)));
//        list.add(Euclidean.fromArray(new double[]{1d, -1d}, ScalarProduct.getStandard(2)));
//        list.add(Euclidean.fromArray(new double[]{2d, 2.5d}, ScalarProduct.getStandard(2)));
//        list.add(Euclidean.fromArray(new double[]{3.5d, 1d}, ScalarProduct.getStandard(2)));
//
//        PointSet<Euclidean> dummy = new PointSet<>(list);
//        Filtration f = Filtration.cech(dummy, 3);
//        f.draw(dummy, 0, f.get(f.size() - 1).val() + 1, 1000, 1000, true);


//        Filtration f = Filtration.cech(new Landmarks<>(parallels, 1000, false), 0);
//        f.draw(parallels, 0, f.get(f.size() - 1).val() + 1, 1000, 1000, true);

//        PointSet<Lexicographic> S = FileIO.fromCSV("/home/julian/Desktop/data.txt", v -> v,
//                  v -> Lexicographic.fromString(v), '\n', ',', '"');

//        PointSet<Euclidean> S = PointSetUtils.getFromMapping(1000, 3, new double[]{2*Math.PI, 2*Math.PI}, PointSetUtils.torusChart( 5, 10));
//        PointSet<Euclidean> S = PointSetUtils.getSphereData(2, 100, 1, 4);
//        PointSet<Euclidean> base = PointSetUtils.randomPointSet(2, 2, -100, 100);
//        PointSet<Euclidean> S = PointSetUtils.getClusteredData(base, new int[]{100000, 100}, new double[]{10d, 10d});
//        PointSet<Euclidean> S = PointSetUtils.getRoseData(1000, 3, 0.2, 4);
//        Landmarks R = new Landmarks(S, 2, Landmarks.Choice.RANDOM);
//        System.out.println(PointSetUtils.toPlot(L, "firebrick1"));
//        System.out.println(PointSetUtils.toPlot(R, "blue"));
//        Filtration f = Filtration.vietoris(L, 2);
//        Filtration fr = Filtration.vietoris(R, 2);
//        Persistence p = new Persistence(f, false);
//        Persistence pr = new Persistence(fr, false);
//        System.out.println(p.toBarcodePlot(0, 2));
//        System.out.println(pr.toBarcodePlot(0, 2));

        // Dimensionality Acknowledge
//        int k = 1000;
//        int f = ThreadLocalRandom.current().nextInt(0, parallels.size());
//        double[] radii = new double[]{10};
//        Persistence[] p = Persistence.dimensionalityReduction(parallels,k,f, radii);
//        System.out.println(p[0].toBarcodePlot(0, 3));

//        Landmarks L = new Landmarks(S, 200, Landmarks.Choice.MAXMIN);
//        Filtration f = Filtration.vietoris(L, 2);
//        f.draw(L, 0, f.get(f.size() - 1).val() + 1, 1000, 1000, true);
//        Register.push("S", "PointSet from mapping", L);
        Input.main();
        Commands.print();

//        Landmarks L = new Landmarks(S, 3, Landmarks.Choice.MINMAX);
////        System.out.println(L.toPlot());
//        Filtration f = Filtration.witness_lazy(L, 2);
//        Filtration cf = Filtration.cech(new Euclidean(L, ScalarProduct.getStandard(10), "Euklid"), 2);
//        Persistence p = new Persistence(f, false);
//        Persistence pc = new Persistence(cf, false);
//        System.out.println(p.toBarcodePlot(0, 2));
//        System.out.println(pc.toBarcodePlot(0, 2));


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
