package com.jmaerte.persistence;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.point_set.Landmarks;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.lin_alg.BinaryVector;
import com.jmaerte.util.calc.Util;
import com.jmaerte.util.log.Logger;
import com.jmaerte.util.vector.Vector2D;
import com.jmaerte.util.vector.Vector5D;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

/**
 * Created by Julian on 27/02/2018.
 */
public class Persistence {

    private static final int factor = 10;
    private static final int initCap = 16;
    private static final DecimalFormat df = new DecimalFormat("#.########", new DecimalFormatSymbols(Locale.US));

    private Filtration f;
    private BinaryVector[] matrix;
    private int[] low;
    private int[] zeroes;
    private int occupation_low;
    private int occupation_zeroes;
    private int[] lowCount;
    private int[] zeroCount;
    private Diagram[] diagram;

    public Persistence(Filtration f, boolean reduced) {
        this.f = f;
        this.matrix = new BinaryVector[initCap];
        this.low = new int[initCap];
        this.zeroes = new int[initCap];

        this.lowCount = new int[f.dimension() + 3];
        this.zeroCount = new int[f.dimension() + 3];
        this.diagram = new Diagram[Math.max(3, f.dimension() + 2)];
        for(int i = 0; i < diagram.length; i++) {
            diagram[i] = new Diagram();
        }
        long ns = System.nanoTime();
        generate(reduced);
        System.out.println("Termination done - it took " + (System.nanoTime() - ns) + "ns");
        System.out.format("%13s | %15s", "Dimension i", "i-th Betti number");
        System.out.println("\n--------------|------------------");
        for(int i = 1; i < diagram.length; i++) {
            System.out.format("%13d | %15d", i-1, zeroCount[i] - lowCount[i]);
            System.out.println();
        }
        evaluate();
    }

    /**
     *
     * @param reduced ordinary or reduced homology?
     */
    private void generate(boolean reduced) {
        Filtration.reduced(reduced);
        Logger.log("Now calculating " + (reduced ? "reduced " : "ordinary ") + "homology.");
        Logger.progress(f.size(), "Termination algorithm");
        int i = 0;
        BinaryVector missingVertex = null;
        for(BinaryVector v : f) {
//            System.out.println(v);
            int p = v.simplexDim + 1;

            if(v.isZero()) {
                addZero(v.filterInd, p);
                i++;
                continue;
            }

            int k = Util.binarySearch(v.getEntry(v.occupation() - 1), low, 0, occupation_low);
            while(k < occupation_low && !v.isZero() && low[k] == v.getEntry(v.occupation() - 1)) {
                try {
//                    currSwitches = System.nanoTime();
//                    if(matrix[k].filterInd > v.filterInd) {
//                        temp = matrix[k];
//                        matrix[k] = v;
//                        v = temp;
//                        p = v.simplexDim + 1;
//                    }
//                    switches += System.nanoTime() - currSwitches;
                    v.add(matrix[k]);
//                    System.out.println("Added " + matrix[k].filterInd + " to " + v.filterInd);
                }catch(Exception e) {
                    e.printStackTrace();
                }
                if(!v.isZero()) k = Util.binarySearch(v.getEntry(v.occupation() - 1), low, 0, k);
            }
            if(!v.isZero()) {
                if(occupation_low == matrix.length) mkPlace(true);
                if(occupation_low - k > 0) {
                    System.arraycopy(matrix, k, matrix, k + 1, occupation_low - k);
                    System.arraycopy(low, k, low, k + 1, occupation_low - k);
                }
                matrix[k] = v;
                low[k] = v.getEntry( v.occupation() - 1);
                occupation_low++;
            }else {
                addZero(v.filterInd, p);
            }
            i++;
            if(i % 1000 == 0) Logger.updateProgress(i);
        }
        for(int k = 0; k < occupation_low; k++) {
            lowCount[f.get(low[k]).depth()]++;
            diagram[f.get(low[k]).depth()].put(f.get(low[k]).val(), matrix[k].filterVal);
        }
//        System.out.println(Arrays.toString(low));
//        System.out.println(Arrays.toString(zeroes));
        Logger.close();
        // EXPERIMENTAL - TRYING TO FREE UP SPACE AFTER CALC.
//        matrix = null;
//        low = null;
//        zeroes = null;
    }

    private void evaluate() {
        int k = 0;
        for(int i = 0; i < occupation_zeroes; i++) {
            k = Util.binarySearch(zeroes[i], low, k, occupation_low);
            if(k < occupation_low && low[k] == zeroes[i]) {
                continue;
            }
            // add (a_i, inf) to diagram[p]
            diagram[f.get(zeroes[i]).depth()].put(f.get(zeroes[i]).val());
        }
    }

