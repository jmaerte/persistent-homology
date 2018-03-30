package com.jmaerte.persistence;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.lin_alg.SBVector;
import com.jmaerte.util.vector.Vector4D;
import com.jmaerte.util.calc.Util;
import com.jmaerte.util.vector.Vector5D;
import com.jmaerte.util.vector.Vector6D;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Julian on 27/02/2018.
 */
public class Persistence {

    private static final int factor = 10;
    private static final DecimalFormat df = new DecimalFormat("#.########", new DecimalFormatSymbols(Locale.US));

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
            System.out.print(i + "/" + f.size() + "\r");
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
        for(int i = 0; i < occupation_zeroes; i++) {
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
        // Color strings: note that they must be escaped by \" since we also want the rgb-function to be a possible input.
        String segmentColor = "\"black\"";
        String birthColor = "\"chartreuse4\"";
        String deathColor = "\"sienna4\"";

        Vector5D<String, String, int[], int[], Integer> value = this.getIntervalArrays();
        int[] nonTrivial = value.getThird();
        int[] groupSize = value.getFourth();
        int length = value.getFifth();
        String grouping = "c(";
        String groupLabelPos = "c(";
        String groupLabel = "c(";
        for(int p = 0; p < nonTrivial.length; p++) {
            grouping += (groupSize[p] + 0.5) + "" + (p + 1 != nonTrivial.length ? ", " : "");
            if(p == 0) {
                groupLabelPos += (groupSize[1]/2 + 0.5);
            }else {
                groupLabelPos += ", " + (0.5 + (double)(groupSize[p - 1] + groupSize[p])/2);
            }
            groupLabel += "expression('H'[" + (nonTrivial[p] - 1) + "])" + (p + 1 != nonTrivial.length ? ", " : "");
        }
        grouping += ")";
        groupLabelPos += ")";
        groupLabel += ")";
        String data = "data.frame(x=1:" + length + ", value1=value1, value2=value2)";
        return "library(tidyverse)\n\n" +
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
                "  geom_point(aes(x=x, y=value1), colour=" + birthColor + ", size=3, alpha = 0.5) +\n" +
                "  geom_point(data=data[fin,], aes(x=x, y=value2), colour=" + deathColor + ", size=3, alpha = 0.5) +\n" +
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
                "                              size = 1) +\n" +
                "    geom_point(data=data[inf,], aes(x=x, y=value1), colour=" + birthColor + ", size=3, alpha=0.5)\n" +
                "}\n" +
                "plot <- plot + scale_y_continuous(limits = c(x_min, x_max), expand=c(0,0)) +\n" +
                "               scale_x_continuous(breaks=groupLabelPos, label=groupLabel)\n" +
                "plot <- plot + geom_vline(data=vlines, aes(xintercept=as.numeric(x))) +\n" +
                "               geom_vline(aes(xintercept=0.5)) +\n" +
                "               geom_segment(aes(x=0.5, xend=" + (length + 0.5) + ", y=x_min, yend=x_min))\n" +
                "print(plot)";
    }

    public String toDiagramPlot() {
        Vector5D<String, String, int[], int[], Integer> value = this.getIntervalArrays();


        return "";
    }

    /**Calculates R-Script arrays a, b that contain the interval information. (a[i], b[i]) is in the diagram.
     * the overhead o meaning the amount of empty diagrams(necessary for plot code)
     * the group size-array containing the occupation of the nodes-array of the p-th diagram in p-th position.
     * the length l of the arrays.
     * @return (a,b,o,g,l)
     */
    private Vector5D<String,String, int[], int[], Integer> getIntervalArrays() {
        String value1 = "c(";
        String value2 = "c(";
        int length = 0;
        ArrayList<Integer> groupSize = new ArrayList<>();
        ArrayList<Integer> nonTrivial = new ArrayList<>();
        for(int p = 0; p < f.dimension() + 2; p++) {
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
}
