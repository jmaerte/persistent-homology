package com.jmaerte.data_struc.complex;

import com.jmaerte.data_struc.graph.WeightedGraph;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.lin_alg.BinaryVector;
import com.jmaerte.util.calc.Function;
import com.jmaerte.util.log.Logger;
import com.jmaerte.util.vector.Vector2D;
import com.jmaerte.util.vector.Vector3D;

import java.util.*;

public class Filtration implements Iterable<BinaryVector> {

    private Tree simplices;
    private int dim;
    private int n;
    private int size;
    private ArrayList<Tree> ordering;

    /**Generates a filtration of the k-skeleton of the order-n total complex K^n_{total}.
     * Generate the neighborhood complex of the graph of {@code valuation}.
     *
     * @param n Amount of vertices.
     * @param k dimension of the skeleton.
     * @param valuation function that maps the tuple (i,j) to the valuation of the edge {i,j}.
     */
    public Filtration(int n, int k, Function<Vector2D<Integer, Integer>, Double> valuation) {
        this.dim = -1;
        this.n = n;
        this.simplices = new Tree(-1, null, 0, 0);
        fill(k, valuation);
    }

    public void insert(WeightedGraph g) {
        for(int i = 0; i < n; i++) {
            simplices.subTrees.put(i, new Tree(i, simplices, 0, 1));
            for(int j = i + 1; j < n; j++) {
//                System.out.println(i + ", " + j + ": " + g.getWeight(i,j));
                simplices.subTrees.get(i).subTrees.put(j, new Tree(j, simplices.subTrees.get(i), g.getWeight(i,j), 2));
            }
        }
    }

    public void insert(int[] path, double valuation) {

    }

    /**Generates the maximal dimension-k filtration with the recursion formula:
     * if sigma is in filtration of dimension at max (k-1) with valuation v.
     * sigma Union {j} is in filtration with valuation valuation.eval(sigma, sigma.dim + 1, j).
     *
     * @param k dimension of the target filtration
     * @param valuation the recursion formula on the simplices. The call valuation.eval(sigma, i, j) means that sigma is of dimension i - 1 and we add j.
     */
    public void generate(int k, Function<Vector3D<int[], Integer, Integer>, Double> valuation) {
        HashMap<Double, LinkedList<Tree>> table = new HashMap<>();
        int[] sigma = new int[k + 1];
        for(int i = 0; i < n; i++) {
            sigma[0] = i;
            simplices.subTrees.put(i, new Tree(i, simplices, valuation.eval(new Vector3D<>(sigma, 0, i)), 1));
            table.computeIfAbsent(simplices.subTrees.get(i).filteredVal, m -> new LinkedList<>());
            table.get(simplices.subTrees.get(i).filteredVal).addLast(simplices.subTrees.get(i));
            generate(k, simplices.subTrees.get(i), sigma, valuation, table);
        }
    }

    private void generate(int k, Tree simplex, int[] sigma, Function<Vector3D<int[], Integer, Integer>, Double> valuation, HashMap<Double, LinkedList<Tree>> table) {
        if(simplex.depth > k) return;
        if(simplex.depth -1 > this.dim) this.dim = simplex.depth - 1;
        for(int j = simplex.node + 1; j < n; j++) {
            sigma[simplex.depth] = j;
            double val = valuation.eval(new Vector3D<>(sigma, simplex.depth, j));
            simplex.subTrees.put(j, new Tree(j, simplex, val, simplex.depth + 1));
            table.computeIfAbsent(val, m -> new LinkedList<>());
            table.get(val).addLast(simplex.subTrees.get(j));
            generate(k, simplex.subTrees.get(j), sigma, valuation, table);
        }
    }

