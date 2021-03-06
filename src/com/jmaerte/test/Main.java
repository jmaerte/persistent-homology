package com.jmaerte.test;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.point_set.*;
import com.jmaerte.lin_alg.BinaryVector;
import com.jmaerte.persistence.Persistence;
import com.jmaerte.util.calc.Util;
import com.jmaerte.util.input.FileIO;
import com.jmaerte.util.input.Input;
import com.jmaerte.util.input.Register;
import com.jmaerte.util.input.Writer;
import com.jmaerte.util.input.commands.Commands;

import java.awt.*;
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



//        PointSet<double[]> parallels = FileIO.fromCSV("/home/julian/Desktop/data.csv", Double::valueOf,
//                list -> list.stream().mapToDouble(d -> d).toArray(), '\n', ',', '\"',
//                d -> Metadata.getEuclidean(d.length), d -> Writer.DoubleArray(",", "\n"));
//
//        int k = 4000;
//        double[] radii = new double[]{0.0005};
//        Persistence[] p = null;
//        try {
//            p = Persistence.dimensionalityReduction(parallels, k, 0, radii);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(p[0].toBarcodePlot(0, 3));

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

//        PointSet<double[]> basis = PointSetUtils.randomPointSet(3, 2, -10, 10);
//        PointSet<double[]> sphere = PointSetUtils.getClusteredData(basis, new int[]{4, 5, 6}, new double[]{1, 2, 2});
//        Filtration F = Filtration.cech(sphere, 3);
//        F.draw(sphere, 0, F.get(F.size() - 1).val() + 1, 1000, true);
//
//
//        PointSet<double[]> S = PointSetUtils.getFromMapping(1000, new double[]{0, 2*Math.PI, 0, 2*Math.PI}, PointSetUtils.torusChart( 5, 10));


//        Filtration f = FileIO.fromCSV("C:\\Users\\Julian\\Desktop\\filtration.txt");
//        PointSet<double[]> S = FileIO.fromCSV("C:\\Users\\Julian\\Desktop\\PointSet.txt", Double::valueOf,
//                list -> list.stream().mapToDouble(d -> d).toArray(), '\n', ',', '\"',
//                d -> Metadata.getEuclidean(d.length), d -> Writer.DoubleArray(",", "\n"));
//        System.out.println(S.size());
//        Persistence p = new Persistence(f, false);
//        System.out.println(p.toBarcodePlot(0, 2));
//        f.draw(S, 0, f.get(f.size() - 1).val() + 1, 1000, false);

//        PointSet<double[]> S = PointSetUtils.getFromMapping(1000, new double[]{0, 2 * Math.PI, 0, 2 * Math.PI}, PointSetUtils.torusChart(5, 10));
//        FileIO.toCSV("C:\\Users\\Ina\\Desktop\\points.csv", S);
//        Landmarks<double[]> L = new Landmarks<>(S, 80, Landmarks.Choice.MAXMIN);
//        Filtration f = Filtration.cech(L, 3);
//        Persistence p = new Persistence(f, false);
//        System.out.println(p.toBarcodePlot(1, 2));
//        f.draw(L, 0, f.get(f.size() - 1).val() + 1, 1000, false);

//        PointSet<double[]> S = FileIO.fromCSV("C:\\Users\\Ina\\Desktop\\Julian\\latex\\Bachelor Arbeit\\new\\dat\\cluster\\density\\s1_set.txt", Double::valueOf,
//                list -> list.stream().mapToDouble(d -> d).toArray(), '\n', ' ', '\"',
//                d -> Metadata.getEuclidean(d.length), d -> Writer.DoubleArray(",", "\n"));
//        Landmarks<double[]> L = new Landmarks<>(S, 200, Landmarks.Choice.RANDOM);
//        Filtration F = Filtration.witness_lazy(L, 1);
//        try {
//            System.out.println(PointSetUtils.toPlot(L, "red"));
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        Persistence P = new Persistence(F, false);
//        System.out.println(P.toBarcodePlot(0,0, true, true));

//-------------------------------------------------------------------------------------------------------------------------------------------------------
//        PointSet<double[]> S = FileIO.fromCSV("C:\\Users\\Ina\\Desktop\\Julian\\latex\\Bachelor Arbeit\\new\\dat\\moby_word_list.txt", Double::valueOf,
//                list -> list.stream().mapToDouble(d -> d).toArray(), '\n', ' ', '\"',
//                d -> Metadata.getEuclidean(d.length), d -> Writer.DoubleArray(",", "\n"));

