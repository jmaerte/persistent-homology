package com.jmaerte.data_struc.graph;

import com.jmaerte.util.log.Logger;
import com.jmaerte.util.vector.Vector2D;
import com.jmaerte.util.calc.Function;

public class WeightedGraph {

    private Node[] nodes;
    private int fill;
    private boolean complete;

    /**Creates a complete weighted Graph (G,w) where G=([0...n-1], E), E = {{x,y} | x,y in [0...n-1]}
     * and w({x,y}) = lambda(new Vector2D(x,y))
     *
     * @param n size of the complete graph to be created.
     * @param lambda a symmetric function in arguments (x,y) in [0...n-1]^2
     */
    public WeightedGraph(int n, Function<Vector2D<Integer, Integer>, Double> lambda) {
        complete = true;
        nodes = new Node[n];
        for(int i = 0; i < n; i++) {
            nodes[i] = new Node(i, n);
        }
        fill = n;
        Logger.progress(n, "Calculating graph");
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < i; j++) {
                double w = lambda.eval(new Vector2D<>(i, j));
                addEdge(i, j, w);
            }
            Logger.updateProgress(i);
        }
        Logger.close();
    }

    public WeightedGraph(int n) {
        nodes = new Node[n];
    }

    /**
     *
     * @param i label of first vertex
     * @param j label of second vertex
     * @param weight
     */
    public void addEdge(int i, int j, double weight) {
        assert i != j;
        nodes[i].addAdjacent(j, weight);
        nodes[j].addAdjacent(i, weight);
    }

    /**Add vertex.
     */
    public void addVertex() {
        assert fill < nodes.length;
        nodes[fill] = new Node(fill, nodes.length);
        fill++;
    }

    public boolean isComplete() {
        return complete;
    }

    /**The user has to guarantee that the edge is really existent.
     *
     * @param i
     * @param j
     * @return
     */
    public double getWeight(int i, int j) {
        int k = nodes[i].binarySearch(j);
        return nodes[i].weight[k];
    }

    public int size() {
        return fill;
    }

    public Node getNode(int i) {
        return nodes[i];
    }

    public class Node {
        private static final int initial = 16;

        private int[] adjacency;
        private double[] weight;
        private int adjacent;

        private int label;

        public Node(int label, int n) {
            this.label = label;
            adjacency = new int[initial];
            weight = new double[initial];
        }

        public void addAdjacent(int adj, double w) {
            int k = binarySearch(adj);
            if(k == adjacent || adjacency[k] != adj) {
                if(adjacent >= adjacency.length) {
                    mkPlace();
                }
                assert adjacent < adjacency.length;
                if(k != adjacent) {
                    System.arraycopy(adjacency, k, adjacency, k+1, adjacent - k);
                    System.arraycopy(weight, k, weight, k+1, adjacent - k);
                }
                adjacency[k] = adj;
                adjacent++;
            }
            weight[k] = w;
        }

        private void mkPlace() {
            int[] adj = new int[2 * this.adjacency.length];
            double[] w = new double[2 * this.weight.length];
            System.arraycopy(adjacency, 0, adj, 0, adjacent);
            System.arraycopy(weight, 0, w, 0, adjacent);
            this.adjacency = adj;
            this.weight = w;
        }

        public int getAdjacent(int i) {
            return adjacency[i];
        }

        public int binarySearch(int j) {
            if(adjacent == 0 || adjacency[adjacent - 1] < j) return adjacent;
            int min = 0;
            int max = adjacent;
            while(min < max) {
                int mid = (min + max)/2;
                if(adjacency[mid] < j) min = mid + 1;
                else if(adjacency[mid] > j) max = mid;
                else return mid;
            }
            return min;
        }
    }
}
