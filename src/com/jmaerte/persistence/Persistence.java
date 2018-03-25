package com.jmaerte.persistence;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.lin_alg.SBVector;
import com.jmaerte.util.vector.Vector4D;
import com.jmaerte.util.calc.Util;
import com.jmaerte.util.vector.Vector5D;

/**
 * Created by Julian on 27/02/2018.
 */
public class Persistence {

    private static final int factor = 10;

    private Filtration f;
    private SBVector[] matrix;
    private int[] low;
    private int[] zeroes;
    private int occupation_low;
    private int occupation_zeroes;
    private int[] lowCount;
    private int[] zeroCount;
    private Diagram[] diagram;

    public Persistence(Filtration f, int initCap) {
        this.f = f;
        this.matrix = new SBVector[initCap];
        this.low = new int[initCap];
        this.zeroes = new int[initCap];

        this.lowCount = new int[f.dimension() + 2];
        this.zeroCount = new int[f.dimension() + 2];
        this.diagram = new Diagram[f.dimension() + 2];
        for(int i = 0; i < diagram.length; i++) {
            diagram[i] = new Diagram();
        }
        generate();
        System.out.format("%13s | %15s", "Dimension i", "Rank of H_i(K)");
        System.out.println("\n--------------|------------------");
        for(int i = 0; i < lowCount.length; i++) {
            System.out.format("%13d | %15d", i-1, zeroCount[i] - lowCount[i]);
            System.out.println();
        }
        evaluate();
    }

    private void generate() {
        int i = 0;
        while(f.hasNext()) {
            System.out.println(i + "/" + f.size());
            SBVector v = f.next();
            int p = v.occupation();

            if(v.isZero()) {
                addZero(i++, p);
                continue;
            }

            int k = Util.binarySearch(v.getEntry(v.occupation() - 1), low, 0, occupation_low);
            while(k < occupation_low && !v.isZero() && low[k] == v.getEntry(v.occupation() - 1)) {
                try {
                    v.add(matrix[k]);
                }catch(Exception e) {
                    e.printStackTrace();
                }
                if(!v.isZero()) k = Util.binarySearch(v.getEntry(v.occupation() - 1), low, 0, occupation_low);
            }
            if(!v.isZero()) {
                if(occupation_low == matrix.length) mkPlace(true);
                if(occupation_low - k > 0) {
                    System.arraycopy(matrix, k, matrix, k + 1, occupation_low - k);
                    System.arraycopy(low, k, low, k + 1, occupation_low - k);
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
                occupation_low++;
                lowCount[f.get(low[k]).dim() + 1]++;
                diagram[f.get(low[k]).dim() + 1].put(f.get(low[k]).getWeight(), f.get(i).getWeight());
            }else {
                addZero(i, p);
            }
            i++;
        }
    }

    private void evaluate() {
        int k = 0;
        for(int i = 0; i < occupation_zeroes && k != occupation_low; i++) {
            k = Util.binarySearch(zeroes[i], low, k, occupation_low);
            if(k < occupation_low && low[k] == zeroes[i]) {
                continue;
            }
            // add (a_i, inf) to diagram[p]
            diagram[f.get(zeroes[i]).dim() + 1].put(f.get(zeroes[i]).getWeight());
        }
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
            SBVector[] vectors = new SBVector[factor * matrix.length];
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

    public String toBarcodePlot() {
        Vector5D<String, String, Integer, int[], Integer> value = this.getIntervalArrays();
        int overhead = value.getThird();
        int[] groupSize = value.getFourth();
        int length = value.getFifth();
        String grouping = "c(";
        String groupLabels = "c(";
        for(int p = 0; p < f.dimension() + 2 - overhead; p++) {
            grouping += groupSize[p] + "" + (p + 1 != f.dimension() + 2 - overhead ? ", " : "");
            groupLabels += "expression('H'[" + p + "])" + (p + 1 != f.dimension() + 2 - overhead ? ", " : "");
        }
        grouping += ")";
        groupLabels += ")";
        String data = "data.frame(x=1:" + length + ", value1=value1, value2=value2)";
        return "library(tidyverse)\n\n" +
                "value1 <- " + value.getFirst() + "\n" +
                "value2 <- " + value.getSecond() + "\n" +
                "data <- " + data + "\n" +
                "fin <- which(is.finite(data$value2))\n" +
                "inf <- which(is.infinite(data$value2))\n" +
                "plot <- ggplot(data) +\n" +
                "  geom_segment(data=data[fin,], aes(x=x, xend=x, y=value1, yend=value2), color=\"black\", size=1) +\n" +
                "  geom_point(aes(x=x, y=value1), color=rgb(0.2,0.7,0.1,0.5), size=3 ) +\n" +
                "  geom_point(data=data[fin,], aes(x=x, y=value2), color=rgb(0.7,0.2,0.1,0.5), size=3 ) +\n" +
                "  coord_flip() +\n" +
                "  theme_light() +\n" +
                "  theme(\n" +
                "    legend.position = \"none\",\n" +
                "    panel.border = element_blank(),\n" +
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
                "                              size = 1) +\n" +
                "    geom_point(data=data[inf,], aes(x=x, y=value1), color=rgb(0.2,0.7,0.1,0.5), size=3 )\n" +
                "}\n" +
                "plot <- plot + scale_y_continuous(limits = c(x_min, x_max), expand=c(0,0))\n" +
                "print(plot)";
    }

    public String toDiagramPlot() {
        Vector5D<String, String, Integer, int[], Integer> value = this.getIntervalArrays();


        return "";
    }

    /**Calculates R-Script arrays a, b that contain the interval information. (a[i], b[i]) is in the diagram.
     * the overhead o meaning the amount of empty diagrams(necessary for plot code)
     * the group size-array containing the occupation of the nodes-array of the p-th diagram in p-th position.
     * the length l of the arrays.
     * @return (a,b,o,g,l)
     */
    private Vector5D<String,String, Integer, int[], Integer> getIntervalArrays() {
        String value1 = "c(";
        String value2 = "c(";
        int length = 0;
        int overhead = 0;
        int[] groupSize = new int[f.dimension() + 2 - overhead];
        for(int i = f.dimension() + 1; i >= 0; i--) {
            if(diagram[i].occ == 0) overhead++;
            else break;
        }
        for(int p = 0; p < f.dimension() + 2 - overhead; p++) {
            if(diagram[p].occ == 0) continue;
            for(int i = 0; i < diagram[p].occ; i++) {
                Diagram.Node n = diagram[p].nodes[i];
                for(int j = 0; j < n.occ; j++) {
                    for(int k = 0; k < n.multiplicity[j]; k++) {
                        value1 += n.a + (j + 1 != n.occ || k + 1 != n.multiplicity[j] ? ", " : "");
                        value2 += n.b[j] + (j + 1 != n.occ || k + 1 != n.multiplicity[j] ? ", " : "");
                        length++;
                        groupSize[p]++;
                    }
                }
                if(n.infMultiplicity != 0) {
                    value1 += (n.occ != 0 ? ", " : "") + n.a;
                    value2 += (n.occ != 0 ? ", " : "") + "Inf";
                    length++;
                    groupSize[p]++;
                }
                if(p + 1 != f.dimension() + 2 - overhead || i + 1 != diagram[p].occ) {
                    value1 += ", ";
                    value2 += ", ";
                }
            }
        }
        value1 += ")";
        value2 += ")";
        return new Vector5D<>(value1, value2, overhead, groupSize, length);
    }
}
