package com.jmaerte.data_struc.complex;

import com.jmaerte.data_struc.graph.WeightedGraph;
import com.jmaerte.data_struc.point_set.Euclidean;
import com.jmaerte.data_struc.point_set.PointSetUtils;
import com.jmaerte.util.calc.Util;
import com.jmaerte.util.vector.Vector2D;

import java.util.*;

public class NeighborhoodFiltration extends Filtration {

    public static final String LOGINTERSECTION = "LOG", LININTERSECTION = "LIN";

    private ArrayList<Simplex> simplices;//Simplices in ascending order(dimension -> elements)
    private Integer[] sigma; // permutation such that simplices o sigma = compatible ordering
    // Hence sigma is used to get from the compatible-ordering relation to the simplices relation.
    // tau is used to get from simplices ordering to the compatible ordering.
    private int[] tau;
    private WeightedGraph graph;
    private int k;
    private int dim;
    private String inter;

    /**Initializes a Neighbourhood-filtration analogous to the one described in
     * in section 1.3
     *
     * @param graph a weighted graph that indicates at which point of time a certain edge
     *              joins the filtration. Note that the 1-skeleton completely determines the filtration
     *              therefore this information suffices
     * @param k the maximal simplex-dimension to compute.
     */
    public NeighborhoodFiltration(WeightedGraph graph, int k, String inter) {
        this.inter = inter;
        simplices = new ArrayList<>();
        this.graph = graph;
        this.k = k;
        simplices.add(new Simplex(new int[]{}, 0));
        dim = -1;
        for(int i = 0; i < graph.size(); i++) {
            generate(new Simplex(new int[]{i}, 0), getLowerNeighbourhood(i));
        }
        Collections.sort(simplices, new SimplexComparator());
        sigma = new Integer[simplices.size()];
        tau = new int[sigma.length];
        for(int i = 0; i < sigma.length; i++) {
            sigma[i] = i;
        }

        Arrays.sort(sigma, new WeightComparator());
        for(int i = 0; i < tau.length; i++) {
            tau[sigma[i]] = i;
        }

//        System.out.println(simplices);
    }

    /**Binary searches for s without its j-th element in the ArrayList.
     *
     * @param maximum maximal index to search to.
     * @param s coface
     * @param c arbitrary comparator
     * @param simplices list to search in. It needs to be sorted with the standard ordering.
     * @return
     */
    public int binarySearch(int maximum, Simplex s, Comparator<Simplex> c, ArrayList<Simplex> simplices) {
        if(maximum == 0 || c.compare(s, simplices.get(maximum - 1)) > 0) return maximum;
        int min = 0;
        int max = maximum;
        while(min < max) {
            int mid = (min + max)/2;
            if(c.compare(s, simplices.get(mid)) > 0) min = mid + 1;
            else if(c.compare(s, simplices.get(mid)) < 0) max = mid;
            else return mid;
        }
        return min;
    }

    public void generate(Simplex simplex, int[] neighbors) {
        simplices.add(simplex);
//        System.out.println(simplex);
        if(simplex.dim() > dim) dim = simplex.dim();
        if(simplex.dim() + 1 > k) {
            // Done since we only want the k-skeleton and already added the simplex.
            return;
        }
        for(int i = 0; i < neighbors.length; i++) {
            int[] s = new int[simplex.vertices.length + 1];
            System.arraycopy(simplex.vertices, 0, s, 1, simplex.vertices.length);

            s[0] = neighbors[i];

            // calculate weight as the maximum of the weight of simplex itself and all edges between the vertices of simplex and the new one.
            double weight = 0;
            for(int j = 0; j < simplex.vertices.length; j++) {
                double curr = graph.getWeight(simplex.vertices[j], neighbors[i]);
                if(curr > weight) {
                    weight = curr;
                }
            }
            if(weight < simplex.getWeight()) weight = simplex.getWeight();
            Simplex sigma = new Simplex(s, weight);

            // Intersect neighborhoods and go into the next generation step.
            generate(sigma, this.intersection(neighbors, getLowerNeighbourhood(neighbors[i])));
        }
    }

    public Simplex get(int i) {
        return simplices.get(sigma[i]);
    }

