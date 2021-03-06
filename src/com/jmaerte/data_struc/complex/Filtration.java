package com.jmaerte.data_struc.complex;

import com.jmaerte.data_struc.point_set.Landmarks;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.lin_alg.BinaryVector;
import com.jmaerte.util.calc.Function;
import com.jmaerte.util.calc.Util;
import com.jmaerte.util.log.Logger;
import com.jmaerte.util.vector.Vector2D;
import com.jmaerte.util.vector.Vector4D;
import com.jmaerte.visualization.Visualization;
import processing.core.PApplet;

import java.io.BufferedWriter;
import java.text.DecimalFormat;
import java.util.*;

public class Filtration implements Iterable<BinaryVector> {

    private static final DecimalFormat numericalErrors = new DecimalFormat("#,############");

    private static boolean reduced = false;
    public Tree simplices;
    private int dim;
    private int n;
    private int size;
    private ArrayList<Tree> ordering;

    private PointSet S;

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
        this.simplices = new Tree(-1, null, 0, 0, n);
        fill(k, valuation);
    }

    public Filtration(int n) {
        this.dim = -1;
        this.n = n;
        this.simplices = new Tree(-1, null, 0, 0, n);
    }

//    public void insert(WeightedGraph g) {
//        for(int i = 0; i < n; i++) {
//            simplices.subTrees.put(i, new Tree(i, simplices, 0, 1));
//            for(int j = i + 1; j < n; j++) {
////                System.out.println(i + ", " + j + ": " + g.getWeight(i,j));
//                simplices.subTrees.get(i).subTrees.put(j, new Tree(j, simplices.subTrees.get(i), g.getWeight(i,j), 2));
//            }
//        }
//    }

    public void insert(int[] path, double valuation, HashMap<Double, LinkedList<Tree>> filteredIndizes) {
        if(path.length - 1 > dim) dim = path.length - 1;
        Tree curr = simplices;
        for(int i = 0; i < path.length; i++) {
            Tree child = curr.getChild(path[i]);
            if(child != null) {
                curr = child;
            } else {
                curr.setChild(path[i], new Tree(path[i], curr, 0, curr.depth + 1, n));
                curr = curr.getChild(path[i]);
            }
        }
        curr.filteredVal = valuation;
        filteredIndizes.computeIfAbsent(valuation, m -> new LinkedList<>());
        filteredIndizes.get(valuation).addLast(curr);
    }

    public void draw(PointSet<double[]> S, double epsilon, double delta, int width, boolean balls) {
        this.attachPointSet(S);
        draw(epsilon, delta, width, balls);
    }

    public void draw(double epsilon, double delta, int dimension, boolean balls) {
        Visualization.f = this;
        Visualization.epsilon = epsilon;
        Visualization.delta = delta;
        Visualization.frameDim = dimension;
        Visualization.S = this.S;
        Visualization.balls = balls;
        Visualization v = new Visualization();
        PApplet.runSketch(new String[]{""}, v);
//        PSurface surface = v.getSurface();
//        PSurfaceAWT.SmoothCanvas smoothCanvas = (PSurfaceAWT.SmoothCanvas)surface.getNative();
//        JFrame frame = (JFrame) smoothCanvas.getFrame();
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        Visualization.main(new String[]{"com.jmaerte.visualization.Visualization"});
    }

    /**Generates the maximal dimension-k filtration with the recursion formula:
     * if sigma is in filtration of dimension at max (k-1) with valuation v.
     * sigma Union {j} is in filtration with valuation valuation.eval(sigma, sigma.dim + 1, j).
     *
     * @param k dimension of the target filtration
     * @param valuation the recursion formula on the simplices. The call valuation.eval(sigma, i, j) means that sigma is of dimension i - 1 and we add j.
     */
    public <T> void generate(int k, Function<Vector4D<int[], T, Integer, Integer>, Vector2D<T, Double>> valuation) {
        HashMap<Double, LinkedList<Tree>> table = new HashMap<>();
        table.put(0d, new LinkedList<>());
        table.get(0d).add(simplices);
        int[] sigma = new int[k + 1];
        Logger.progress(n, "Building Function-Filtration");
        for(int i = 0; i < n; i++) {
            sigma[0] = i;
            Vector2D<T, Double> v = valuation.eval(new Vector4D<>(sigma, null, 0, i));
            simplices.setChild(i, new Tree(i, simplices, v.getSecond(), 1, n));
            table.computeIfAbsent(simplices.getChild(i).filteredVal, m -> new LinkedList<>());
            table.get(simplices.getChild(i).filteredVal).addLast(simplices.getChild(i));
            generate(k, simplices.getChild(i), sigma, v.getFirst(), valuation, table);
            Logger.updateProgress(i);
        }
        Logger.close();
        pack(table);
    }

    private <T> void generate(int k, Tree simplex, int[] sigma, T object, Function<Vector4D<int[], T, Integer, Integer>, Vector2D<T, Double>> valuation, HashMap<Double, LinkedList<Tree>> table) {
        if(simplex.depth > k) return;
        if(simplex.depth - 1 > this.dim) this.dim = simplex.depth - 1;
        for(int j = simplex.node + 1; j < n; j++) {
            sigma[simplex.depth] = j;
            Vector2D<T, Double> v = valuation.eval(new Vector4D<>(sigma, object, simplex.depth, j));
            simplex.setChild(j, new Tree(j, simplex, v.getSecond(), simplex.depth + 1, n));
            table.computeIfAbsent(v.getSecond(), m -> new LinkedList<>());
            table.get(v.getSecond()).addLast(simplex.getChild(j));
            generate(k, simplex.getChild(j), sigma, v.getFirst(), valuation, table);
        }
    }

    /**Computes the maximal dimension-k filtration with a 1-skeleton such that val({i,j}) = valuation.eval(i,j).
     * This is the k-skeleton of the clique-filtration of a complete graph, such as the vietoris- or witness-filtration.
     *
     * @param k dimension of the target filtration
     * @param valuation the valuation function on the edges.
     */
    public void fill(int k, Function<Vector2D<Integer, Integer>, Double> valuation) {
        // Generate 1-skeleton
        for(int i = 0; i < n; i++) {
            simplices.setChild(i, new Tree(i, simplices, 0, 1, n));
            for(int j = i + 1; j < n; j++) {
//                System.out.println(i + ", " + j + ": " + g.getWeight(i,j));
                simplices.getChild(i).setChild(j, new Tree(j, simplices.getChild(i), valuation.eval(new Vector2D<>(i, j)), 2, n));
            }
        }
//        insert(new WeightedGraph(n, valuation));

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
        Logger.progress(n, "Building Clique-Filtration");
        for(int i = 0; i < n; i++) {
            sigma[0] = i;
            table.get(0d).addLast(simplices.getChild(i));
            for(int j = i + 1; j < n; j++) {
                sigma[1] = j;
                Tree curr = simplices.getChild(i).getChild(j);
                table.computeIfAbsent(curr.filteredVal, m -> new LinkedList<>());
                table.get(curr.filteredVal).add(simplices.getChild(i).getChild(j));
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
            double val = simplices.getChild(sigma[0]).getChild(j).filteredVal;
            for(int i = 1; i < simplex.depth; i++) {
                double curr = simplices.getChild(sigma[i]).getChild(j).filteredVal;
                if(curr > val) val = curr;
            }
            if(val < simplex.filteredVal) val = simplex.filteredVal;
            simplex.setChild(j, new Tree(j, simplex, val, simplex.depth + 1, n));
            table.computeIfAbsent(val, m -> new LinkedList<>());
            table.get(val).addLast(simplex.getChild(j));
            fill(k, simplex.getChild(j), sigma, table);
        }
    }

    /**An implementation of the clique filtration for arbitrary graphs.
     *
     */
    public void graph() {

    }

    public void pack(HashMap<Double, LinkedList<Tree>> table) {
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

    public static void reduced(boolean r) {
        reduced = r;
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

    public int vertexSize() {
        return n;
    }

    public void attachPointSet(PointSet S) {
        this.S = S;
    }

    @Override
    public Iterator<BinaryVector> iterator() {
        return new Iterator<BinaryVector>() {
            int i = (reduced ? 0 : 1);

            @Override
            public boolean hasNext() {
                return i < ordering.size();
            }

            @Override
            public BinaryVector next() {
                if(!hasNext()) throw new NoSuchElementException();
                Tree t = ordering.get(i);
                int depth = t.depth;
                int[] path = new int[t.depth];
                int j = 1;
//                System.out.println(t.node + " " + t.depth);
                while(t.node != -1) {
//                    System.out.println(Arrays.toString(path));
                    path[depth - j++] = t.node;
                    t = t.parent;
                }
                return binaryVector(path, depth, ordering.get(i).filteredVal, ordering.get(i).filteredInd);
            }

            private BinaryVector binaryVector(int[] path, int depth, double filteredVal, int filteredInd) {
                i++;
                if(!reduced && depth == 1) {
                    return new BinaryVector(size, new int[]{}, 0, 0, filteredVal, filteredInd);
                }
                Tree curr;
                int[] entries = new int[depth];
                for(int i = 0; i < depth; i++) {
                    curr = simplices;
                    for(int j = 0; j < depth; j++) {
                        if(i != j) {
                            curr = curr.getChild(path[j]);
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

    public void write(BufferedWriter bw) throws Exception {
        for(int i = 1; i < size(); i++) {
            Tree t = get(i);
            double val = t.filteredVal;
            int[] path = new int[t.depth];
            for(int j = t.depth - 1; j >= 0; j--) {
                path[j] = t.node;
                t = t.parent;
            }
            bw.write("{");
            for(int j = 0; j < path.length; j++) {
                bw.write(path[j] + (j + 1 == path.length ? "" : ","));
            }
            bw.write("}");
            bw.write(" ");
            bw.write("" + val);
            if(i + 1 != size()) {
                bw.newLine();
            }
        }
        bw.flush();
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
//                        ns += (System.nanoTime() - curr);Integer
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
//                            this.path[next.depth] = i;Integer
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
        Filtration f = new Filtration(S.size(), k, v -> S.d(v.getFirst(), v.getSecond())/2);
        f.attachPointSet(S);
        return f;
    }

    /**Generates the k-skeleton of the cech filtration.
     * This is a geometric approximation of a point sample S in euclidean space.
     * The Cech complex for parameter epsilon is defined to be the nerve of the collection of all
     * balls of radius epsilon with base points S.
     * Therefore using the nerve theorem we know that the cech-complex for a good value of epsilon(good meaning that the collection of balls
     * is an open cover of the space the points got sampled from) has the same homotopy type as the metric space we sampled from.
     * Since simplicial homology is a homology theory in the sense of Eilenberg-Steenrod we get that the homology of this spaces
     * got to be the same(remark: only for this value of epsilon).
     * Now it is up to us to estimate this value of epsilon. This is done by looking at strong characteristics of the samples we investigate.
     * E.g. a whole in the middle of a point set sampled from a thickened circle.
     *
     * @param S The Sample point set
     * @param k dimension of the resulting filtration.
     * @return The filtration described above.
     */
    public static Filtration cech(PointSet<double[]> S, int k) {
        Filtration f = new Filtration(S.size());
        f.generate(k, Util.getCechFunction(S));
        f.attachPointSet(S);
        return f;
    }

    /**Generates the k-skeleton of the lazy-witness filtration.(Lazy in the sense that this filtration behaves to the witness filtration
     * as the vietoris does to the cech filtration)
     * This is a more topological kind of approximation than the vietoris or cech filtrations(which actually are geometric)
     * Because given a set of well-distributed points in a metric space we can approximate this metric space by using a
     * much larger set of data-points to determine the weight of the edges.
     *
     * @param L The landmark points that are used as the vertices of our filtration.
     * @param k dimension to calculate to.
     * @return W(L)_k
     */
    public static Filtration witness_lazy(Landmarks L, int k) {
        Filtration f = new Filtration(L.size(), k, Util.witness(L));
        f.attachPointSet(L);
        return f;
    }
}