//        PointSet<String> S = FileIO.fromCSV("C:\\Users\\Ina\\Desktop\\Julian\\latex\\Bachelor Arbeit\\new\\dat\\moby_word_list.txt",
//                x -> x, x -> x.get(0), '\n', ' ', '"',
//                d -> Metadata.Levehnshtein, d -> Writer.String());
//        Landmarks<String> L = new Landmarks<>(S, 800, true);
//        Filtration f = Filtration.witness_lazy(L, 1);
//        Persistence p = new Persistence(f, false);
//        System.out.println(p.toBarcodePlot(0, 0, true, false));

        PointSet<double[]> S = FileIO.fromCSV("C:\\Users\\Ina\\Desktop\\Julian\\latex\\Bachelor Arbeit\\new\\dat\\cluster\\density\\s1_set.txt", Double::valueOf,
                list -> list.stream().mapToDouble(d -> d).toArray(), '\n', ' ', '\"',
                d -> Metadata.getEuclidean(d.length), d -> Writer.DoubleArray(",", "\n"));
//        Landmarks<double[]> L = new Landmarks<double[]>(S, 300, true);
//        Filtration F = Filtration.cech(L, 1);
//        Persistence p = new Persistence(F, false);
//        System.out.println(p.toBarcodePlot(0, 0, true, true));

////        Filtration k = Filtration.cech(S, 0);
////        k.draw(S, 0, 100, 1000, false);
//
//
////        double a = ThreadLocalRandom.current().nextDouble(-1, 1);
////        PointSet<double[]> S = PointSetUtils.getFromMapping(500, new double[]{-10, 10},
////                (x) -> new double[]{x[0] + ThreadLocalRandom.current().nextDouble(-1, 1),
////                    x[0] + ThreadLocalRandom.current().nextDouble(-1, 1)});
////        try {
////            System.out.println(PointSetUtils.toFilePlot(S));
////        }catch(Exception e) {
////            e.printStackTrace();
////        }
//
//        // General metric case
//
        System.out.println(S.size());
        int[] neighbors = Persistence.getNeighbors(S, 300, 0);
        int indexZero = 0;
        for(int i = 0; i < neighbors.length; i++) {
            if(neighbors[i] == 0) indexZero = i;
        }
        S = S.getSubSet(neighbors);
        double sum = 0;
        int n = 0;
        for(int i = 0; i < S.size(); i++) {
            for(int j = i + 1; j < S.size(); j++) {
                sum += S.d(i, j);
                n++;
            }
        }
        sum = sum / n;
        int max = -1;
        int index = -1;
        ArrayList<Integer> others = new ArrayList<>();
        for(int i = 0; i < S.size(); i++) {
            int curr = 0;
            for(int j = 0; j < S.size(); j++) {
                if(S.d(i, j) < sum) curr++;
            }
            if(index < 0 || curr > max) {
                max = curr;
                index = i;
            }
        }

        System.out.println(index);
        try {
            System.out.println(PointSetUtils.toPlot(new Landmarks(S, new int[]{indexZero}, new double[0][0]), "red"));
        }catch(Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println(PointSetUtils.toPlot(new Landmarks(S, new int[]{index}, new double[0][0]), "red"));
        }catch(Exception e) {
            e.printStackTrace();
        }

//
//
//
//        //Euclidean
////        int[] neighbors = Persistence.getNeighbors(S, 700, 4);
////        double[] x = new double[3];
////        for(int i = 0; i < x.length; i++) {
////            for(int j = 0; j < neighbors.length; j++) {
////                x[i] += S.get(neighbors[j])[i];
////            }
////            x[i] *= 1d/neighbors.length;
////        }
//        Filtration test = Filtration.vietoris(new Landmarks<String>(S, 60, true), 4);
//        Persistence p = new Persistence(test, false);
//        System.out.println(p.toBarcodePlot(0, 3, false, false));
//        double[] radii = new double[]{4, 5, 6};
//        for(int i = 0; i < radii.length; i++) {
//            ArrayList<Integer> elements = new ArrayList<>();
//            for(int j = 0; j < S.size(); j++) {
//                if(S.d(index, j) > radii[i]) {
//                    elements.add(j);
//                }
//            }
//            int[] outer = elements.stream().mapToInt(Integer::intValue).toArray();
//            System.out.println(outer.length);
//            Landmarks<String> L = new Landmarks<>(S.getSubSet(outer), Math.min(60,outer.length), true);
//            Filtration f = Filtration.vietoris(L, 4);
////            f.draw(L, 0, f.get(f.size() - 1).val() + 1, 1000, true);
//            System.out.println(new Persistence(f, false).toBarcodePlot(0, 3, false, false));
//        }

