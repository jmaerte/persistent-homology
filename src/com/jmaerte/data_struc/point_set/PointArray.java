package com.jmaerte.data_struc.point_set;

import java.text.DecimalFormat;

/**An implementation of the PointSet, that stores the Points of the Set as a double[].
 * An instance of this realizes a {@link PointSet} with cardinality <i>n</i> in euclidean <i>R^d</i>.
 */
public class PointArray implements PointSet {

    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private static DecimalFormat df4 = new DecimalFormat("#.####");

    private final int d, n;
    private final double[][] values;
    private Metric<double[]> m;

    public PointArray(int d, int n, Metric<double[]> m) {
        this.d = d;
        this.n = n;
        this.m = m;
        this.values = new double[n][d];
    }

    public double get(int i, int j) {
        assert i >= 0 && i < n && j >= 0 && j < d;
        return values[i][j];
    }

    /**Note that this is mutable. Therefore you should not manipulate the result of this method call.
     *
     * @param i index of the vector to get
     * @return vector to get
     */
    public double[] get(int i) {
        return values[i];
    }

    public void set(int i, int j, double val) {
        assert i >= 0 && i < n & j >= 0 && j < d;
        values[i][j] = val;
    }

    public int dimension() {
        return d;
    }

    public int size() {
        return n;
    }

    public String toString() {
        String s = "";
        for(int j = 0; j < d; j++) {
            for(int i = 0; i < n; i++) {
                s += df2.format(values[i][j]) + "\t";
            }
            s+= "\n";
        }
        return s;
    }

    public double d(Integer i, Integer j) {
        return m.d(get(i), get(j));
    }

    public double d(double[] x, double[] y) {
        return m.d(x, y);
    }

    public String toPlot() {
        if(d > 2) return "";
        String x = "c(";
        String y = "c(";
        for(int i = 0; i < n; i++) {
            if(i != 0) {
                x += ", ";
                y += ", ";
            }
            x += df4.format(get(i, 0));
            y += df4.format(get(i, 1));
        }
        x += ")";
        y += ")";
        return  "library(tidyverse)\n\n" +
                "x <- " + x + "\n" +
                "y <- " + y + "\n" +
                "data <- data.frame(x = x, y = y)\n" +
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
}