    public int dimension() {
        return f.dimension();
    }

    private void addZero(int i, int p) {
        int k = Util.binarySearch(i, zeroes, 0, occupation_zeroes);
        if(k < occupation_zeroes && zeroes[k] == i) return;
        if(occupation_zeroes + 1 == zeroes.length) mkPlace(false);
        if(k != occupation_zeroes) {
            System.arraycopy(zeroes, k, zeroes, k + 1, occupation_zeroes - k);
        }
        zeroes[k] = i;
        occupation_zeroes++;
        zeroCount[p]++;
    }

    private void mkPlace(boolean low) {
        if(low) {
            BinaryVector[] vectors = new BinaryVector[factor * matrix.length];
            int[] lows = new int[factor * this.low.length];
            System.arraycopy(matrix, 0, vectors, 0, occupation_low);
            System.arraycopy(this.low, 0, lows, 0, occupation_low);
            matrix = vectors;
            this.low = lows;
        }else {
            int[] zero = new int[factor * this.zeroes.length];
            System.arraycopy(zeroes, 0, zero, 0, occupation_zeroes);
            this.zeroes = zero;
        }
    }


    public String toString() {
        String s = "Persistent cycles = [\n";
        for(int p = 0; p < diagram.length; p++) {
            s += "\tp = " + (p - 1) + ": " + diagram[p] + "\n";
        }
        return s + "]";
    }

    public String toBarcodePlot(int m, int n, boolean drawAverage, boolean cut) {
        n++;
        // Color strings: note that they must be escaped by \" since we also want the rgb-function to be a possible input.
        String segmentColor = "\"black\"";
        String birthColor = "\"chartreuse4\"";
        String deathColor = "\"sienna4\"";
        double average = getAverage(m, n);
        Vector5D<String, String, int[], int[], Integer> value = this.getIntervalArrays(m, n, cut? average : -1);
        int[] nonTrivial = value.getThird();
        int[] groupSize = value.getFourth();
        int length = value.getFifth();
        String grouping = "c(";
        String groupLabelPos = "c(";
        String groupLabel = "c(";
        for(int p = 0; p < nonTrivial.length; p++) {
            grouping += (groupSize[p] + 0.5) + "" + (p + 1 != nonTrivial.length ? ", " : "");
            if(p == 0) {
                groupLabelPos += (groupSize[0]/2d + 0.5);
            }else {
                groupLabelPos += ", " + (0.5 + (double)(groupSize[p - 1] + groupSize[p])/2d);
            }
            groupLabel += "expression('H'[" + (nonTrivial[p] - 1) + "])" + (p + 1 != nonTrivial.length ? ", " : "");
        }
        grouping += ")";
        groupLabelPos += ")";
        groupLabel += ")";
        String data = "data.frame(x=1:" + length + ", value1=value1, value2=value2)";
        return "library(ggplot2)\n\n" +
                "value1 <- " + value.getFirst() + "\n" +
                "value2 <- " + value.getSecond() + "\n" +
                "grouping <- " + grouping + "\n" +
                "groupLabel <- " + groupLabel + "\n" +
                "groupLabelPos <- " + groupLabelPos + "\n" +
                "data <- " + data + "\n" +
                "vlines <- data.frame(x=grouping)\n" +
                "fin <- which(is.finite(data$value2))\n" +
                "inf <- which(is.infinite(data$value2))\n" +
                "plot <- ggplot(data) +\n" +
                "  geom_segment(data=data[fin,], aes(x=x, xend=x, y=value1, yend=value2), color=" + segmentColor + ", size=1) +\n" +
//                "  geom_point(aes(x=x, y=value1), colour=" + birthColor + ", size=3, alpha = 0.5) +\n" +
//                "  geom_point(data=data[fin,], aes(x=x, y=value2), colour=" + deathColor + ", size=3, alpha = 0.5) +\n" +
                "  coord_flip() +\n" +
                "  theme_light() +\n" +
                "  theme(\n" +
                "    legend.position = \"none\",\n" +
                "    panel.border = element_blank(),\n" +
                "    panel.grid.major.y = element_blank(),\n" +
                "    panel.grid.minor.y = element_blank(),\n" +
                "  ) +\n" +
                "  xlab(\"Simplices\") +\n" +
                "  ylab(epsilon ~ \"- Persistence\")\n" +
                "\n" +
                "x_min <- ggplot_build(plot)$layout$panel_ranges[[1]]$x.range[[1]]\n" +
                "x_max <- ggplot_build(plot)$layout$panel_ranges[[1]]$x.range[[2]]\n" +
                "if(length(inf) != 0) {\n" +
                "  plot <- plot + \n" +
                "    geom_segment(data = data[inf,], aes(y = value1,\n" +
                "                                        yend = x_max,\n" +
                "                                        x = x,\n" +
                "                                        xend = x),\n" +
                "                              linetype=\"solid\",\n" +
                "                              color=\"black\",\n" +
                "                              arrow = arrow(type=\"closed\", angle=30, length = unit(0.2,\"cm\")),\n" +
                "                              size = 1)\n" +
//                "    geom_point(data=data[inf,], aes(x=x, y=value1), colour=" + birthColor + ", size=3, alpha=0.5)\n" +
                "}\n" +
                "plot <- plot + scale_y_continuous(limits = c(x_min, x_max), expand=c(0,0)) +\n" +
                "               scale_x_continuous(breaks=groupLabelPos, label=groupLabel)\n" +
                "plot <- plot + geom_vline(data=vlines, aes(xintercept=as.numeric(x))) +\n" +
                "               geom_vline(aes(xintercept=0.5)) +\n" +
                "               geom_segment(aes(x=0.5, xend=" + (length + 0.5) + ", y=x_min, yend=x_min))\n" +
                (drawAverage ? "plot <- plot + geom_hline(aes(yintercept=" + average + "), colour=\"red\")\n" : "") +
                "print(plot)";
    }

