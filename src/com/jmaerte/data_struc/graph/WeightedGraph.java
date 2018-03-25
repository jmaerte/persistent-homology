package com.jmaerte.data_struc.graph;

import com.jmaerte.data_struc.point_set.VertexFactory;
import com.jmaerte.util.vector.Vector2D;
import com.jmaerte.util.calc.Function;
import com.jmaerte.util.calc.Util;

import java.util.concurrent.ThreadLocalRandom;

public class WeightedGraph {

    private Node[] nodes;
    private int fill;

    /**Creates a complete weighted Graph (G,w) where G=([0...n-1], E), E = {{x,y} | x,y in [0...n-1]}
     * and w({x,y}) = lambda(new Vector2D(x,y))
     *
     * @param n size of the complete graph to be created.
     * @param lambda a symmetric function in arguments (x,y) in [0...n-1]^2
     */
    public WeightedGraph(int n, Function<Vector2D<Integer, Integer>, Double> lambda) {
        nodes = new Node[n];
        for(int i = 0; i < n; i++) {
            nodes[i] = new Node(i, n);
        }
        fill = n;
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < i; j++) {
                double w = lambda.eval(new Vector2D<>(i, j));
                addEdge(i, j, w);
            }
        }
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
        private int[] adjacency;
        private double[] weight;
        private int adjacent;

        private int label;

        public Node(int label, int n) {
            this.label = label;
            adjacency = new int[n];
            weight = new double[n];
        }

        public void addAdjacent(int adj, double w) {
            int k = binarySearch(adj);
            if(k == adjacent || adjacency[k] != adj) {
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

    public static WeightedGraph vietoris(VertexFactory factory) {
        return new WeightedGraph(factory.size(), new Function<Vector2D<Integer, Integer>, Double>() {
            @Override
            public Double eval(Vector2D<Integer, Integer> x) {
                return factory.d(x.getFirst(), x.getSecond());
            }
        });
    }

    /**Generates the witness graph described in the bachelor thesis with minmax-process.
     *
     * @param factory point factory
     * @return witness graph.
     */
    public static WeightedGraph witness(VertexFactory factory, int n) {
        int[] L = new int[n];
        L[0] = ThreadLocalRandom.current().nextInt(0, factory.size());

        double[] min = new double[factory.size()];
        for(int i = 0; i < factory.size(); i++) {
            if(i != L[0]) min[i] = factory.d(i, L[0]);
            else min[i] = -1;
        }
        for(int i = 1; i < n; i++) {
            int index = -1;
            double max = -1;
            for(int j = 0; j < factory.size(); j++) {
                if(max < min[j]) {
                    index = j;
                    max = min[j];
                }
            }
            int k = Util.binarySearch(index, L, 0, i);
            System.out.println(index);
            if(k < i) {
                System.arraycopy(L, k, L, k + 1, i - k);
            }
            L[k] = index;
            for(int j = 0; j < factory.size(); j++) {
                if(j != index) {
                    double d = factory.d(index, j);
                    if(min[j] > d) min[j] = d;
                }else {
                    min[j] = -1;
                }
            }
        }
        return witness(factory, L);
    }

    /**Generates the witness graph with given landmarks set.
     *
     * @param factory
     * @param L sorted landmark set.
     * @return
     */
    public static WeightedGraph witness(VertexFactory factory, int[] L) {
        int[] D = new int[factory.size() - L.length];
        for(int i = 0, k = 0; i < factory.size(); i++) {
            if(L[k] == i) k++;
            else D[i - k] = i;
        }
        double[][] dist = new double[L.length][factory.size() - L.length];

        return null;
    }
}