    /**Computes the maximal dimension-k filtration with a 1-skeleton such that val({i,j}) = valuation.eval(i,j).
     *
     * @param k dimension of the target filtration
     * @param valuation the valuation function on the edges.
     */
    public void fill(int k, Function<Vector2D<Integer, Integer>, Double> valuation) {
        // Generate 1-skeleton
        insert(new WeightedGraph(n, valuation));

        // expand to neighborhood complex
        fill(k);
    }

    /**Generates the maximal dimension-k filtration with the same 1-skeleton than the current one.
     *
     * @param k dimension of the target filtration.
     */
    public void fill(int k) {
        int[] sigma = new int[k + 1];
        HashMap<Double, LinkedList<Tree>> table = new HashMap<>();
        table.put(0d, new LinkedList<>());
        table.get(0d).add(simplices);
        Logger.progress(n, "Building Neighborhood-Filtration");
        for(int i = 0; i < n; i++) {
            Logger.log(i + "\r");
            sigma[0] = i;
            table.get(0d).addLast(simplices.subTrees.get(i));
            for(int j = i + 1; j < n; j++) {
                sigma[1] = j;
                Tree curr = simplices.subTrees.get(i).subTrees.get(j);
                table.computeIfAbsent(curr.filteredVal, m -> new LinkedList<>());
                table.get(curr.filteredVal).add(simplices.subTrees.get(i).subTrees.get(j));
                fill(k, curr, sigma, table);
            }
            Logger.updateProgress(i);
        }
        Logger.close();
        pack(table);
    }

    private void fill(int k, Tree simplex, int[] sigma, HashMap<Double, LinkedList<Tree>> table) {
        if(simplex.depth > k) return;
        if(simplex.depth - 1 > this.dim) this.dim = simplex.depth - 1;
//        System.out.println(Arrays.toString(sigma) + " " + simplex.filteredVal + " " + simplex.depth);
        for(int j = simplex.node + 1; j < n; j++) {
            sigma[simplex.depth] = j;
//            System.out.println("\t" + Arrays.toString(sigma) + " " + (j-sigma[0]-1));
            double val = simplices.subTrees.get(sigma[0]).subTrees.get(j).filteredVal;
            for(int i = 1; i < simplex.depth; i++) {
                double curr = simplices.subTrees.get(sigma[i]).subTrees.get(j).filteredVal;
                if(curr > val) val = curr;
            }
            if(val < simplex.filteredVal) val = simplex.filteredVal;
            simplex.subTrees.put(j, new Tree(j, simplex, val, simplex.depth + 1));
            table.computeIfAbsent(val, m -> new LinkedList<>());
            table.get(val).addLast(simplex.subTrees.get(j));
            fill(k, simplex.subTrees.get(j), sigma, table);
        }
    }

    private void pack(HashMap<Double, LinkedList<Tree>> table) {
        this.ordering = new ArrayList<>();
        Set<Double> keySet = table.keySet();
        double[] keys = new double[keySet.size()];
        int k = 0;
        for(double d : keySet) {
            keys[k++] = d;
        }
        Arrays.sort(keys);
        k = 0;
        Logger.progress(keys.length, "Packing");
        for(int i = 0; i < keys.length; i++) {
            LinkedList<Tree> list = table.get(keys[i]);
            Collections.sort(list, Comparator.comparingInt(a -> a.depth));
            for(Tree t : list) {
                this.ordering.add(k, t);
                t.filteredInd = k++;
            }
            Logger.updateProgress(i);
        }
        Logger.close();
        this.size = k;
//        System.out.println(simplices);
    }

    public Tree get(int i) {
        return ordering.get(i);
    }