    public String toDiagramPlot(int p) {
        String[] fields = getDiagramFrame(p);
        return "library(ggplot2)\n" +
                "\n" +
                "value1 <- " + fields[0] + "\n" +
                "value2 <- " + fields[1] + "\n" +
                "mult <- " + fields[2] + "\n" +
                "data <- data.frame(value1 = value1, value2 = value2, mult = mult)\n" +
                "\n" +
                "plot <- ggplot(data = data, aes(x = value1, y = value2, color = mult)) + \n" +
                "        geom_point() +\n" +
                "        scale_colour_gradient() +\n" +
                "        theme_light() +\n" +
                "        theme(\n" +
                "          legend.position = \"none\",\n" +
                "          panel.border = element_blank(),\n" +
                "        ) +\n" +
                "        xlab(\"death\") +\n" +
                "        ylab(\"birth\")\n" +
                "\n" +
                "plot <- plot + stat_function(fun=function(x)x, colour=\"black\")\n" +
                "print(plot)";
    }

    private String[] getDiagramFrame(int p) {
        p = p + 1;
        String value1 = "c(";
        String value2 = "c(";
        String mult = "c(";
        for(int i = 0; i < diagram[p].occ; i++){
            Diagram.Node n = diagram[p].nodes[i];
            for(int j = 0; j < n.occ; j++) {
                if(i != 0 || j != 0) {
                    value1 += ", ";
                    value2 += ", ";
                    mult += ", ";
                }
                value1 += n.a;
                value2 += n.b[j];
                mult += n.multiplicity[j];
            }
        }
        value1 += ")";
        value2 += ")";
        mult += ")";
        return new String[]{value1, value2, mult};
    }

    /**Calculates R-Script arrays a, b that contain the interval information. (a[i], b[i]) is in the diagram.
     * the overhead o meaning the amount of empty diagrams(necessary for plot code)
     * the group size-array containing the occupation of the nodes-array of the p-th diagram in p-th position.
     * the length l of the arrays.
     * @return (a,b,o,g,l)
     */
    private Vector5D<String, String, int[], int[], Integer> getIntervalArrays(int b, int m, double minPersistence) {
        String value1 = "c(";
        String value2 = "c(";
        int length = 0;
        ArrayList<Integer> groupSize = new ArrayList<>();
        ArrayList<Integer> nonTrivial = new ArrayList<>();
        for(int p = b + 1; p < m + 1; p++) {
            if(diagram[p].occ == 0) continue;
            nonTrivial.add(p);
            groupSize.add(groupSize.size() == 0 ? 0 : groupSize.get(groupSize.size() - 1));
            for(int i = 0; i < diagram[p].occ; i++) {
                if(nonTrivial.size() > 1 || i != 0) {
                    value1 += ", ";
                    value2 += ", ";
                }
                Diagram.Node n = diagram[p].nodes[i];
                for(int j = 0; j < n.occ; j++) {
                    if(n.b[j] - n.a <= minPersistence) continue;
                    for(int k = 0; k < n.multiplicity[j]; k++) {
                        value1 += df.format(n.a) + (j + 1 == n.occ && k + 1 == n.multiplicity[j] && n.infMultiplicity == 0 ? "" : ", ");
                        value2 += df.format(n.b[j]) + (j + 1 == n.occ && k + 1 == n.multiplicity[j] && n.infMultiplicity == 0 ? "" : ", ");
                        length++;
                        groupSize.set(groupSize.size() - 1, groupSize.get(groupSize.size() - 1) + 1);
                    }
                }
                if(n.infMultiplicity != 0) {
                    value1 += df.format(n.a);
                    value2 += "Inf";
                    length++;
                    groupSize.set(groupSize.size() - 1, groupSize.get(groupSize.size() - 1) + 1);
                }
            }
        }
        value1 += ")";
        value2 += ")";
        int[] nT = new int[nonTrivial.size()];
        int[] gS = new int[nT.length];
        for(int i = 0; i < nT.length; i++) {
            nT[i] = nonTrivial.get(i);
            gS[i] = groupSize.get(i);
        }
        return new Vector5D<>(value1, value2, nT, gS, length);
    }

