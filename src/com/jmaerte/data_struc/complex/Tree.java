package com.jmaerte.data_struc.complex;

import java.util.ArrayList;

public class Tree {

    protected int node;
    protected Tree[] subTrees;
    protected Tree parent;

    public Tree(int node, Tree parent) {
        this.node = node;
        this.parent = parent;
        this.subTrees = new Tree[node];
    }
}