    public int dimension() {
        return dim + 1;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<BinaryVector> iterator() {
        return new Iterator<>() {

            int i = 0;

            @Override
            public boolean hasNext() {
                return i < ordering.size();
            }

            @Override
            public BinaryVector next() {
                if(!hasNext()) throw new NoSuchElementException();

                Tree t = ordering.get(i);
                int[] path = new int[t.depth];
                int i = 0;
                while(t.parent != null) {
                    path[t.depth - i] = t.node;
                    i++;
                    t = t.parent;
                }
                this.i++;
                return binaryVector(path, ordering.get(i).depth, ordering.get(i).filteredVal, ordering.get(i).filteredInd);
            }

            private BinaryVector binaryVector(int[] path, int depth, double filteredVal, int filteredInd) {
                Tree curr;
                int[] entries = new int[depth];
                for(int i = 0; i < depth; i++) {
                    curr = simplices;
                    for(int j = 0; j < depth; j++) {
                        if(i != j) {
                            curr = curr.subTrees.get(path[j]);
                        }
                    }
//                    int k = Util.binarySearch(curr.filteredInd, entries, 0, i);
//                    if(i - k > 0) {
//                        System.arraycopy(entries, k, entries, k + 1, i-k);
//                    }
//                    entries[k] = curr.filteredInd;
                    entries[i] = curr.filteredInd;
                }
                Arrays.sort(entries);
                return new BinaryVector(size, entries, entries.length, depth - 1, filteredVal, filteredInd);
            }
        };
    }


//    @Override
//    public Iterator<BinaryVector> iterator() {
//        return new Iterator<>() {
//            long ns = 0;
//
//            Tree next = simplices;
//            int[] path = new int[dim + 2];
//
//            @Override
//            public boolean hasNext() {
//                return next != null;
//            }
//
//            @Override
//            public BinaryVector next() {
//                if(!hasNext()) throw new NoSuchElementException();
//                long curr = System.nanoTime();
//                Tree res = next;
//                BinaryVector v = binaryVector(path, res.depth, res.filteredVal, res.filteredInd);
////                System.out.println(res.node + " " + res.filteredInd + " " + res.filteredVal);
//
//                for(int i = next.node + 1; i < n; i++) {
//                    if(next.subTrees.get(i) != null) {
//                        next = next.subTrees.get(i);
//                        this.path[next.depth - 1] = i;
//                        ns += (System.nanoTime() - curr);
//                        return v;
//                    }
//                }
//                while(true) {
//                    if(next.parent == null) {
//                        next = null;
//                        ns += (System.nanoTime() - curr);
//                        System.out.println("Iterator-time " + ns + "ns");
//                        return v;
//                    }
//                    next = next.parent;
//                    for(int i = path[next.depth] + 1; i < n; i++) {
//                        if(next.subTrees.get(i) != null) {
//                            this.path[next.depth] = i;
//                            next = next.subTrees.get(i);
//                            ns += (System.nanoTime() - curr);
//                            return v;
//                        }
//                    }
//                }
//            }
//
//            private BinaryVector binaryVector(int[] path, int depth, double filteredVal, int filteredInd) {
//                Tree curr;
//                int[] entries = new int[depth];
//                for(int i = 0; i < depth; i++) {
//                    curr = simplices;
//                    for(int j = 0; j < depth; j++) {
//                        if(i != j) {
//                            curr = curr.subTrees.get(path[j]);
//                        }
//                    }
////                    int k = Util.binarySearch(curr.filteredInd, entries, 0, i);
////                    if(i - k > 0) {
////                        System.arraycopy(entries, k, entries, k + 1, i-k);
////                    }
////                    entries[k] = curr.filteredInd;
//                    entries[i] = curr.filteredInd;
//                }
//                Arrays.sort(entries);
//                return new BinaryVector(size, entries, entries.length, depth - 1, filteredVal, filteredInd);
//            }
//        };
//    }

    public static Filtration vietoris(PointSet S, int k) {
        return new Filtration(S.size(), k, v -> S.d(v.getFirst(), v.getSecond()));
    }

    public static Filtration cech(PointSet S, int k) {
        return null;
    }

    public static Filtration example() {
        Filtration res = new Filtration(3, 2, v -> 0d);
        res.simplices.subTrees.get(0).subTrees.get(1).subTrees.get(2).filteredVal = 5;
        return res;
    }
}