    private double getAverage(int n, int m) {
        double av = 0d;
        int amount = 0;
        for(int i = n + 1; i < m + 1; i++) {
            for(int j = 0; j < diagram[i].occ; j++) {
                Diagram.Node node = diagram[i].nodes[j];
                for(int k = 0; k < node.occ; k++) {
                    av += node.multiplicity[k] * (node.b[k] - node.a);
                    amount += node.multiplicity[k];
                }
            }
        }
        return av / amount;
    }

    /**
     *
     * @param S PointSet in Euclidean space.
     * @param k Amount of observed points
     * @param z The element of S to look around.
     * @return
     */
    public static Persistence[] dimensionalityReduction(PointSet<double[]> S, int k, int z, double[] radii) throws Exception {
        int dim;
        try {
            dim = S.getMetadata().dimension();
        }catch(Exception e) {
            throw new Exception("Sorry, this method is euclidean PointSets only.");
        }
        Persistence[] res = new Persistence[radii.length];
        int[] neighbors = getNeighbors(S, k, z);
        double[] x = new double[dim];
        for(int i = 0; i < x.length; i++) {
            for(int j = 0; j < neighbors.length; j++) {
                x[i] += S.get(neighbors[j])[i];
            }
            x[i] *= 1d/neighbors.length;
        }

        for(int i = 0; i < radii.length; i++) {
            ArrayList<Integer> elements = new ArrayList<>();
            for(int n : neighbors) {
                if(S.getMetadata().d(x, S.get(n)) > radii[i]) {
                    elements.add(n);
                }
            }
            int[] outer = elements.stream().mapToInt(Integer::intValue).toArray();
            System.out.println(outer.length);
            Landmarks<double[]> L = new Landmarks<>(S.getSubSet(outer), 2 * dim, true);
            Filtration f = Filtration.vietoris(L, dim);
//            f.draw(L, 0, f.get(f.size() - 1).val() + 1, 1000, true);
            res[i] = new Persistence(f, false);
            System.out.println(res[i].toBarcodePlot(0, dim - 1, false, false));
        }
        return res;
    }

    private static int[] getNeighbors(PointSet<double[]> S, int k, int z) {
        PriorityQueue<Vector2D<Integer, Double>> queue = new PriorityQueue<>(new Comparator<Vector2D<Integer, Double>>() {
            @Override
            public int compare(Vector2D<Integer, Double> o1, Vector2D<Integer, Double> o2) {
                double x = o2.getSecond() - o1.getSecond();
                return (int) (Math.signum(x) * (Math.ceil(Math.abs(x))));
            }
        });
        double[] d = new double[S.size()];
        for(int i = 0; i < S.size(); i++) {
            if(queue.size() < k) {
                queue.add(new Vector2D<>(i, S.d(i, z)));
            }else {
                Vector2D<Integer, Double> root = queue.peek();
                d[i] = S.d(i, z);
                if(root.getSecond() > d[i]) {
                    queue.poll();
                    queue.add(new Vector2D<>(i, d[i]));
                }
            }
        }
        double max = queue.peek().getSecond();
        for(int i = 0; i < S.size(); i++) {
            if(d[i] == max) {
                queue.add(new Vector2D<>(i, d[i]));
            }
        }
        return Arrays.stream(queue.toArray((Vector2D<Integer, Double>[]) new Vector2D[queue.size()])).mapToInt(v -> v.getFirst()).toArray();
    }
}
