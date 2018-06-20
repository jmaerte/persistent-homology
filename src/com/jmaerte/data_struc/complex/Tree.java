package com.jmaerte.data_struc.complex;

import java.util.HashMap;
import java.util.Map;

public class Tree {

    protected int node;
    protected double filteredVal;
    protected int filteredInd;
    protected int depth;
    private Tree[] subTrees;
    protected Tree parent;

    public Tree(int node, Tree parent, double filteredVal, int depth, int totalSize) {
        this.node = node;
        this.depth = depth;
        this.filteredVal = filteredVal;
        this.parent = parent;
        this.subTrees = new Tree[totalSize - node - 1];
    }

    public Tree getChild(int i) {
        assert i > node;
        return subTrees[i-node-1];
    }

    public void setChild(int j, Tree t) {
        subTrees[j-node-1] = t;
    }

    public Tree getParent() {
        return parent;
    }

    public int depth() {
        return depth;
    }

    public double val() {
        return filteredVal;
    }

    public int node() {
        return node;
    }

    public String toString() {
        String s = "";
        s += "node: " + node + ", val: " + filteredVal + ", ind: " + filteredInd + "->[";
        for(Tree e : subTrees) {
            s += e.toString() + "; ";
        }
        s += "]";
        return s;
    }
}
