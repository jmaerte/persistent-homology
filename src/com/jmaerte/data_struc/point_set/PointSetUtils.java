package com.jmaerte.data_struc.point_set;

import com.jmaerte.util.calc.Function;
import com.jmaerte.util.input.Writer;
import com.jmaerte.util.log.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PointSetUtils {

    private static DecimalFormat df4 = new DecimalFormat("#.####");
//
//    /**Instantiate a {@link PointSet} from a 2d array.
//     *
//     * @param arr 2d array to read from.
//     * @return the point set such that arr[i] is the array corresponding to the i-th vector of the {@link PointSet}.
//     */
//    public static PointSet from2D_Array(double[][] arr, Metric<double[]> m, String name) {
//        if(arr.length == 0) return null;
//        PointArray ps = new PointArray(arr[0].length, arr.length, m, name);
//        for(int i = 0; i < arr.length; i++) {
//            for(int j = 0; j < arr[i].length; i++) {
//                ps.set(i, j, arr[i][j]);
//            }
//        }
//        return ps;
//    }
//
//    /**Instantiate a {@link PointSet} from a 1d array.
//     *
//     * @param arr the array of values, where arr[n * j + i] is the j-th component of the i-th vector.
//     *            (meaning that in modulo n notation the columns are the vectors)
//     * @param n the amount of vectors to parse.
//     * @return
//     * @throws Exception inherited from {@link PointArray#set(int, int, double)}.
//     */
//    public static PointSet fromArray(double[] arr, int n, Metric<double[]> m, String name) {
//        if(arr.length == 0 || arr.length % n != 0) return null;
//        PointArray ps = new PointArray(arr.length / n, n, m, name);
//        for(int i = 0; i < arr.length; i++) {
//            ps.set(i % n, i /n, arr[i]);
//        }
//        return ps;
//    }
//
    public static PointSet<double[]> randomPointSet(int n, int d, double min, double max) {
        ArrayList<double[]> array = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < n; i++) {
            double[] v = new double[d];
            for(int j = 0; j < d; j++) {
                v[j] = r.nextDouble() * (max - min) + min;
            }
            array.add(v);
        }
        return new PointSet<double[]>(array) {
            Writer writer = Writer.DoubleArray(",", "\n");

            public double d(double[] v, double[] w) {
                return ScalarProduct.getStandard(d).d(v,w);
            }

            @Override
            public Metadata<double[]> getMetadata() {
                return Metadata.getEuclidean(d);
            }

            @Override
            public void write(BufferedWriter bw, double[] doubles) throws Exception {
                writer.write(bw, doubles);
            }
        };
    }

    /**Generates sphere-like data for testing purposes.
     *
     * @return
     */
    public static PointSet<double[]> getSphereData(int d, int n, double eps, double radius) {
        ArrayList<double[]> arr = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            double[] point = new double[d];
            double sqSum = 0;
            double[] noise = new double[d];
            for(int p = 0; p < d; p++) {
                noise[p] = eps != 0 ? ThreadLocalRandom.current().nextDouble(-eps, eps) : 0;
                point[p] = ThreadLocalRandom.current().nextDouble(-radius, radius);
                sqSum += point[p] * point[p];
            }
            double sum = Math.sqrt(sqSum);

            for(int p = 0; p < d; p++) {
                point[p] = radius / sum * point[p] + noise[p];
            }
            arr.add(point);
        }
        return new PointSet<double[]>(arr) {
            Writer writer = Writer.DoubleArray(",", "\n");

            public double d(double[] v, double[] w) {
                return ScalarProduct.getStandard(d).d(v,w);
            }

            @Override
            public Metadata<double[]> getMetadata() {
                return Metadata.getEuclidean(d);
            }

            @Override
            public void write(BufferedWriter bw, double[] doubles) throws Exception {
                writer.write(bw, doubles);
            }
        };
    }

    /**Returns a point set in a rose structure with some artificial noise.
     *
     * @param n amount of points
     * @param k number of petals. even k produces 2k petals, odd k produces k petals.
     * @param eps noise factor. every generated point on the rose set gets noised into a eps x eps square around the point(see supremal-metric)
     * @param radius the radius of the petals(the norm of the extremal point from origin)
     * @return the described rose set.
     */
    public static PointSet<double[]> getRoseData(int n, int k, double eps, double radius) {
        ArrayList<double[]> arr = new ArrayList<>();
        double theta;
        for(int i = 0; i < n; i++) {
            theta = ThreadLocalRandom.current().nextDouble(0, 2 * Math.PI);
            double factor = radius * Math.cos(k * theta);
            double[] v = new double[]{
                    factor * Math.cos(theta) + (eps > 0 ? ThreadLocalRandom.current().nextDouble(0, eps) : 0),
                    factor * Math.sin(theta) + (eps > 0 ? ThreadLocalRandom.current().nextDouble(0, eps) : 0)
            };
            arr.add(v);
        }
        return new PointSet<double[]>(arr) {
            Writer writer = Writer.DoubleArray(",", "\n");

            public double d(double[] v, double[] w) {
                return ScalarProduct.getStandard(2).d(v,w);
            }

            @Override
            public Metadata<double[]> getMetadata() {
                return Metadata.getEuclidean(2);
            }

            @Override
            public void write(BufferedWriter bw, double[] doubles) throws Exception {
                writer.write(bw, doubles);
            }
        };
    }

    public static PointSet<double[]> getClusteredData(PointSet<double[]> S, int[] k, double[] radius) {
        int n = S.size();
        ArrayList<double[]> arr = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            createCluster(arr, S.get(i), k[i], radius[i]);
        }
        return new PointSet<double[]>(arr) {
            public double d(double[] v, double[] w) {
                return S.getMetadata().d(v, w);
            }

            @Override
            public Metadata<double[]> getMetadata() {
                return S.getMetadata();
            }

            @Override
            public void write(BufferedWriter bw, double[] doubles) throws Exception {
                S.write(bw, doubles);
            }
        };
    }

    /**Creates a cluster around a given center c with radius r.
     * Remark: This clusters are cuboids.(topological: (-r,r)^d)
     *
     * @param res The PointArray to fill in
     * @param center the center point in ambient space
     * @param k the magnitude of the cluster.
     * @param r radius of the cluster
     */
    private static void createCluster(ArrayList<double[]> res, double[] center, int k, double r) {
        int d = center.length;
        for(int l = 0; l < k; l++) {
            double[] v = new double[d];
            for(int j = 0; j < d; j++) {
                v[j] = center[j] + ThreadLocalRandom.current().nextDouble(-r, r);
            }
            res.add(v);
        }
    }

    /**Creates points on a sub-manifold from a chart of an atlas.
     * The chart is given by a function which maps points from the cuboid [0,boundary[0])x...x[0,boundary[n])
     * onto the sub-manifold. Examples for such charts are up to a less-dimensional sub-manifold bijective maps of the torus or the sphere.
     *
     * @param n amount of points to generate
     * @param boundary the boundary points of the pre-image cuboid, 2j, 2j+1 is the interval in j-th dimension.
     * @param chart the mapping to use on the generated points
     * @return
     */
    public static PointSet<double[]> getFromMapping(int n, double[] boundary, Function<double[], double[]> chart) {
        ArrayList<double[]> arr = new ArrayList<>();
        int dim = -1;
        for(int i = 0; i < n; i++) {
            double[] point = new double[boundary.length/2];
            for(int j = 0; j < boundary.length/2; j++) {
                point[j] = ThreadLocalRandom.current().nextDouble(boundary[2 * j], boundary[2*j + 1]);
            }
            point = chart.eval(point);
            if(dim < 0) dim = point.length;
            arr.add(point);
        }
        final int dimension = dim;
        return new PointSet<double[]>(arr) {
            Writer writer = Writer.DoubleArray(",", "\r\n");

            public double d(double[] v, double[] w) {
                return ScalarProduct.getStandard(dimension).d(v,w);
            }

            @Override
            public Metadata<double[]> getMetadata() {
                return Metadata.getEuclidean(dimension);
            }

            @Override
            public void write(BufferedWriter bw, double[] doubles) throws Exception {
                writer.write(bw, doubles);
            }
        };
    }

    public static Function<double[], double[]> torusChart(double r, double R) {
        return v -> {
            if(v.length != 2) return null;
            double[] res = new double[3];
            res[0] = (R + r * Math.cos(v[0])) * Math.cos(v[1]);
            res[1] = (R + r * Math.cos(v[0])) * Math.sin(v[1]);
            res[2] = r * Math.sin(v[0]);
            return res;
        };
    }

    public static Function<double[], double[]> swissRollMapping() {
        return v -> {
            if(v.length != 2) return null;
            return new double[]{
                    v[0] * Math.cos(v[0]),
                    v[1],
                    v[0] * Math.sin(v[0])
            };
        };
    }

    public static Function<double[], double[]> kleinBottle3Chart(double a, double b, double c) {
        return v -> {
            if(v.length != 2) return null;
            double[] res = new double[3];
            double r = c * (1 - Math.cos(v[0])/2);
            if(v[0] < Math.PI) {
                res[0] = (a * (1 + Math.sin(v[0])) + r * Math.cos(v[1])) * Math.cos(v[0]);
                res[1] = (b + r * Math.cos(v[1])) * Math.sin(v[0]);
                res[2] = r * Math.sin(v[1]);
            }else {
                res[0] = a * (1 + Math.sin(v[0])) * Math.cos(v[0]) - r * Math.cos(v[1]);
                res[1] = b * Math.sin(v[0]);
                res[2] = r * Math.sin(v[1]);
            }

            return res;
        };
    }

    public static Function<double[], double[]> kleinBottle4Chart(double a, double b, double c) {
        return v -> {
            if(v.length != 2) return null;
            double[] res = new double[4];
            if(v[0] < Math.PI) {
                res[0] = (a + b * Math.cos(v[1])) * Math.cos(v[0]);
                res[1] = (b + b * Math.cos(v[1])) * Math.sin(v[0]);
                res[2] = b * Math.sin(v[1]) * Math.cos(v[0] / 2);
                res[3] = b * Math.sin(v[1]) * Math.sin(v[0] / 2);
            }

            return res;
        };
    }

    public static String toFile(PointSet S) {
        File file = new File("output/" + Logger.dateFormat.format(Logger.date) + "/");
        file.mkdirs();
        File ps = new File("output/" + Logger.dateFormat.format(Logger.date) + "/pointset_" + S.id() + ".txt");
        try {
            ps.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(ps));
            S.write(bw);
            bw.flush();
            bw.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return ps.getAbsolutePath();
    }

    public static String toFilePlot(PointSet<double[]> S) throws Exception {
        String path = toFile(S).replace('\\', '/');
        if(S.getMetadata().dimension() > 2) return "";
        return  "library(ggplot2)\n\n" +
                "data <- read.table(\"" + path + "\", header = FALSE, sep=\",\")\n" +
                "data$ID <- seq.int(nrow(data))\n" +
                "plot <- ggplot(data) + geom_point(aes(x=V1, y=V2), colour=\"black\", size=0.5) + \n" +
                "theme_light() +\n" +
                "theme(\n" +
                "    legend.position = \"none\",\n" +
                "    panel.border = element_blank(),\n" +
                "    panel.grid.major.y = element_blank(),\n" +
                "    panel.grid.minor.y = element_blank(),\n" +
                "    panel.grid.major.x = element_blank(),\n" +
                "    panel.grid.minor.x = element_blank(),\n" +
                ") +\n" +
                "xlab(\"x\") + ylab(\"y\") + \n" +
                "geom_vline(aes(xintercept=0)) +\n" +
                "geom_hline(aes(yintercept=0))\n\n" +
                "print(plot)";
    }

    public static String toPlot(PointSet<double[]> S) throws Exception {
        if(S.getMetadata().dimension() > 2) return "";
        String x = "c(";
        String y = "c(";
        for(int i = 0; i < S.size(); i++) {
            if(i != 0) {
                x += ", ";
                y += ", ";
            }
            x += df4.format(S.get(i)[0]).replace(',', '.');
            y += df4.format(S.get(i)[1]).replace(',', '.');
        }
        x += ")";
        y += ")";
        return  "library(ggplot2)\n\n" +
                "x <- " + x + "\n" +
                "y <- " + y + "\n" +
                "data <- data.frame(id = 1:" + S.size() + ", x = x, y = y)\n" +
                "plot <- ggplot(data) + geom_point(aes(x=x, y=y), colour=\"black\", size=1) + \n" +
                "theme_light() +\n" +
                "theme(\n" +
                "    legend.position = \"none\",\n" +
                "    panel.border = element_blank(),\n" +
                "    panel.grid.major.y = element_blank(),\n" +
                "    panel.grid.minor.y = element_blank(),\n" +
                "    panel.grid.major.x = element_blank(),\n" +
                "    panel.grid.minor.x = element_blank(),\n" +
                ") +\n" +
                "geom_vline(aes(xintercept=0)) +\n" +
                "geom_hline(aes(yintercept=0))\n\n" +
                "print(plot)";
    }

    public static String toPlot(Landmarks<double[]> L, String color) throws Exception {
        String p = PointSetUtils.toFilePlot(L.pointSet());
        String landmarks = "c(";
        for(int i = 0; i < L.size(); i++) {
            landmarks += (L.landmarks()[i] + 1) + (i + 1 == L.size() ? ")" : ", ");
        }
        return p + "\n" +
                "landmarks <- " + landmarks + "\n" +
                "plot <- plot + geom_point(data = subset(data, ID %in% landmarks), aes(x = V1, y = V2), colour=\"" + color + "\", size=1.5, shape=2, fill = 1)\n" +
                "print(plot)";
    }
}