    /**Returns the faces of a simplex given through its index in compatible ordering.
     * It works through searching for the simplex in the simplices array through binary search.
     * The binary search takes log(N) * O(k), where k is the dimension of the simplex and N = sum_{i = 1}^{k}\binom{n}{i}.
     * We can actually make N smaller by determining the range that the (k-1)-simplices lie in.
     * Then N would be \binom{n}{k}.
     *
     * @param i index of simplex in compatible ordering.
     * @return faces of simplex.
     */
    public int[] faces(int i) {
        Simplex s = simplices.get(sigma[i]);
        ArrayList<Integer> indices = new ArrayList<>();
        FaceComparator c;
        for(int j = 0; j < s.vertices.length; j++) {
            c = new FaceComparator(j);
            int k = binarySearch(sigma[i], s, c, simplices);
            if(k < sigma[i] && c.compare(s, simplices.get(k)) == 0) {
                indices.add(tau[k]);
            }
        }
        return indices.stream().mapToInt(Integer::valueOf).toArray();
    }

    private int[] getLowerNeighbourhood(int l) {
        WeightedGraph.Node node = graph.getNode(l);
        int k = node.binarySearch(l);
        int[] res = new int[k];
        for(int i = 0; i < k; i++) {
            res[i] = node.getAdjacent(i);
        }
        return res;
    }

    public int size() {
        return simplices.size();
    }

    public int dimension() {
        return dim;
    }

    public class FaceComparator implements Comparator<Simplex> {

        private int j;

        public FaceComparator(int j) {
            this.j = j;
        }

        // Initialize comparator which compares the given simplices in the following way:
        // Let f(s,j) denote the face of s that is missing the j-th vertex of s in a fixed ordering(that is given here)
        // we then compare s and t through comparing f(s,j) and t. The order of the arguments is important here.

        public int compare(Simplex s, Simplex t) {
            if(s.dim() - 1 != t.dim()) return s.dim() - 1 - t.dim();
            for(int i = 0; i < s.dim(); i++) {
                if(s.vertices[i + (i >= j ? 1 : 0)] != t.vertices[i]) {
                    return s.vertices[i + (i >= j ? 1 : 0)] - t.vertices[i];
                }
            }
            return 0;
        }
    }

    public class SimplexComparator implements Comparator<Simplex> {

        public int compare(Simplex s, Simplex t) {
            if(s.dim() != t.dim()) return s.dim() - t.dim();
            for(int i = 0; i <= s.dim(); i++) {
                if(s.vertices[i] != t.vertices[i]) return s.vertices[i] - t.vertices[i];
            }
            return 0;
        }
    }

    public class WeightComparator implements Comparator<Integer> {

        public int compare(Integer i, Integer j) {
            double x = simplices.get(i).getWeight() - simplices.get(j).getWeight();
            return (int) (Math.signum(x) * Math.ceil(Math.abs(x)));
        }
    }

    private int[] intersection(int[] a, int[] b) {
        switch(inter) {
            case "LOG":
                return Util.logIntersection(a, b);
            case "LIN":
                return Util.linIntersection(a, b);
            default: return Util.logIntersection(a, b);
        }
    }

    /**Returns a vector2D with first = logintersection time, second = linintersection time.
     *
     * @param runs
     * @param d
     * @param n
     * @return
     */
    public static Vector2D<Double, Double> testIntersection(int runs, int d, int n) {
        double a = 0;
        double b = 0;
        long ms = 0;
        for(int i = 0; i < runs; i++) {
            Euclidean S = PointSetUtils.getSphereData(d, n, 0, 1);
            ms = System.currentTimeMillis();
            new NeighborhoodFiltration(WeightedGraph.vietoris(S), n - 1, NeighborhoodFiltration.LOGINTERSECTION);
            a += System.currentTimeMillis() - ms;
            ms = System.currentTimeMillis();
            new NeighborhoodFiltration(WeightedGraph.vietoris(S), n - 1, NeighborhoodFiltration.LININTERSECTION);
            b += System.currentTimeMillis() - ms;
        }
        a /= runs;
        b /= runs;
        return new Vector2D<>(a, b);
    }
}
