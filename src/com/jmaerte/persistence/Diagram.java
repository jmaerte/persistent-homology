package com.jmaerte.persistence;

import com.jmaerte.util.calc.Util;

import java.util.Arrays;

public class Diagram {

    protected Node[] nodes;
    protected int occ;
    private final String INFINITY;
    private final double eps = 1e-8;

    protected Diagram(double max) {
        this(""+max);
    }

    protected Diagram() {
        this("inf");
    }

    private Diagram(String inf) {
        this.INFINITY = inf;
        nodes = new Node[2];
    }

    /**Adds the tuple (a,b) to the diagram.
     *
     * @param a birth time
     * @param b death time
     */
    public void put(double a, double b) {
        if(Math.abs(a - b) < eps) return;
        //if(a == b) return;
        int k = binarySearch(a);
        if(k < occ && nodes[k].a == a) {
            nodes[k].add(b);
        }else {
            if(occ + 1 == nodes.length) mkPlace();
            if(k != occ) {
                System.arraycopy(nodes, k, nodes, k + 1, occ - k);
            }
            nodes[k] = new Node(a);
            nodes[k].add(b);
            occ++;
        }
    }

    /**Adds the tuple (a,+inf) to the diagram
     *
     * @param a the birth time.
     */
    public void put(double a) {
        int k = binarySearch(a);
        if(k < occ && nodes[k].a == a) {
            nodes[k].inf();
        }else {
            if(occ + 1 == nodes.length) mkPlace();
            if(k != occ) {
                System.arraycopy(nodes, k, nodes, k + 1, occ - k);
            }
            nodes[k] = new Node(a);
            nodes[k].inf();
            occ++;
        }
    }

    private void mkPlace() {
        Node[] nodes = new Node[2 * this.nodes.length];
        System.arraycopy(this.nodes, 0, nodes, 0, occ);
        this.nodes = nodes;
    }

    public int binarySearch(double a) {
        if(occ == 0 || nodes[occ - 1].a < a) return occ;
        int min = 0;
        int max = occ;
        while(min < max) {
            int mid = (min + max)/2;
            if(nodes[mid].a < a) min = mid + 1;
            else if(nodes[mid].a > a) max = mid;
            else return mid;
        }
        return min;
    }

    protected class Node {
        private static final int initCap = 1;

        // birth coefficient
        protected double a;
        // death coefficients
        protected double[] b;
        // Multiplicity of coefficient tuples
        protected int[] multiplicity;
        protected int infMultiplicity;
        // Occupation of the b and multiplicity arrays.
        protected int occ;

        private Node(double a) {
            this.a = a;
            this.b = new double[initCap];
            this.multiplicity = new int[initCap];
            this.occ = 0;
        }

        private void add(double b) {
            int k = Util.binarySearch(b, this.b, 0, occ);
            if(k < occ && this.b[k] == b) {
                multiplicity[k]++;
            }else {
                if(occ + 1 == this.b.length) mkPlace();
                if(k != occ) {
                   System.arraycopy(this.b, k, this.b, k + 1, occ - k);
                   System.arraycopy(this.multiplicity, k, this.multiplicity, k + 1, occ - k);
                }
                this.b[k] = b;
                this.multiplicity[k] = 1;
                occ++;
            }
        }

        private void inf() {
            infMultiplicity++;
        }

        private void mkPlace() {
            double[] b = new double[2 * this.b.length];
            int[] multiplicity = new int[2 * this.multiplicity.length];
            System.arraycopy(this.b, 0, b, 0, occ);
            System.arraycopy(this.multiplicity, 0, multiplicity, 0, occ);
            this.b = b;
            this.multiplicity = multiplicity;
        }
    }

    public String toString() {
        String s = "[";
        for(int i = 0; i < occ; i++) {
            Node n = nodes[i];
            for(int j = 0; j < n.occ; j++) {
                s += "(" + n.a + ", " + n.b[j] + ") : " + n.multiplicity[j] + (j + 1 == n.occ && n.infMultiplicity == 0  ? "" : ", ");
            }
            if(n.infMultiplicity != 0) {
                s += "(" + n.a + ", " + INFINITY + ") : " + n.infMultiplicity + (i + 1 != occ ? ", " : "");
            }else if(i + 1 != occ) s += ", ";
        }
        return s + "]";
    }
}