//------------------------------------------------------------------------------------------------------------------------------------

//        PointSet<double[]> S = FileIO.fromCSV("C:\\Users\\Ina\\Desktop\\Julian\\latex\\Bachelor Arbeit\\new\\dat\\rea16.csv", Double::valueOf,
//                list -> list.stream().mapToDouble(d -> d).toArray(), '\n', ',', '\"',
//                d -> Metadata.getEuclidean(d.length), d -> Writer.DoubleArray(",", "\n"));
//
//        Landmarks<double[]> L = new Landmarks<>(S, 500, false);
//        Filtration f = Filtration.cech(L, 1);
//        Persistence p = new Persistence(f, false);
//        System.out.println(p.toBarcodePlot(0, 0, true, false));

//        PointSet<String> S = FileIO.fromCSV("C:\\Users\\Ina\\Desktop\\Julian\\latex\\Bachelor Arbeit\\new\\dat\\moby_word_list.txt",
//                x -> x, x -> x.get(0), '\n', ' ', '"',
//                d -> Metadata.Levehnshtein, d -> Writer.String());
//        Filtration k = Filtration.cech(S, 0);
//        k.draw(S, 0, 100, 1000, false);


//        double a = ThreadLocalRandom.current().nextDouble(-1, 1);
//        PointSet<double[]> S = PointSetUtils.getFromMapping(500, new double[]{-10, 10},
//                (x) -> new double[]{x[0] + ThreadLocalRandom.current().nextDouble(-1, 1),
//                    x[0] + ThreadLocalRandom.current().nextDouble(-1, 1)});
//        try {
//            System.out.println(PointSetUtils.toFilePlot(S));
//        }catch(Exception e) {
//            e.printStackTrace();
//        }

        // General metric case

//        System.out.println(S.size());
//        int[] neighbors = Persistence.getNeighbors(S, 300, 0); // 0 = Aale
//        S = S.getSubSet(neighbors);
//        double sum = 0;
//        int n = 0;
//        for(int i = 0; i < S.size(); i++) {
//            for(int j = i + 1; j < S.size(); j++) {
//                sum += S.d(i, j);
//                n++;
//            }
//        }
//        sum = sum / n;
//        int max = -1;
//        int index = -1;
//        for(int i = 0; i < S.size(); i++) {
//            int curr = 0;
//            for(int j = 0; j < S.size(); j++) {
//                if(S.d(i, j) < sum) curr++;
//            }
//            if(index < 0 || curr > max) {
//                max = curr;
//                index = i;
//            }
//        }
//
//        System.out.println(index);



//        //Euclidean
//        int[] neighbors = Persistence.getNeighbors(S, 300, 4);
//        double[] x = new double[16];
//        for(int i = 0; i < x.length; i++) {
//            for(int j = 0; j < neighbors.length; j++) {
//                x[i] += S.get(neighbors[j])[i];
//            }
//            x[i] *= 1d/neighbors.length;
//        }
//        Filtration test = Filtration.vietoris(new Landmarks<double[]>(S, 60, true), 4);
//        Persistence p = new Persistence(test, false);
//        System.out.println(p.toBarcodePlot(0, 3, false, false));
//        double[] radii = new double[]{4E-5, 6E-5};
//        for(int i = 0; i < radii.length; i++) {
//            ArrayList<Integer> elements = new ArrayList<>();
//            for(int j = 0; j < S.size(); j++) {
//                if(S.getMetadata().d(x, S.get(j)) > radii[i]) {
//                    elements.add(j);
//                }
//            }
//            int[] outer = elements.stream().mapToInt(Integer::intValue).toArray();
//            System.out.println(outer.length);
//            Landmarks<double[]> L = new Landmarks<>(S.getSubSet(outer), Math.min(60,outer.length), true);
//            Filtration f = Filtration.vietoris(L, 4);
////            f.draw(L, 0, f.get(f.size() - 1).val() + 1, 1000, true);
//            System.out.println(new Persistence(f, false).toBarcodePlot(0, 3, false, false));
//        }

//------------------------------------------------------------------------------------------------------------
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
//        Register.push("T", "PointSet from Torus mapping of size 1000", S, PointSet.class);
//        Input.main();
//        Commands.print();

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
