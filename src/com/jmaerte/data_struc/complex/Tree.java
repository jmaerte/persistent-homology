package com.jmaerte.data_struc.complex;

import java.util.HashMap;
import java.util.Map;

public class Tree {

    protected int node;
    protected double filteredVal;
    protected int filteredInd;
    protected int depth;
    protected HashMap<Integer, Tree> subTrees;
    protected Tree parent;

    public Tree(int node, Tree parent, double filteredVal, int depth) {
        this.node = node;
        this.depth = depth;
        this.filteredVal = filteredVal;
        this.parent = parent;
        this.subTrees = new HashMap<>();
    }

    public int depth() {
        return depth;
    }

    public double val() {
        return filteredVal;
    }

    public String toString() {
        String s = "";
        s += "node: " + node + ", val: " + filteredVal + ", ind: " + filteredInd + "->[";
        for(Map.Entry<Integer, Tree> e : subTrees.entrySet()) {
            s += e.getValue().toString() + "; ";
        }
        s += "]";
        return s;
    }
}
